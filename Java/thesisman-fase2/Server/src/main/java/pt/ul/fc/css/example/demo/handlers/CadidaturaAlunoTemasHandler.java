package pt.ul.fc.css.example.demo.handlers;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.catalogs.AlunoCatalog;
import pt.ul.fc.css.example.demo.catalogs.TemaCatalog;
import pt.ul.fc.css.example.demo.dtos.TemaDTO;
import pt.ul.fc.css.example.demo.entities.Aluno;
import pt.ul.fc.css.example.demo.entities.Tema;

@Component
public class CadidaturaAlunoTemasHandler {

  @Autowired private TemaCatalog TemaCatalog;
  @Autowired private AlunoCatalog AlunoCatalog;

  public CadidaturaAlunoTemasHandler(TemaCatalog TemaCatalog, AlunoCatalog AlunoCatalog) {
    this.TemaCatalog = TemaCatalog;
    this.AlunoCatalog = AlunoCatalog;
  }

  public Optional<Aluno> getAlunoById(Long id) {
    return AlunoCatalog.getAluno(id);
  }

  public boolean updateAlunoFinalizadaCandidatura(Long id, boolean finalizadaCandidatura) {
    Optional<Aluno> aluno = AlunoCatalog.getAluno(id);

    if (aluno.isPresent()) {
      aluno.get().setFinalizadaCandidatura(finalizadaCandidatura);
      AlunoCatalog.addAluno(aluno.get());
      return true;
    } else {
      return false;
    }
  }

  public List<TemaDTO> getTemasByAnoLetivo(int anoLetivo) {
    return TemaCatalog.getTemasByAnoLetivo(anoLetivo);
  }

  public List<TemaDTO> getTemasByAluno(Long id) {
    Optional<Aluno> aluno = AlunoCatalog.getAluno(id);

    if (aluno.isPresent()) {
      return AlunoCatalog.getTemasByAluno(aluno.get());
    } else {
      return null;
    }
  }

  @Transactional
  public boolean cadidatarTema(Long idAluno, Long idTema) {
    Optional<Aluno> aluno = AlunoCatalog.getAluno(idAluno);
    Optional<Tema> tema = TemaCatalog.getTema(idTema);

    if (aluno.isPresent() && tema.isPresent()) {

      if (aluno.get().getTemas().contains(tema.get())) {

        return false;

      } else if (aluno.get().getTemas().size() >= 5) {

        return false;

      } else {

        aluno.get().addTema(tema.get());
        AlunoCatalog.addAluno(aluno.get());
        return true;
      }
    } else {
      return false;
    }
  }

  @Transactional
  public boolean removerTema(Long idAluno, Long idTema) {
    Optional<Aluno> aluno = AlunoCatalog.getAluno(idAluno);
    Optional<Tema> tema = TemaCatalog.getTema(idTema);

    if (aluno.isPresent() && tema.isPresent()) {

      if (aluno.get().getTemas().contains(tema.get())) {

        aluno.get().removeTema(tema.get());
        AlunoCatalog.addAluno(aluno.get());
        return true;

      } else {

        return false;
      }
    } else {
      return false;
    }
  }
}
