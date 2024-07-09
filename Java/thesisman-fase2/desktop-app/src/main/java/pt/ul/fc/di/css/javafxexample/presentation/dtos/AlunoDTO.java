package pt.ul.fc.di.css.javafxexample.presentation.dtos;

import java.util.List;

public class AlunoDTO {
  private Long id;
  private String nome;
  private int numero;
  private Long teseId;
  private Long temaAtribuidoId;
  private List<Long> defesasNormalIds;
  private List<Long> temasIds;
  private Long defesaFinalId;
  private boolean finalizadaCandidatura;

  public AlunoDTO() {}

  public AlunoDTO(
      Long id,
      String nome,
      int numero,
      Long defesaFinalId,
      Long teseId,
      Long temaAtribuidoId,
      List<Long> defesasNormalIds,
      List<Long> temasIds,
      boolean finalizadaCandidatura) {

    this.id = id;
    this.nome = nome;
    this.numero = numero;
    this.defesaFinalId = defesaFinalId;
    this.teseId = teseId;
    this.temaAtribuidoId = temaAtribuidoId;
    this.defesasNormalIds = defesasNormalIds;
    this.temasIds = temasIds;
    this.finalizadaCandidatura = finalizadaCandidatura;
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
