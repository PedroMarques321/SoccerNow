package pt.ul.fc.css.soccernow.javafx.model;

import javafx.beans.property.*;

public class Jogador {
    private final SimpleLongProperty id;
    private final SimpleStringProperty username;
    private final SimpleStringProperty email;
    private final SimpleStringProperty posicao;
    private final SimpleIntegerProperty numeroCamisa;

    public Jogador(long id, String username, String email, String posicao, int numeroCamisa) {
        this.id = new SimpleLongProperty(id);
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.posicao = new SimpleStringProperty(posicao);
        this.numeroCamisa = new SimpleIntegerProperty(numeroCamisa);
    }

    public long getId() { return id.get(); }
    public String getUsername() { return username.get(); }
    public String getEmail() { return email.get(); }
    public String getPosicao() { return posicao.get(); }
    public int getNumeroCamisa() { return numeroCamisa.get(); }

    public SimpleLongProperty idProperty() { return id; }
    public SimpleStringProperty usernameProperty() { return username; }
    public SimpleStringProperty emailProperty() { return email; }
    public SimpleStringProperty posicaoProperty() { return posicao; }
    public SimpleIntegerProperty numeroCamisaProperty() { return numeroCamisa; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jogador jogador = (Jogador) o;
        return getId() == jogador.getId();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getId());
    }

    @Override
    public String toString() {
        return username.get();
    }
}