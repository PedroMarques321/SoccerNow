package pt.ul.fc.css.soccernow.dtos;

import java.time.LocalDateTime;

public class EstatisticasDTO {
  private Long id;
  private Integer golos;
  private Integer cartoesAmarelos;
  private Integer cartoesVermelhos;
  private Long jogadorId; // referencia ao jogador
  private String jogadorNome; // nome do jogador para display
  private Long jogoId; // referencia ao jogo
  private LocalDateTime jogoDataHora; // data/hora do jogo para display
  private String jogoLocal; // local do jogo para display
  private String equipaCasa; // nome da equipa da casa para display
  private String equipaFora; // nome da equipa de fora para display

  public EstatisticasDTO() {}

  public EstatisticasDTO(
      Long id,
      Integer golos,
      Integer cartoesAmarelos,
      Integer cartoesVermelhos,
      Long jogadorId,
      String jogadorNome,
      Long jogoId,
      LocalDateTime jogoDataHora,
      String jogoLocal,
      String equipaCasa,
      String equipaFora) {
    this.id = id;
    this.golos = golos;
    this.cartoesAmarelos = cartoesAmarelos;
    this.cartoesVermelhos = cartoesVermelhos;
    this.jogadorId = jogadorId;
    this.jogadorNome = jogadorNome;
    this.jogoId = jogoId;
    this.jogoDataHora = jogoDataHora;
    this.jogoLocal = jogoLocal;
    this.equipaCasa = equipaCasa;
    this.equipaFora = equipaFora;
  }

  // Getters
  public Long getId() {
    return id;
  }

  public Integer getGolos() {
    return golos;
  }

  public Integer getCartoesAmarelos() {
    return cartoesAmarelos;
  }

  public Integer getCartoesVermelhos() {
    return cartoesVermelhos;
  }

  public Long getJogadorId() {
    return jogadorId;
  }

  public String getJogadorNome() {
    return jogadorNome;
  }

  public Long getJogoId() {
    return jogoId;
  }

  public LocalDateTime getJogoDataHora() {
    return jogoDataHora;
  }

  public String getJogoLocal() {
    return jogoLocal;
  }

  public String getEquipaCasa() {
    return equipaCasa;
  }

  public String getEquipaFora() {
    return equipaFora;
  }

  // Setters
  public void setId(Long id) {
    this.id = id;
  }

  public void setGolos(Integer golos) {
    this.golos = golos;
  }

  public void setCartoesAmarelos(Integer cartoesAmarelos) {
    this.cartoesAmarelos = cartoesAmarelos;
  }

  public void setCartoesVermelhos(Integer cartoesVermelhos) {
    this.cartoesVermelhos = cartoesVermelhos;
  }

  public void setJogadorId(Long jogadorId) {
    this.jogadorId = jogadorId;
  }

  public void setJogadorNome(String jogadorNome) {
    this.jogadorNome = jogadorNome;
  }

  public void setJogoId(Long jogoId) {
    this.jogoId = jogoId;
  }

  public void setJogoDataHora(LocalDateTime jogoDataHora) {
    this.jogoDataHora = jogoDataHora;
  }

  public void setJogoLocal(String jogoLocal) {
    this.jogoLocal = jogoLocal;
  }

  public void setEquipaCasa(String equipaCasa) {
    this.equipaCasa = equipaCasa;
  }

  public void setEquipaFora(String equipaFora) {
    this.equipaFora = equipaFora;
  }
}
