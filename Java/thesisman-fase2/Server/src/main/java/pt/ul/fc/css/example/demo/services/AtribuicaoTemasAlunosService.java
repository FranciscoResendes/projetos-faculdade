package pt.ul.fc.css.example.demo.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.entities.Aluno;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.handlers.AtribuicaoTemasAlunosHandler;

@Service
public class AtribuicaoTemasAlunosService {

  @Autowired private AtribuicaoTemasAlunosHandler atribuicaoTemasAlunosHandler;

  public List<Aluno> getAlunosComCandidaturaFinalizada(boolean finalizadaCandidatura) {
    return atribuicaoTemasAlunosHandler.getAlunoByfinalizadaCandidatura(finalizadaCandidatura);
  }

  public Aluno getAlunoById(Long id) {
    return atribuicaoTemasAlunosHandler.getAlunoById(id);
  }

  public Tema getTemaById(Long id) {
    return atribuicaoTemasAlunosHandler.getTemaById(id);
  }

  public void criarTese(Aluno aluno, Tema tema) {
    atribuicaoTemasAlunosHandler.criarTese(aluno, tema);
  }
}
