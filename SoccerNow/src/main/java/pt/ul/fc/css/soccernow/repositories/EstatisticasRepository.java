package pt.ul.fc.css.soccernow.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.soccernow.models.Estatisticas;

public interface EstatisticasRepository extends JpaRepository<Estatisticas, Long> {

  // Buscar estatísticas de um jogador específico
  @Query("SELECT e FROM Estatisticas e WHERE e.jogador.id = :jogadorId ORDER BY e.jogo.dataHora DESC")
  List<Estatisticas> findByJogadorId(@Param("jogadorId") Long jogadorId);

  // Buscar estatísticas de um jogo específico
  @Query("SELECT e FROM Estatisticas e WHERE e.jogo.id = :jogoId ORDER BY e.jogador.username")
  List<Estatisticas> findByJogoId(@Param("jogoId") Long jogoId);

  // Buscar estatísticas por data
  @Query("SELECT e FROM Estatisticas e WHERE DATE(e.jogo.dataHora) = :data ORDER BY e.jogador.username")
  List<Estatisticas> findByData(@Param("data") LocalDate data);

  // Buscar estatísticas entre datas
  @Query("SELECT e FROM Estatisticas e WHERE DATE(e.jogo.dataHora) BETWEEN :dataInicio AND :dataFim ORDER BY e.jogo.dataHora DESC")
  List<Estatisticas> findByDataBetween(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

  // Verificar se já existem estatísticas para um jogador num jogo específico
  @Query("SELECT COUNT(e) > 0 FROM Estatisticas e WHERE e.jogador.id = :jogadorId AND e.jogo.id = :jogoId")
  boolean existsByJogadorIdAndJogoId(@Param("jogadorId") Long jogadorId, @Param("jogoId") Long jogoId);

  // Buscar estatísticas com golos
  @Query("SELECT e FROM Estatisticas e WHERE e.golos > 0 ORDER BY e.jogo.dataHora DESC")
  List<Estatisticas> findEstatisticasComGolos();

  // Buscar estatísticas com cartões
  @Query("SELECT e FROM Estatisticas e WHERE (e.cartoesAmarelos > 0 OR e.cartoesVermelhos > 0) ORDER BY e.jogo.dataHora DESC")
  List<Estatisticas> findEstatisticasComCartoes();

  // Buscar estatísticas de uma equipa específica (jogadores que jogaram por essa equipa)
  @Query("SELECT e FROM Estatisticas e WHERE e.jogo.equipaCasa.id = :equipaId OR e.jogo.equipaFora.id = :equipaId ORDER BY e.jogo.dataHora DESC")
  List<Estatisticas> findByEquipaId(@Param("equipaId") Long equipaId);

  // Buscar top marcadores (jogadores com mais golos)
  @Query("SELECT e.jogador.id, e.jogador.username, SUM(e.golos) as totalGolos FROM Estatisticas e WHERE e.golos > 0 GROUP BY e.jogador.id, e.jogador.username ORDER BY totalGolos DESC")
  List<Object[]> findTopMarcadores();
} 