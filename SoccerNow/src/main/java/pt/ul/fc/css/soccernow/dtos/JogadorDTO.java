package pt.ul.fc.css.soccernow.dtos;

public class JogadorDTO {
  private Long id;
  private String username;
  private String email;
  private String posicao;
  private Integer numeroCamisa;
  private String password;

  public JogadorDTO() {}

  public JogadorDTO(
      Long id,
      String username,
      String email,
      String password,
      String posicao,
      Integer numeroCamisa) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.posicao = posicao;
    this.numeroCamisa = numeroCamisa;
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

  public String getPosicao() {
    return posicao;
  }

  public Integer getNumeroCamisa() {
    return numeroCamisa;
  }

  public String getPassword() {
    return password;
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

  public void setPosicao(String posicao) {
    this.posicao = posicao;
  }

  public void setNumeroCamisa(Integer numeroCamisa) {
    this.numeroCamisa = numeroCamisa;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
