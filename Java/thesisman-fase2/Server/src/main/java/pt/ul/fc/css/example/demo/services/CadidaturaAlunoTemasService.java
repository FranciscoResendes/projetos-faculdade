package pt.ul.fc.css.example.demo.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.dtos.AlunoDTO;
import pt.ul.fc.css.example.demo.dtos.TemaDTO;
import pt.ul.fc.css.example.demo.entities.Aluno;
import pt.ul.fc.css.example.demo.handlers.CadidaturaAlunoTemasHandler;

@Service
public class CadidaturaAlunoTemasService {

  @Autowired private CadidaturaAlunoTemasHandler cadidaturaAlunoTemasHandler;

  public CadidaturaAlunoTemasService(CadidaturaAlunoTemasHandler cadidaturaAlunoTemasHandler) {
    this.cadidaturaAlunoTemasHandler = cadidaturaAlunoTemasHandler;
  }

  public Optional<AlunoDTO> getAlunoById(Long id) {
    return cadidaturaAlunoTemasHandler.getAlunoById(id).map(Aluno::toAlunoDTO);
  }

  public boolean updateAlunoFinalizadaCandidatura(Long id, boolean finalizadaCandidatura) {
    return cadidaturaAlunoTemasHandler.updateAlunoFinalizadaCandidatura(id, finalizadaCandidatura);
  }

  public List<TemaDTO> getTemasByAnoLetivo(int anoLetivo) {
    return cadidaturaAlunoTemasHandler.getTemasByAnoLetivo(anoLetivo);
  }

  public List<TemaDTO> getTemasByAluno(Long id) {
    return cadidaturaAlunoTemasHandler.getTemasByAluno(id);
  }

  public boolean candidatarTema(AlunoDTO alunoDTO, TemaDTO temaDTO) {
    return cadidaturaAlunoTemasHandler.cadidatarTema(alunoDTO.getId(), temaDTO.getId());
  }

  public boolean removerTema(AlunoDTO alunoDTO, TemaDTO temaDTO) {
    System.out.println("Candidatar tema" + alunoDTO.getId() + " " + temaDTO.getId());

    return cadidaturaAlunoTemasHandler.removerTema(alunoDTO.getId(), temaDTO.getId());
  }
}
