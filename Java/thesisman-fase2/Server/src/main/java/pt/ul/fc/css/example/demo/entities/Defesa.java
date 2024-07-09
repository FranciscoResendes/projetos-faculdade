package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Entity
public class Defesa {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Integer nota;

  @Temporal(TemporalType.TIMESTAMP)
  private Date data;

  @ManyToMany private List<Docente> juri;
  @OneToOne private Tese tese;
  private boolean isFinal;
  private String sala;

  public Defesa() {}

  public Defesa(Date data, List<Docente> juri, boolean isFinal, String sala) {
    this.juri = juri;
    this.data = data;
    this.nota = null;
    this.tese = null;
    this.isFinal = isFinal;
    this.sala = sala;
  }

  public Long getId() {
    return this.id;
  }

  public Integer getNota() {
    return this.nota;
  }

  public Date getDate() {
    return this.data;
  }

  public List<Docente> getJuri() {
    return this.juri;
  }

  public Tese getTese() {
    return this.tese;
  }

  public boolean getIsFinal() {
    return this.isFinal;
  }

  public String getSala() {
    return this.sala;
  }

  public void setNota(Integer novaNota) {
    this.nota = novaNota;
  }
}
