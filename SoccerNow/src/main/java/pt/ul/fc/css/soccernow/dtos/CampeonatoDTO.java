package pt.ul.fc.css.soccernow.dtos;

import java.time.LocalDate;

public class CampeonatoDTO {
    private Long id;
    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String modalidade; // "PONTOS" ou "ELIMINATORIA"
    private Integer numJogosRealizados;
    private Integer numJogosARealizar;

    public CampeonatoDTO() {}

    public CampeonatoDTO(Long id, String nome, LocalDate dataInicio, LocalDate dataFim, String modalidade, Integer numJogosRealizados, Integer numJogosARealizar) {
        this.id = id;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.modalidade = modalidade;
        this.numJogosRealizados = numJogosRealizados;
        this.numJogosARealizar = numJogosARealizar;
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

    public Integer getNumJogosRealizados() {
        return numJogosRealizados;
    }

    public void setNumJogosRealizados(Integer numJogosRealizados) {
        this.numJogosRealizados = numJogosRealizados;
    }

    public Integer getNumJogosARealizar() {
        return numJogosARealizar;
    }
    
    public void setNumJogosARealizar(Integer numJogosARealizar) {
        this.numJogosARealizar = numJogosARealizar;
    }
}