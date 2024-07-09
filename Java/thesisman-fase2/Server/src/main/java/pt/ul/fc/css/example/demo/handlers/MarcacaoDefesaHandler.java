package pt.ul.fc.css.example.demo.handlers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.catalogs.AlunoCatalog;
import pt.ul.fc.css.example.demo.catalogs.DefesaCatalog;
import pt.ul.fc.css.example.demo.dtos.DefesaDTO;
import pt.ul.fc.css.example.demo.entities.Aluno;
import pt.ul.fc.css.example.demo.entities.Defesa;
import pt.ul.fc.css.example.demo.entities.Docente;
import pt.ul.fc.css.example.demo.repositories.DocenteRepository;

@Component
public class MarcacaoDefesaHandler {
  @Autowired private DefesaCatalog defesaCat;
  @Autowired private AlunoCatalog alunoCat;
  @Autowired private DocenteRepository docRep;
  private Aluno aluno;

  public MarcacaoDefesaHandler(DefesaCatalog defesaCat, AlunoCatalog alunoCat) {
    this.defesaCat = defesaCat;
    this.alunoCat = alunoCat;
  }

  public void marcarDefesa(Defesa novaDefesa, int numeroAluno) {
    this.aluno = alunoCat.getAlunoByNumber(numeroAluno);
    List<Defesa> listaDefesas = defesaCat.getAllDefesas();
    boolean isDisponivel = true;

    if (!novaDefesa.getSala().equals("")) {
      for (Defesa d : listaDefesas) {
        if (d.getSala().equals(novaDefesa.getSala())) {
          isDisponivel = false;
        }
      }
    }
    if (isDisponivel) {
      if (!novaDefesa.getIsFinal()) {
        this.aluno.addDefesaNormal(novaDefesa);
      } else {
        this.aluno.setDefesaFinal(novaDefesa);
      }
      defesaCat.addDefesa(novaDefesa);
    }
  }

  public Defesa toDefesa(DefesaDTO dto) {
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
      Date d = formatter.parse(dto.getData());
      String sala = dto.getSala();
      Boolean isFinal = dto.getIsFinal().equals("sim") ? true : false;
      int juri1 = Integer.parseInt(dto.getJuriUm());
      int juri2 = Integer.parseInt(dto.getJuriDois());
      int juri3 = -1;

      if (dto.getJuriTres().length() > 0) {
        juri3 = Integer.parseInt(dto.getJuriTres());
      }

      List<Docente> juris = new ArrayList<>();
      Docente doc1 = docRep.findByNumero(juri1);
      Docente doc2 = docRep.findByNumero(juri2);
      Docente doc3 = docRep.findByNumero(juri3);
      if (doc1 == null || doc2 == null) {
        return null;
      }
      if (isFinal && doc3 == null) {
        return null;
      }
      if (!isFinal && doc3 != null) {
        return null;
      }
      juris.add(doc1);
      juris.add(doc2);
      juris.add(doc3);

      return new Defesa(d, juris, isFinal, sala);

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
