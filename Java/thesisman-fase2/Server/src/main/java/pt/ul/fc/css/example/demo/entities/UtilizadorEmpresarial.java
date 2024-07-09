package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class UtilizadorEmpresarial extends Utilizador {

  @Column(nullable = false)
  private String empresa;

  public UtilizadorEmpresarial() {}

  public UtilizadorEmpresarial(String nome, int numero, String empresa) {
    this.nome = nome;
    this.numero = numero;
    this.empresa = empresa;
  }

  public String getEmpresa() {
    return this.empresa;
  }
}
