package pt.ul.fc.css.example.demo.dtos;

import java.util.List;

public class TemaDTO {
  private Long id;
  private String titulo;
  private int remuneracao;
  private String descricao;
  private int anoLetivo;
  private List<String> mestradosCompativeis;
  private Integer autorId;
  private boolean available;

  // constructors, getters and setters

  public TemaDTO() {}

  public TemaDTO(
      String titulo,
      int remuneracao,
      String descricao,
      int anoLetivo,
      List<String> mestradosCompativeis,
      Integer autorId) {
    this.titulo = titulo;
    this.remuneracao = remuneracao;
    this.descricao = descricao;
    this.anoLetivo = anoLetivo;
    this.mestradosCompativeis = mestradosCompativeis;
    this.autorId = autorId;
    this.available = true;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public int getRemuneracao() {
    return remuneracao;
  }

  public void setRemuneracao(int remuneracao) {
    this.remuneracao = remuneracao;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public int getAnoLetivo() {
    return anoLetivo;
  }

  public void setAnoLetivo(int anoLetivo) {
    this.anoLetivo = anoLetivo;
  }

  public List<String> getMestradosCompativeis() {
    return mestradosCompativeis;
  }

  public void setMestradosCompativeis(List<String> mestradosCompativeis) {
    this.mestradosCompativeis = mestradosCompativeis;
  }

  public Integer getAutorId() {
    return autorId;
  }

  public void setAutorId(Integer autorId) {
    this.autorId = autorId;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }
}
