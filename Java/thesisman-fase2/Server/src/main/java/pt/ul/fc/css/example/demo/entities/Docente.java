package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.Entity;

@Entity
public class Docente extends Utilizador {

  public Docente() {}

  public Docente(String nome, int numero) {
    this.nome = nome;
    this.numero = numero;
  }
  
}
