package pt.ul.fc.css.example.demo.catalogs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.dtos.TemaDTO;
import pt.ul.fc.css.example.demo.entities.Aluno;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.repositories.AlunoRepository;

@Component
public class AlunoCatalog {

  @Autowired private final AlunoRepository alunoRepository;

  public AlunoCatalog(AlunoRepository alunoRepository) {
    this.alunoRepository = alunoRepository;
  }

  public Aluno addAluno(Aluno aluno) {
    return alunoRepository.save(aluno);
  }

  public List<Aluno> getAlunoByfinalizadaCandidatura(boolean finalizadaCandidatura) {
    return alunoRepository.findByFinalizadaCandidaturaAndTemaAtribuidoIsNull(finalizadaCandidatura);
  }

  public Optional<Aluno> getAluno(Long id) {
    return alunoRepository.findById(id);
  }

  public Aluno getAlunoById(Long id) {
    return alunoRepository.findById(id).get();
  }

  public Iterable<Aluno> getAllAlunos() {
    return alunoRepository.findAll();
  }

  public void deleteAluno(Long id) {
    alunoRepository.deleteById(id);
  }

  public List<TemaDTO> getTemasByAluno(Aluno aluno) {
    List<TemaDTO> temas = new ArrayList<TemaDTO>();

    for (Tema tema : aluno.getTemas()) {
      TemaDTO temaDTO = tema.toTemaDTO();
      if (tema.isAvailable()) {
        temas.add(temaDTO);
      }
    }

    return temas;
  }

  public Aluno getAlunoByNumber(int num) {
    return alunoRepository.findByNumero(num);
  }
}
