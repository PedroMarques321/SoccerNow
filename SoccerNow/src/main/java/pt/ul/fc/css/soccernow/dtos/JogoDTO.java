package pt.ul.fc.css.soccernow.dtos;

public class JogoDTO {
  private Long id;
  private String dataHora;
  private String local;
  private Long equipaCasaId;
  private Long equipaVisitanteId;
  private Long arbitroPrincipalId;
  private Long[] arbitrosAuxiliaresIds;
  private Long[] jogadoresCasaIds;
  private Long[] jogadoresVisitanteIds;
  private Long goleiroCasaId;
  private Long goleiroVisitanteId;
  private boolean jogoAmigavel;
  private Long campeonatoId;
  private String estado; // "AGENDADO", "EM_ANDAMENTO", "FINALIZADO", "CANCELADO"
  private Integer golosCasa;
  private Integer golosFora;

  // Construtor vazio
  public JogoDTO() {}

  // Getters e Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDataHora() {
    return dataHora;
  }

  public void setDataHora(String dataHora) {
    this.dataHora = dataHora;
  }

  public String getLocal() {
    return local;
  }

  public void setLocal(String local) {
    this.local = local;
  }

  public Long getEquipaCasaId() {
    return equipaCasaId;
  }

  public void setEquipaCasaId(Long equipaCasaId) {
    this.equipaCasaId = equipaCasaId;
  }

  public Long getEquipaVisitanteId() {
    return equipaVisitanteId;
  }

  public void setEquipaVisitanteId(Long equipaVisitanteId) {
    this.equipaVisitanteId = equipaVisitanteId;
  }

  public Long getArbitroPrincipalId() {
    return arbitroPrincipalId;
  }

  public void setArbitroPrincipalId(Long arbitroPrincipalId) {
    this.arbitroPrincipalId = arbitroPrincipalId;
  }

  public Long[] getArbitrosAuxiliaresIds() {
    return arbitrosAuxiliaresIds;
  }

  public void setArbitrosAuxiliaresIds(Long[] arbitrosAuxiliaresIds) {
    this.arbitrosAuxiliaresIds = arbitrosAuxiliaresIds;
  }

  public Long[] getJogadoresCasaIds() {
    return jogadoresCasaIds;
  }

  public void setJogadoresCasaIds(Long[] jogadoresCasaIds) {
    this.jogadoresCasaIds = jogadoresCasaIds;
  }

  public Long[] getJogadoresVisitanteIds() {
    return jogadoresVisitanteIds;
  }

  public void setJogadoresVisitanteIds(Long[] jogadoresVisitanteIds) {
    this.jogadoresVisitanteIds = jogadoresVisitanteIds;
  }

  public Long getGoleiroCasaId() {
    return goleiroCasaId;
  }

  public void setGoleiroCasaId(Long goleiroCasaId) {
    this.goleiroCasaId = goleiroCasaId;
  }

  public Long getGoleiroVisitanteId() {
    return goleiroVisitanteId;
  }

  public void setGoleiroVisitanteId(Long goleiroVisitanteId) {
    this.goleiroVisitanteId = goleiroVisitanteId;
  }

  public boolean isJogoAmigavel() {
    return jogoAmigavel;
  }

  public void setJogoAmigavel(boolean jogoAmigavel) {
    this.jogoAmigavel = jogoAmigavel;
  }

  public Long getCampeonatoId() {
    return campeonatoId;
  }

  public void setCampeonatoId(Long campeonatoId) {
    this.campeonatoId = campeonatoId;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public Integer getGolosCasa() { return golosCasa; }
  public void setGolosCasa(Integer golosCasa) { this.golosCasa = golosCasa; }

  public Integer getGolosFora() { return golosFora; }
  public void setGolosFora(Integer golosFora) { this.golosFora = golosFora; }
}
