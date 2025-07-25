package pt.ul.fc.css.soccernow.javafx.model;

import javafx.beans.property.*;

import java.util.List;


public class Equipa {
    private final SimpleLongProperty id;
    private final SimpleStringProperty nome;
    private final SimpleStringProperty cidade;
    private final SimpleStringProperty historicoJogos;
    private final SimpleStringProperty conquistas;
    private List<Jogador> jogadores;

    public Equipa(long id, String nome, String cidade, String historicoJogos, String conquistas) {
        this(id, nome, cidade, historicoJogos, conquistas, null);
    }

    public Equipa(long id, String nome, String cidade, String historicoJogos, String conquistas, List<Jogador> jogadores) {
        this.id = new SimpleLongProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.cidade = new SimpleStringProperty(cidade);
        this.historicoJogos = new SimpleStringProperty(historicoJogos);
        this.conquistas = new SimpleStringProperty(conquistas);
        this.jogadores = jogadores;
    }

    public long getId() { return id.get(); }
    public String getNome() { return nome.get(); }
    public String getCidade() { return cidade.get(); }
    public String getHistoricoJogos() { return historicoJogos.get(); }
    public String getConquistas() { return conquistas.get(); }
    public List<Jogador> getJogadores() { return jogadores; }

    public SimpleLongProperty idProperty() { return id; }
    public SimpleStringProperty nomeProperty() { return nome; }
    public SimpleStringProperty cidadeProperty() { return cidade; }
    public SimpleStringProperty historicoJogosProperty() { return historicoJogos; }
    public SimpleStringProperty conquistasProperty() { return conquistas; }
    public void setJogadores(List<Jogador> jogadores) { this.jogadores = jogadores; }

    @Override
    public String toString() {
        return nome.get();
    }
}