package pt.ul.fc.css.example.demo.dtos;

public class UtilizadorEmpresarialDTO {
  private String numero;
  private String nome;
  private String empresa;

  public String getNome() {
    return this.nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getNumero() {
    return this.numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public String getEmpresa() {
    return this.empresa;
  }

  public void setEmpresa(String empresa) {
    this.empresa = empresa;
  }
}
