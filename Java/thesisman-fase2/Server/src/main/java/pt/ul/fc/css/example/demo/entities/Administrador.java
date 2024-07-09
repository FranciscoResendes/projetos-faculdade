package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.Entity;

@Entity
public class Administrador extends Utilizador {

  public Administrador() {}

  public Administrador(String nome, int numero) {
    this.nome = nome;
    this.numero = numero;
  }
}
