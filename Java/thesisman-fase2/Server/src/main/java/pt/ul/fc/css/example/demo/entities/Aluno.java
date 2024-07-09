package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import pt.ul.fc.css.example.demo.dtos.AlunoDTO;

@Entity
public class Aluno {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String nome;

  @Column(nullable = false)
  private int numero;

  @OneToMany private List<Defesa> defesasNormal;

  @OneToOne
  @JoinColumn(name = "defesaFinalId", referencedColumnName = "id")
  private Defesa defesaFinal;

  @OneToOne
  @JoinColumn(name = "TeseId", referencedColumnName = "id")
  private Tese tese;

  @ManyToMany private List<Tema> temas;

  @OneToOne
  @JoinColumn(name = "temaId", referencedColumnName = "id")
  private Tema temaAtribuido;

  private boolean finalizadaCandidatura;

  public Aluno() {};

  public Aluno(String nome, int num) {
    this.numero = num;
    this.nome = nome;
    this.defesasNormal = new ArrayList<>();
    this.defesaFinal = null;
    this.tese = null;
    this.temas = new ArrayList<>();
    this.temaAtribuido = null;
    this.finalizadaCandidatura = false;
  }

  public AlunoDTO toAlunoDTO() {
    return new AlunoDTO(this);
  }

  public Long getId() {
    return this.id;
  }

  public String getNome() {
    return this.nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public int getNumero() {
    return this.numero;
  }

  public void setNumero(int numero) {
    this.numero = numero;
  }

  public Defesa getDefesaFinal() {
    return this.defesaFinal;
  }

  public void setDefesaFinal(Defesa defesaFinal) {
    this.defesaFinal = defesaFinal;
  }

  public List<Defesa> getDefesaNormal() {
    return this.defesasNormal;
  }

  public void addDefesaNormal(Defesa defesa) {
    this.defesasNormal.add(defesa);
  }

  public Tese getTese() {
    return this.tese;
  }

  public void setTese(Tese tese) {
    this.tese = tese;
  }

  public Tema getTemaAtribuido() {
    return temaAtribuido;
  }

  public List<Tema> getTemas() {
    return temas;
  }

  public void addTema(Tema tema) {
    this.temas.add(tema);
  }

  public void removeTema(Tema tema) {
    this.temas.remove(tema);
  }

  public void setTemaAtribuido(Tema tema) {
    this.temaAtribuido = tema;
  }

  public boolean isFinalizadaCandidatura() {
    return finalizadaCandidatura;
  }

  public void setFinalizadaCandidatura(boolean finalizadaCandidatura) {
    this.finalizadaCandidatura = finalizadaCandidatura;
  }
}
