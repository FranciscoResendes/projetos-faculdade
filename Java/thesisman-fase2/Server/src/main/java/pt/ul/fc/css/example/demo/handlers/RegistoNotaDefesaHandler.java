package pt.ul.fc.css.example.demo.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.catalogs.AlunoCatalog;
import pt.ul.fc.css.example.demo.catalogs.DefesaCatalog;
import pt.ul.fc.css.example.demo.entities.Aluno;
import pt.ul.fc.css.example.demo.entities.Defesa;

@Component
public class RegistoNotaDefesaHandler {
  @Autowired private AlunoCatalog alunoCat;
  @Autowired private DefesaCatalog defCat;

  public RegistoNotaDefesaHandler(AlunoCatalog alunoCat, DefesaCatalog defCat) {
    this.alunoCat = alunoCat;
    this.defCat = defCat;
  }

  public boolean registarNota(long defId, int nota, int numAluno) {
    Defesa def = defCat.getDefesa(defId);
    Aluno aluno = alunoCat.getAlunoByNumber(numAluno);
    int index = aluno.getDefesaNormal().indexOf(def);

    if (def != null && aluno != null) {
      if (def.getIsFinal()) {
        def.setNota(nota);
        defCat.addDefesa(def); //na realidade isto está a dar update
      } else if(index > -1){
        def.setNota(nota);
        defCat.addDefesa(def);
      }
      return true;
    }
    return false;
  }
}
