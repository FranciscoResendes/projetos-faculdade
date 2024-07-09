package pt.ul.fc.css.example.demo.handlers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.catalogs.AlunoCatalog;
import pt.ul.fc.css.example.demo.catalogs.DocenteCatalog;
import pt.ul.fc.css.example.demo.catalogs.TemaCatalog;
import pt.ul.fc.css.example.demo.catalogs.UtilizadorEmpresarialCatalog;
import pt.ul.fc.css.example.demo.entities.Aluno;
import pt.ul.fc.css.example.demo.entities.Dissertacao;
import pt.ul.fc.css.example.demo.entities.Docente;
import pt.ul.fc.css.example.demo.entities.Projeto;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.entities.UtilizadorEmpresarial;
import pt.ul.fc.css.example.demo.repositories.TeseRepository;

@Component
public class AtribuicaoTemasAlunosHandler {

  @Autowired private TemaCatalog TemaCatalog;
  @Autowired private AlunoCatalog AlunoCatalog;
  @Autowired private DocenteCatalog docenteCatalog;
  @Autowired private UtilizadorEmpresarialCatalog utilizadorEmpresarialCatalog;
  @Autowired private TeseRepository teseRepository;

  public AtribuicaoTemasAlunosHandler(TemaCatalog TemaCatalog, AlunoCatalog AlunoCatalog) {
    this.TemaCatalog = TemaCatalog;
    this.AlunoCatalog = AlunoCatalog;
  }

  public List<Aluno> getAlunoByfinalizadaCandidatura(boolean finalizadaCandidatura) {
    return AlunoCatalog.getAlunoByfinalizadaCandidatura(finalizadaCandidatura);
  }

  public Aluno getAlunoById(Long id) {
    return AlunoCatalog.getAlunoById(id);
  }

  public Tema getTemaById(Long id) {
    return TemaCatalog.getTemaById(id);
  }

  public void criarTese(Aluno aluno, Tema tema) {
    aluno.setTemaAtribuido(tema);
    tema.setAvailable(false);
    AlunoCatalog.addAluno(aluno);
    TemaCatalog.addTema(tema);

    if (tema.getAutor() != null) {
      Docente docente = docenteCatalog.findByNumero(tema.getAutor());
      UtilizadorEmpresarial utilizadorEmpresarial =
          utilizadorEmpresarialCatalog.findByNumero(tema.getAutor());

      if (docente != null) {
        Dissertacao tese = new Dissertacao(tema, docente);
        tese = teseRepository.save(tese);

        aluno.setTese(tese);
        AlunoCatalog.addAluno(aluno);
      } else if (utilizadorEmpresarial != null) {
        Projeto tese = new Projeto(tema, utilizadorEmpresarial);
        tese = teseRepository.save(tese);

        aluno.setTese(tese);
        AlunoCatalog.addAluno(aluno);
      }
    }
  }
}
