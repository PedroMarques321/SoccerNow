package pt.ul.fc.css.soccernow.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.soccernow.enums.EstadoJogo;
import pt.ul.fc.css.soccernow.models.Arbitro;
import pt.ul.fc.css.soccernow.models.Campeonato;
import pt.ul.fc.css.soccernow.models.Equipa;
import pt.ul.fc.css.soccernow.models.Estatisticas;
import pt.ul.fc.css.soccernow.models.Jogador;
import pt.ul.fc.css.soccernow.models.Jogo;
import pt.ul.fc.css.soccernow.repositories.ArbitroRepository;
import pt.ul.fc.css.soccernow.repositories.CampeonatoRepository;
import pt.ul.fc.css.soccernow.repositories.EquipaRepository;
import pt.ul.fc.css.soccernow.repositories.EstatisticasRepository;
import pt.ul.fc.css.soccernow.repositories.JogadorRepository;
import pt.ul.fc.css.soccernow.repositories.JogoRepository;

@Component
public class DataSeeder implements CommandLineRunner {

  @Autowired private JogadorRepository jogadorRepository;
  @Autowired private ArbitroRepository arbitroRepository;
  @Autowired private EquipaRepository equipaRepository;
  @Autowired private JogoRepository jogoRepository;
  @Autowired private CampeonatoRepository campeonatoRepository;
  @Autowired private EstatisticasRepository estatisticasRepository;

