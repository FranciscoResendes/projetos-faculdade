package pt.ul.fc.di.css.javafxexample.presentation.model;

import pt.ul.fc.di.css.javafxexample.presentation.dtos.AlunoDTO;

public class Model {
  private static Model instance = null;
  private AlunoDTO alunoDTO;

  private Model() {}

  public static Model getInstance() {
    if (instance == null) {
      instance = new Model();
    }
    return instance;
  }

  public void setAlunoDTO(AlunoDTO alunoDTO) {
    this.alunoDTO = alunoDTO;
  }

  public AlunoDTO getAlunoDTO() {
    return alunoDTO;
  }
}
