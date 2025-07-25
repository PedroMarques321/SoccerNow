package pt.ul.fc.css.soccernow.models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Jogador")
public class Jogador extends User {

  @Column private String posicao;

  @Column(name = "numero_camisa")
  private Integer numeroCamisa;

  // @ManyToMany
  // @JoinTable(
  //     name = "jogador_equipa",
  //     joinColumns = @JoinColumn(name = "jogador_id"),
  //     inverseJoinColumns = @JoinColumn(name = "equipa_id"))
  // @JsonIgnoreProperties("jogadores")
  // private Set<Equipa> equipas = new HashSet<>();

  // @OneToMany(mappedBy = "jogador", cascade = CascadeType.ALL)
  // private Set<Estatisticas> estatisticas = new HashSet<>();

  public Jogador() {}

  // Construtor
  public Jogador(
      String username, String email, String password, String posicao, Integer numeroCamisa) {
    super(username, email, password);
    this.posicao = posicao;
    this.numeroCamisa = numeroCamisa;
  }

  // Getters
  public String getPosicao() {
    return posicao;
  }

  public Integer getNumeroCamisa() {
    return numeroCamisa;
  }

  // Setters
  public void setPosicao(String posicao) {
    this.posicao = posicao;
  }

  public void setNumeroCamisa(Integer numeroCamisa) {
    this.numeroCamisa = numeroCamisa;
  }
}
