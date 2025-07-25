package pt.ul.fc.css.soccernow.javafx.model;

import java.time.LocalDate;

public class Campeonato {
    private Long id;
    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String modalidade; // "PONTOS" ou "ELIMINATORIA"

    public Campeonato(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Campeonato(Long id, String nome, LocalDate dataInicio, LocalDate dataFim, String modalidade) {
        this.id = id;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.modalidade = modalidade;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public String getModalidade() {
        return modalidade;
    }

    @Override
    public String toString() {
        return nome;
    }
}