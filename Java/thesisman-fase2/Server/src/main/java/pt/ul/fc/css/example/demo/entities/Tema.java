package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
import pt.ul.fc.css.example.demo.dtos.TemaDTO;

@Entity
public class Tema {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String titulo;

  @Column(nullable = false)
  private int remuneracao;

  @Column(nullable = false)
  private String descricao;

  @Column(nullable = false)
  private int anoLetivo;

  @Column(nullable = false)
  List<String> mestradosCompativeis;

  private Integer autor;

  private boolean available;

  public Tema() {}

  public Tema(
      String titu,
      int remun,
      String desc,
      int anoLetivo,
      List<String> mestradosComp,
      Integer autor) {
    this.titulo = titu;
    this.remuneracao = remun;
    this.descricao = desc;
    this.anoLetivo = anoLetivo;
    this.mestradosCompativeis = mestradosComp;
    this.autor = autor;
    this.available = true;
  }

  public TemaDTO toTemaDTO() {
    TemaDTO temaDTO = new TemaDTO();
    temaDTO.setId(this.id);
    temaDTO.setTitulo(this.titulo);
    temaDTO.setRemuneracao(this.remuneracao);
    temaDTO.setDescricao(this.descricao);
    temaDTO.setMestradosCompativeis(this.mestradosCompativeis);
    temaDTO.setAnoLetivo(this.anoLetivo);
    temaDTO.setAutorId(this.autor);
    temaDTO.setAvailable(this.available);
    return temaDTO;
  }

  public Long getId() {
    return this.id;
  }

  public String getTitulo() {
    return this.titulo;
  }

  public int getRemuneracao() {
    return this.remuneracao;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public List<String> getMestradosCompativeis() {
    return this.mestradosCompativeis;
  }

  public Integer getAutor() {
    return this.autor;
  }

  public void setTitulo(String titu) {
    this.titulo = titu;
  }

  public void setRemuneracao(int remun) {
    this.remuneracao = remun;
  }

  public void setDescricao(String desc) {
    this.descricao = desc;
  }

  public void setMestradosCompativeis(List<String> mestradosComp) {
    this.mestradosCompativeis = mestradosComp;
  }

  public void setAutor(Integer autor) {
    this.autor = autor;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }
}
