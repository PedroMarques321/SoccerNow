package pt.ul.fc.css.soccernow.models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Arbitro")
public class Arbitro extends User {
  @Column(name = "certificado")
  private boolean certificado;

  public Arbitro() {}

  // Construtor
  public Arbitro(String username, String email, String password, boolean certificado) {
    super(username, email, password);
    this.certificado = certificado;
  }

  // Getters
  public boolean getCertificado() {
    return certificado;
  }

  // Setters
  public void setCertificado(boolean certificado) {
    this.certificado = certificado;
  }

  @Override
  public String toString() {
    return "Arbitro{"
        + "id="
        + getId()
        + ", nome='"
        + getUsername()
        + '\''
        + ", email='"
        + getEmail()
        + '\''
        + ", certificado="
        + certificado
        + '}';
  }
}