  @Override
  public void run(String... args) throws Exception {
    if (jogadorRepository.count() == 0) {
      System.out.println("Iniciando população da base de dados...");

      // Criar jogadores
      Jogador j1 = new Jogador("João Silva", "joao.silva@css.pt", "pass123", "Guarda-Redes", 1);
      Jogador j2 = new Jogador("Pedro Santos", "pedro.santos@css.pt", "pass123", "Defesa", 2);
      Jogador j3 = new Jogador("Miguel Costa", "miguel.costa@css.pt", "pass123", "Médio", 3);
      Jogador j4 = new Jogador("André Oliveira", "andre.oliveira@css.pt", "pass123", "Avançado", 4);
      Jogador j5 = new Jogador("Bruno Ferreira", "bruno.ferreira@css.pt", "pass123", "Defesa", 5);

      Jogador j6 =
          new Jogador("Carlos Rodrigues", "carlos.rodrigues@css.pt", "pass123", "Guarda-Redes", 6);
      Jogador j7 = new Jogador("Daniel Pereira", "daniel.pereira@css.pt", "pass123", "Defesa", 7);
      Jogador j8 = new Jogador("Eduardo Sousa", "eduardo.sousa@css.pt", "pass123", "Médio", 8);
      Jogador j9 = new Jogador("Fernando Lima", "fernando.lima@css.pt", "pass123", "Avançado", 9);
      Jogador j10 =
          new Jogador("Gabriel Martins", "gabriel.martins@css.pt", "pass123", "Médio", 10);

      Jogador j11 = new Jogador("Hugo Alves", "hugo.alves@css.pt", "pass123", "Guarda-Redes", 11);
      Jogador j12 = new Jogador("Ivo Ribeiro", "ivo.ribeiro@css.pt", "pass123", "Defesa", 12);
      Jogador j13 = new Jogador("José Cardoso", "jose.cardoso@css.pt", "pass123", "Médio", 13);
      Jogador j14 = new Jogador("Luís Mendes", "luis.mendes@css.pt", "pass123", "Avançado", 14);
      Jogador j15 =
          new Jogador("Manuel Teixeira", "manuel.teixeira@css.pt", "pass123", "Defesa", 15);

      Jogador j16 =
          new Jogador("Nuno Gonçalves", "nuno.goncalves@css.pt", "pass123", "Guarda-Redes", 16);
      Jogador j17 = new Jogador("Paulo Dias", "paulo.dias@css.pt", "pass123", "Defesa", 17);
      Jogador j18 =
          new Jogador("Ricardo Fernandes", "ricardo.fernandes@css.pt", "pass123", "Médio", 18);
      Jogador j19 = new Jogador("Sérgio Pinto", "sergio.pinto@css.pt", "pass123", "Avançado", 19);
      Jogador j20 = new Jogador("Tiago Carvalho", "tiago.carvalho@css.pt", "pass123", "Médio", 20);

      Jogador j21 = new Jogador("Vasco Gomes", "vasco.gomes@css.pt", "pass123", "Guarda-Redes", 21);
      Jogador j22 = new Jogador("Xavier Rocha", "xavier.rocha@css.pt", "pass123", "Defesa", 22);
      Jogador j23 = new Jogador("Yuri Silva", "yuri.silva@css.pt", "pass123", "Médio", 23);
      Jogador j24 = new Jogador("Zé Santos", "ze.santos@css.pt", "pass123", "Avançado", 24);
      Jogador j25 = new Jogador("António Costa", "antonio.costa@css.pt", "pass123", "Defesa", 25);

      List<Jogador> jogadores =
          Arrays.asList(
              j1, j2, j3, j4, j5, j6, j7, j8, j9, j10, j11, j12, j13, j14, j15, j16, j17, j18, j19,
              j20, j21, j22, j23, j24, j25);
      jogadorRepository.saveAll(jogadores);

      // Criar árbitros
      Arbitro a1 = new Arbitro("João Referee", "joao.referee@css.pt", "pass123", true);
      Arbitro a2 = new Arbitro("Pedro Arbitro", "pedro.arbitro@css.pt", "pass123", false);
      Arbitro a3 = new Arbitro("Miguel Judge", "miguel.judge@css.pt", "pass123", true);
      Arbitro a4 = new Arbitro("André Oficial", "andre.oficial@css.pt", "pass123", true);
      Arbitro a5 = new Arbitro("Bruno Referee", "bruno.referee@css.pt", "pass123", false);

      List<Arbitro> arbitros = Arrays.asList(a1, a2, a3, a4, a5);
      arbitroRepository.saveAll(arbitros);

      // Criar equipas
      Equipa e1 = new Equipa("FC Dragões", "Lisboa");
      e1.setHistoricoJogos("Fundada em 2010. Equipa com tradição no futsal universitário.");
      e1.setConquistas("Campeão Liga CSS 2023");
      e1.addJogador(j1);
      e1.addJogador(j2);
      e1.addJogador(j3);
      e1.addJogador(j4);
      e1.addJogador(j5);

      Equipa e2 = new Equipa("Sporting Leões", "Porto");
      e2.setHistoricoJogos("Fundada em 2012. Conhecida pela formação de jovens talentos.");
      e2.setConquistas("Vice-campeão Taça CSS 2023");
      e2.addJogador(j6);
      e2.addJogador(j7);
      e2.addJogador(j8);
      e2.addJogador(j9);
      e2.addJogador(j10);

      Equipa e3 = new Equipa("Benfica Eagles", "Braga");
      e3.setHistoricoJogos("Fundada em 2015. Equipa jovem e ambiciosa.");
      e3.setConquistas("3º lugar Liga Universitária 2022");
      e3.addJogador(j11);
      e3.addJogador(j12);
      e3.addJogador(j13);
      e3.addJogador(j14);
      e3.addJogador(j15);

      Equipa e4 = new Equipa("Porto Warriors", "Coimbra");
      e4.setHistoricoJogos("Fundada em 2008. Uma das equipas mais antigas da competição.");
      e4.setConquistas("Campeão Regional 2021");
      e4.addJogador(j16);
      e4.addJogador(j17);
      e4.addJogador(j18);
      e4.addJogador(j19);
      e4.addJogador(j20);

      Equipa e5 = new Equipa("Braga Fighters", "Aveiro");
      e5.setHistoricoJogos("Fundada em 2018. Equipa em crescimento constante.");
      e5.setConquistas("Sem conquistas registadas");
      e5.addJogador(j21);
      e5.addJogador(j22);
      e5.addJogador(j23);
      e5.addJogador(j24);
      e5.addJogador(j25);

      List<Equipa> equipas = Arrays.asList(e1, e2, e3, e4, e5);
      equipaRepository.saveAll(equipas);

      // Criar campeonatos
      Campeonato c1 = new Campeonato();
      c1.setNome("Liga CSS 2024");
      c1.setDataInicio(LocalDate.of(2024, 1, 15));
      c1.setDataFim(LocalDate.of(2024, 6, 30));
      c1.setModalidade("PONTOS");

      Campeonato c2 = new Campeonato();
      c2.setNome("Taça CSS");
      c2.setDataInicio(LocalDate.of(2024, 2, 1));
      c2.setDataFim(LocalDate.of(2024, 5, 15));
      c2.setModalidade("ELIMINATORIA");

      Campeonato c3 = new Campeonato();
      c3.setNome("Liga de Inverno");
      c3.setDataInicio(LocalDate.of(2024, 10, 1));
      c3.setDataFim(LocalDate.of(2025, 3, 31));
      c3.setModalidade("PONTOS");

      Campeonato c4 = new Campeonato();
      c4.setNome("Torneio de Verão");
      c4.setDataInicio(LocalDate.of(2024, 6, 1));
      c4.setDataFim(LocalDate.of(2024, 8, 31));
      c4.setModalidade("ELIMINATORIA");

      Campeonato c5 = new Campeonato();
      c5.setNome("Super Taça CSS");
      c5.setDataInicio(LocalDate.of(2024, 9, 1));
      c5.setDataFim(LocalDate.of(2024, 9, 30));
      c5.setModalidade("ELIMINATORIA");

      List<Campeonato> campeonatos = Arrays.asList(c1, c2, c3, c4, c5);
      campeonatoRepository.saveAll(campeonatos);

      // Criar jogos
      Jogo jogo1 = new Jogo(LocalDateTime.of(2024, 3, 15, 15, 0), "Estádio da Luz", e1, e2);
      jogo1.setArbitroPrincipal(a1);
      jogo1.setCampeonato(c1);
      jogo1.setEstado(EstadoJogo.FINALIZADO);
      jogo1.setGolosCasa(2);
      jogo1.setGolosFora(1);

      Jogo jogo2 = new Jogo(LocalDateTime.of(2024, 3, 22, 16, 30), "Estádio do Dragão", e3, e4);
      jogo2.setArbitroPrincipal(a2);
      jogo2.setCampeonato(c1);
      jogo2.setEstado(EstadoJogo.FINALIZADO);
      jogo2.setGolosCasa(0);
      jogo2.setGolosFora(3);

      Jogo jogo3 = new Jogo(LocalDateTime.of(2024, 4, 5, 17, 0), "Pavilhão Municipal", e5, e1);
      jogo3.setArbitroPrincipal(a3);
      jogo3.setCampeonato(c2);
      jogo3.setEstado(EstadoJogo.FINALIZADO);
      jogo3.setGolosCasa(1);
      jogo3.setGolosFora(1);

      Jogo jogo4 = new Jogo(LocalDateTime.of(2024, 12, 10, 15, 30), "Arena Futsal", e2, e3);
      jogo4.setArbitroPrincipal(a4);
      jogo4.setCampeonato(c3);
      jogo4.setEstado(EstadoJogo.AGENDADO);

      Jogo jogo5 = new Jogo(LocalDateTime.of(2024, 12, 20, 18, 0), "Centro Desportivo", e4, e5);
      jogo5.setArbitroPrincipal(a5);
      jogo5.setEstado(EstadoJogo.AGENDADO);

      List<Jogo> jogos = Arrays.asList(jogo1, jogo2, jogo3, jogo4, jogo5);
      jogoRepository.saveAll(jogos);

      // Criar estatísticas para jogos finalizados
      Estatisticas est1 = new Estatisticas();
      est1.setJogador(j4);
      est1.setJogo(jogo1);
      est1.setGolos(2);
      est1.setCartoesAmarelos(0);
      est1.setCartoesVermelhos(0);

      Estatisticas est2 = new Estatisticas();
      est2.setJogador(j9);
      est2.setJogo(jogo1);
      est2.setGolos(1);
      est2.setCartoesAmarelos(1);
      est2.setCartoesVermelhos(0);

      Estatisticas est3 = new Estatisticas();
      est3.setJogador(j19);
      est3.setJogo(jogo2);
      est3.setGolos(3);
      est3.setCartoesAmarelos(0);
      est3.setCartoesVermelhos(0);

      Estatisticas est4 = new Estatisticas();
      est4.setJogador(j24);
      est4.setJogo(jogo3);
      est4.setGolos(1);
      est4.setCartoesAmarelos(0);
      est4.setCartoesVermelhos(0);

      Estatisticas est5 = new Estatisticas();
      est5.setJogador(j4);
      est5.setJogo(jogo3);
      est5.setGolos(1);
      est5.setCartoesAmarelos(1);
      est5.setCartoesVermelhos(0);

      List<Estatisticas> estatisticas = Arrays.asList(est1, est2, est3, est4, est5);
      estatisticasRepository.saveAll(estatisticas);

      System.out.println("Base de dados populada com sucesso!");
      System.out.println("Dados criados:");
      System.out.println("   " + jogadores.size() + " Jogadores");
      System.out.println("   " + arbitros.size() + " Árbitros");
      System.out.println("   " + equipas.size() + " Equipas");
      System.out.println("   " + campeonatos.size() + " Campeonatos");
      System.out.println("   " + jogos.size() + " Jogos");
      System.out.println("   " + estatisticas.size() + " Estatísticas");
      System.out.println("Aplicação pronta para uso!");
    } else {
      System.out.println("Base de dados já contém dados. Saltando população inicial.");
    }
  }
}
