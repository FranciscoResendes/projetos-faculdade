package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.Entity;

@Entity
public class Dissertacao extends Tese {

  public Dissertacao() {}

  public Dissertacao(Tema tema, Docente orientador) {
    this.tema = tema;
    this.orientador = orientador;
    this.fileData = null;
  }
}
