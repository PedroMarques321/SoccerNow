package pt.ul.fc.css.soccernow.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.ul.fc.css.soccernow.models.Equipa;

public interface EquipaRepository extends JpaRepository<Equipa, Long> {

  // Validacoes para nomes unicos
  boolean existsByNome(String nome);

  @Query(
      "SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Equipa e WHERE e.nome = :nome"
          + " AND e.id != :id")
  boolean existsByNomeAndIdNot(@Param("nome") String nome, @Param("id") Long id);

  // Buscar equipas que tenham um jogador com um id específico
  @Query("SELECT e FROM Equipa e JOIN e.jogadores j WHERE j.id = :jogadorId")
  List<Equipa> findEquipasByJogadorId(@Param("jogadorId") Long jogadorId);

  // Verificar se jogador já está numa equipa
  @Query(
      "SELECT COUNT(e) > 0 FROM Equipa e JOIN e.jogadores j WHERE e.id = :equipaId AND j.id ="
          + " :jogadorId")
  boolean existsByEquipaIdAndJogadorId(
      @Param("equipaId") Long equipaId, @Param("jogadorId") Long jogadorId);
}
