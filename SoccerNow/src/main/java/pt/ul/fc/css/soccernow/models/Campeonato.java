package pt.ul.fc.css.soccernow.models;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "campeonatos")
public class Campeonato {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String nome;

  @Column(nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("dataInicio")
  private LocalDate dataInicio;

  @Column
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("dataFim")
  private LocalDate dataFim;

  @Column(nullable = false)
  private String modalidade; // "PONTOS" ou "ELIMINATORIA"

  // Construtor vazio
  public Campeonato() {}

  // Getters e Setters
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

  public LocalDate getDataInicio() {
    return dataInicio;
  }

  public void setDataInicio(LocalDate dataInicio) {
    this.dataInicio = dataInicio;
  }

  public LocalDate getDataFim() {
    return dataFim;
  }

  public void setDataFim(LocalDate dataFim) {
    this.dataFim = dataFim;
  }

  public String getModalidade() {
    return modalidade;
  }

  public void setModalidade(String modalidade) {
    this.modalidade = modalidade;
  }
}
