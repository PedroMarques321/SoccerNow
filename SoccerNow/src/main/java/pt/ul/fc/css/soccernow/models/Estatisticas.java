package pt.ul.fc.css.soccernow.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "estatisticas")
public class Estatisticas {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private Integer golos;

  @Column(name = "cartoes_amarelos")
  private Integer cartoesAmarelos;

  @Column(name = "cartoes_vermelhos")
  private Integer cartoesVermelhos;

  @ManyToOne
  @JoinColumn(name = "jogador_id", nullable = false)
  private Jogador jogador;

  @ManyToOne
  @JoinColumn(name = "jogo_id", nullable = false)
  private Jogo jogo;

  public Estatisticas() {}

  // Construtor
  public Estatisticas(
      Integer golos,
      Integer cartoesAmarelos,
      Integer cartoesVermelhos) {
    this.golos = golos;
    this.cartoesAmarelos = cartoesAmarelos;
    this.cartoesVermelhos = cartoesVermelhos;
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

  public Jogador getJogador() {
    return jogador;
  }

  public Jogo getJogo() {
    return jogo;
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

  public void setJogador(Jogador jogador) {
    this.jogador = jogador;
  }

  public void setJogo(Jogo jogo) {
    this.jogo = jogo;
  }

  @Override
  public String toString() {
    return "Estatisticas{"
        + "id=" + id
        + ", golos=" + golos
        + ", cartoesAmarelos=" + cartoesAmarelos
        + ", cartoesVermelhos=" + cartoesVermelhos
        + ", jogador=" + (jogador != null ? jogador.getUsername() : "null")
        + ", jogo=" + (jogo != null ? jogo.getId() : "null")
        + '}';
  }
}
