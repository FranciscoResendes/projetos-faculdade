package pt.ul.fc.css.example.demo.dtos;

import java.util.List;
import java.util.stream.Collectors;
import pt.ul.fc.css.example.demo.entities.Aluno;
import pt.ul.fc.css.example.demo.entities.Defesa;
import pt.ul.fc.css.example.demo.entities.Tema;

public class AlunoDTO {

  private Long id;
  private String nome;
  private int numero;
  private List<Long> defesasNormalIds;
  private Long defesaFinalId;
  private Long teseId;
  private List<Long> temasIds;
  private Long temaAtribuidoId;
  private boolean finalizadaCandidatura;

  public AlunoDTO() {}

  public AlunoDTO(Aluno aluno) {
    this.id = aluno.getId();
    this.nome = aluno.getNome();
    this.numero = aluno.getNumero();
    this.defesasNormalIds =
        aluno.getDefesaNormal().stream().map(Defesa::getId).collect(Collectors.toList());
    this.defesaFinalId = aluno.getDefesaFinal() != null ? aluno.getDefesaFinal().getId() : null;
    this.teseId = aluno.getTese() != null ? aluno.getTese().getId() : null;
    this.temasIds = aluno.getTemas().stream().map(Tema::getId).collect(Collectors.toList());
    this.temaAtribuidoId =
        aluno.getTemaAtribuido() != null ? aluno.getTemaAtribuido().getId() : null;
    this.finalizadaCandidatura = aluno.isFinalizadaCandidatura();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public int getNumero() {
    return numero;
  }

  public void setNumero(int numero) {
    this.numero = numero;
  }

  public void setTeseId(Long id) {
    this.teseId = id;
  }

  public Object getTeseId() {
    return teseId;
  }

  public Long getTemaAtribuidoId() {
    return temaAtribuidoId;
  }

  public void setTemaAtribuidoId(Long temaAtribuidoId) {
    this.temaAtribuidoId = temaAtribuidoId;
  }

  public Object getDefesasNormalIds() {
    return defesasNormalIds;
  }

  public void addDefesaNormal(Long defesasNormalId) {
    this.defesasNormalIds.add(defesasNormalId);
  }

  public Object getTemasIds() {
    return temasIds;
  }

  public void addTemaId(Long temaId) {
    this.temasIds.add(temaId);
  }

  public Object getDefesaFinalId() {
    return defesaFinalId;
  }

  public void setDefesaFinalId(Long defesaFinalId) {
    this.defesaFinalId = defesaFinalId;
  }

  public boolean isFinalizadaCandidatura() {
    return finalizadaCandidatura;
  }

  public void setFinalizadaCandidatura(boolean finalizadaCandidatura) {
    this.finalizadaCandidatura = finalizadaCandidatura;
  }
}
