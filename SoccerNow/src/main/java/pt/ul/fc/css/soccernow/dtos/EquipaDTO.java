package pt.ul.fc.css.soccernow.dtos;

import java.util.List;
import java.util.Set;

public class EquipaDTO {
  private Long id;
  private String nome;
  private String cidade;
  private String historicoJogos;
  private String conquistas;
  private Set<JogadorDTO> jogadores;

  public EquipaDTO() {}

  public EquipaDTO(Long id, String nome, String cidade, String historicoJogos, String conquistas) {
    this.id = id;
    this.nome = nome;
    this.cidade = cidade;
    this.historicoJogos = historicoJogos;
    this.conquistas = conquistas;
  }

  // Getters
  public Long getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getCidade() {
    return cidade;
  }

  public String getHistoricoJogos() {
    return historicoJogos;
  }

  public String getConquistas() {
    return conquistas;
  }

  public Set<JogadorDTO> getJogadores() {
    return jogadores;
  }

  // Setters
  public void setId(Long id) {
    this.id = id;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public void setHistoricoJogos(String historicoJogos) {
    this.historicoJogos = historicoJogos;
  }

  public void setConquistas(String conquistas) {
    this.conquistas = conquistas;
  }

  public void setJogadores(Set<JogadorDTO> jogadores) {
    this.jogadores = jogadores;
  }
}
