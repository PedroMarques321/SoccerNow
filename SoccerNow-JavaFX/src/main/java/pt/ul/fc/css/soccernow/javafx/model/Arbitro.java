package pt.ul.fc.css.soccernow.javafx.model;

public class Arbitro {
    private long id;
    private String username;
    private String email;
    private boolean certificado;

    public Arbitro(long id, String username, String email, boolean certificado) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.certificado = certificado;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean isCertificado() {
        return certificado;
    }

    @Override
    public String toString() {
        return username + (certificado ? " (certificado)" : "");
    }
}