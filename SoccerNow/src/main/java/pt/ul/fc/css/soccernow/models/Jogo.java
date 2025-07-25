package pt.ul.fc.css.soccernow.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import pt.ul.fc.css.soccernow.enums.EstadoJogo;

@Entity
@Table(name = "jogos")
public class Jogo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDateTime dataHora;

  @Column(nullable = false)
  private String local;

  @ManyToOne
  @JoinColumn(name = "equipa_casa_id", nullable = false)
  private Equipa equipaCasa;

  @ManyToOne
  @JoinColumn(name = "equipa_fora_id", nullable = false)
  private Equipa equipaFora;

  // Árbitros do jogo
  @ManyToMany
  @JoinTable(
      name = "jogo_arbitro",
      joinColumns = @JoinColumn(name = "jogo_id"),
      inverseJoinColumns = @JoinColumn(name = "arbitro_id"))
  private Set<Arbitro> arbitros = new HashSet<>();

  // Arbitro Principal (obrigatório se houver mais do que um)
  @ManyToOne
  @JoinColumn(name = "arbitro_principal_id")
  private Arbitro arbitroPrincipal;

  // Jogadores titulares da equipa da casa (5 jogadores)
  @ManyToMany
  @JoinTable(
      name = "jogo_titulares_casa",
      joinColumns = @JoinColumn(name = "jogo_id"),
      inverseJoinColumns = @JoinColumn(name = "jogador_id"))
  private Set<Jogador> titularesCasa = new HashSet<>();

  // Jogadores titulares da equipa de fora (5 jogadores)
  @ManyToMany
  @JoinTable(
      name = "jogo_titulares_fora",
      joinColumns = @JoinColumn(name = "jogo_id"),
      inverseJoinColumns = @JoinColumn(name = "jogador_id"))
  private Set<Jogador> titularesFora = new HashSet<>();

  // Guarda - redes da casa (1 jogador)
  @ManyToOne
  @JoinColumn(name = "guarda_redes_casa_id")
  private Jogador guardaRedesCasa;

  // Guarda - redes de fora (1 jogador)
  @ManyToOne
  @JoinColumn(name = "guarda_redes_fora_id")
  private Jogador guardaRedesFora;

  // Golos marcados pela equipa da casa
  @Column private Integer golosCasa;

  // Golos marcados pela equipa de fora
  @Column private Integer golosFora;

  // Estado do jogo
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EstadoJogo estado = EstadoJogo.AGENDADO;

  // Campeonato (null se for jogo amigável)
  @ManyToOne
  @JoinColumn(name = "campeonato_id")
  private Campeonato campeonato;

  // Construtores
  public Jogo() {}

  public Jogo(LocalDateTime dataHora, String local, Equipa equipaCasa, Equipa equipaFora) {
    this.dataHora = dataHora;
    this.local = local;
    this.equipaCasa = equipaCasa;
    this.equipaFora = equipaFora;
  }

  // Getters
  public Long getId() {
    return id;
  }

  public LocalDateTime getDataHora() {
    return dataHora;
  }

  public String getLocal() {
    return local;
  }

  public Equipa getEquipaCasa() {
    return equipaCasa;
  }

  public Equipa getEquipaFora() {
    return equipaFora;
  }

  public Set<Arbitro> getArbitros() {
    return arbitros;
  }

  public Arbitro getArbitroPrincipal() {
    return arbitroPrincipal;
  }

  public Set<Jogador> getTitularesCasa() {
    return titularesCasa;
  }

  public Set<Jogador> getTitularesFora() {
    return titularesFora;
  }

  public Jogador getGuardaRedesCasa() {
    return guardaRedesCasa;
  }

  public Jogador getGuardaRedesFora() {
    return guardaRedesFora;
  }

  public Integer getGolosCasa() {
    return golosCasa;
  }

  public Integer getGolosFora() {
    return golosFora;
  }

  public EstadoJogo getEstado() {
    return estado;
  }

  public Campeonato getCampeonato() {
    return campeonato;
  }

  // Setters
  public void setId(Long id) {
    this.id = id;
  }

  public void setDataHora(LocalDateTime dataHora) {
    this.dataHora = dataHora;
  }

  public void setLocal(String local) {
    this.local = local;
  }

  public void setEquipaCasa(Equipa equipaCasa) {
    this.equipaCasa = equipaCasa;
  }

  public void setEquipaFora(Equipa equipaFora) {
    this.equipaFora = equipaFora;
  }

  public void setArbitros(Set<Arbitro> arbitros) {
    this.arbitros.clear();
    if (arbitros != null) {
      this.arbitros.addAll(arbitros);
    }
  }

  public void setArbitroPrincipal(Arbitro arbitroPrincipal) {
    this.arbitroPrincipal = arbitroPrincipal;
  }

  public void setTitularesCasa(Set<Jogador> titularesCasa) {
    this.titularesCasa.clear();
    if (titularesCasa != null) {
      this.titularesCasa.addAll(titularesCasa);
    }
  }

  public void setTitularesFora(Set<Jogador> titularesFora) {
    this.titularesFora.clear();
    if (titularesFora != null) {
      this.titularesFora.addAll(titularesFora);
    }
  }

  public void setGuardaRedesCasa(Jogador guardaRedesCasa) {
    this.guardaRedesCasa = guardaRedesCasa;
  }

  public void setGuardaRedesFora(Jogador guardaRedesFora) {
    this.guardaRedesFora = guardaRedesFora;
  }

  public void setGolosCasa(Integer golosCasa) {
    this.golosCasa = golosCasa;
  }

  public void setGolosFora(Integer golosFora) {
    this.golosFora = golosFora;
  }

  public void setEstado(EstadoJogo estado) {
    this.estado = estado;
  }

  public void setCampeonato(Campeonato campeonato) {
    this.campeonato = campeonato;
  }

  public boolean isJogoAmigavel() {
    return campeonato == null;
  }

  public boolean temResultado() {
    return golosCasa != null && golosFora != null;
  }

  public Equipa getEquipaVencedora() {
    if (!temResultado()) {
      return null; // Jogo ainda não tem resultado
    }
    if (golosCasa > golosFora) {
      return equipaCasa;
    } else if (golosFora > golosCasa) {
      return equipaFora;
    } else {
      return null; // Empate
    }
  }

  public void adicionarArbitro(Arbitro arbitro) {
    this.arbitros.add(arbitro);

    if (this.arbitroPrincipal == null) {
      this.arbitroPrincipal = arbitro; // Define o primeiro árbitro como principal
    }
  }

  public void definirResultado(int golosCasa, int golosFora) {
    this.golosCasa = golosCasa;
    this.golosFora = golosFora;
    this.estado = EstadoJogo.FINALIZADO; // Atualiza o estado do jogo
  }

  public boolean podeAlterarEquipas() {
    return estado.permiteAlteracaoEquipas();
  }

  public boolean podeRegistarResultado() {
    return estado.permiteRegistoResultado();
  }
}
