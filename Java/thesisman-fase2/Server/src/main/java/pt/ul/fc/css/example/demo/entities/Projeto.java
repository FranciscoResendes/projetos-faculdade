package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Projeto extends Tese {

  @ManyToOne private UtilizadorEmpresarial orientadorExterno;

  public Projeto() {}

  public Projeto(Tema tema, UtilizadorEmpresarial orientadorE) {
    this.tema = tema;
    this.orientador = null;
    this.orientadorExterno = orientadorE;
    this.fileData = null;
  }

  public void setOrientador(Docente docente) {
    this.orientador = docente;
  }

  public Docente getOrientador() {
    return this.orientador;
  }

  public UtilizadorEmpresarial getorientadorExterno() {
    return this.orientadorExterno;
  }
}
