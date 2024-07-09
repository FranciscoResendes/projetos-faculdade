package pt.ul.fc.css.example.demo.dtos;

public class DefesaDTO {

  private Long id;
  private String data;
  private String juriUm;
  private String juriDois;
  private String juriTres;
  private String isFinal;
  private String sala;

  public Long getId() {
    return this.id;
  }

  public String getData() {
    return this.data;
  }

  public String getJuriUm() {
    return this.juriUm;
  }

  public String getJuriDois() {
    return this.juriDois;
  }

  public String getJuriTres() {
    return this.juriTres;
  }

  public String getIsFinal() {
    return this.isFinal;
  }

  public String getSala() {
    return this.sala;
  }

  public void setId(long newId) {
    this.id = newId;
  }

  public void setData(String newDate) {
    this.data = newDate;
  }

  public void setJuriUm(String newJuri) {
    this.juriUm = newJuri;
  }

  public void setJuriDois(String newJuri) {
    this.juriDois = newJuri;
  }

  public void setJuriTres(String newJuri) {
    this.juriTres = newJuri;
  }

  public void setIsFinal(String newIsFinal) {
    this.isFinal = newIsFinal;
  }

  public void setSala(String newSala) {
    this.sala = newSala;
  }
}
