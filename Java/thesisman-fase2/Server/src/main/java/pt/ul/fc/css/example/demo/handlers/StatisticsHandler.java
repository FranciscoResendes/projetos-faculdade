package pt.ul.fc.css.example.demo.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.catalogs.AlunoCatalog;
import pt.ul.fc.css.example.demo.entities.Aluno;
import pt.ul.fc.css.example.demo.entities.Defesa;

@Component
public class StatisticsHandler {

  @Autowired private AlunoCatalog alunoCat;

  public StatisticsHandler() {}

  public float calculateStatistics() {
    Iterable<Aluno> alunos = alunoCat.getAllAlunos();
    int contadorAlunos = 0;
    int alunosPositivos = 0;

    for (Aluno a : alunos) {
      Defesa def = a.getDefesaFinal();
      if (def != null) {
        System.out.println("Nota: " + def.getNota());
        if (def.getNota() != null && def.getNota() > 9) {
          alunosPositivos++;
        }
        if (def.getNota() != null) {
          contadorAlunos++;
        }
      }
    }
    if (contadorAlunos == 0) {
      return 0;
    }

    float soma = (float) alunosPositivos / contadorAlunos;

    return soma * 100;
  }
}
