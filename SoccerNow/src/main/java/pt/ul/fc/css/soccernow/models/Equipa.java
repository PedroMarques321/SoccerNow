package pt.ul.fc.css.soccernow.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "equipas")
public class Equipa {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String nome;

  @Column private String cidade;

  @Column(name = "historico_jogos", columnDefinition = "TEXT")
  private String historicoJogos;

  @Column(name = "conquistas", columnDefinition = "TEXT")
  private String conquistas;

  @ManyToMany
  @JoinTable(
      name = "jogador_equipa",
      joinColumns = @JoinColumn(name = "equipa_id"),
      inverseJoinColumns = @JoinColumn(name = "jogador_id"))
  @JsonIgnoreProperties("equipas")
  private final Set<Jogador> jogadores = new HashSet<>();

  public Equipa() {}

  // Construtor
  public Equipa(String nome, String cidade) {
    this.nome = nome;
    this.cidade = cidade;
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

  public Set<Jogador> getJogadores() {
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

  public void addJogador(Jogador jogador) {
    jogadores.add(jogador);
  }

  public void removeJogador(Jogador jogador) {
    jogadores.remove(jogador);
  }

  @Override
  public String toString() {
    return "Equipa{"
        + "id="
        + id
        + ", nome='"
        + nome
        + '\''
        + ", cidade='"
        + cidade
        + '\''
        + ", historicoJogos='"
        + historicoJogos
        + '\''
        + ", conquistas='"
        + conquistas
        + '\''
        + '}';
  }
}
