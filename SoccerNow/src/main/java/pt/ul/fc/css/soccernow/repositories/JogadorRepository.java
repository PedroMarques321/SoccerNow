package pt.ul.fc.css.soccernow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.soccernow.models.Jogador;

public interface JogadorRepository extends JpaRepository<Jogador, Long> {

  // Usado para ao registar novo jogador verificar se o número de camisa já existe
  boolean existsByNumeroCamisa(Integer numeroCamisa);

  // Usado para ao atualizar um jogador verificar se o número de camisa já existe
  // @Query("SELECT CASE WHEN COUNT(j) > 0 THEN true ELSE false END FROM Jogador j WHERE
  // j.numeroCamisa = :numeroCamisa AND j.id != :id")
  // boolean existsByNumeroCamisaAndIdNot(Integer numeroCamisa, Long id);

  // Spring gera automaticamente??
  boolean existsByNumeroCamisaAndIdNot(Integer numeroCamisa, Long id);
}
