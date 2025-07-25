package pt.ul.fc.css.soccernow.dtos;

public class ArbitroDTO {
  private Long id;
  private String username;
  private String email;
  private String password;
  private boolean certificado;

  public ArbitroDTO() {}

  public ArbitroDTO(Long id, String username, String email, String password, boolean certificado) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.certificado = certificado;
  }

  // Getters
  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public boolean getCertificado() {
    return certificado;
  }

  // Setters
  public void setId(Long id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setCertificado(boolean certificado) {
    this.certificado = certificado;
  }
}
