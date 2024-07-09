package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Utilizador {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  @Column(nullable = false)
  protected String nome;

  @Column(nullable = false)
  protected int numero;

  protected Long getId() {
    return this.id;
  }

  protected String getNome() {
    return this.nome;
  }

  protected void setNome(String nome) {
    this.nome = nome;
  }

  protected int getNumero() {
    return this.numero;
  }

  protected void setNumero(int numero) {
    this.numero = numero;
  }
}
