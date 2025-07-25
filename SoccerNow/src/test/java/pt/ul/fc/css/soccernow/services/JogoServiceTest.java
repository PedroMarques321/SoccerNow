package pt.ul.fc.css.soccernow.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ul.fc.css.soccernow.dtos.JogoDTO;
import pt.ul.fc.css.soccernow.enums.EstadoJogo;
import pt.ul.fc.css.soccernow.models.Arbitro;
import pt.ul.fc.css.soccernow.models.Equipa;
import pt.ul.fc.css.soccernow.models.Jogador;
import pt.ul.fc.css.soccernow.models.Jogo;
import pt.ul.fc.css.soccernow.repositories.ArbitroRepository;
import pt.ul.fc.css.soccernow.repositories.EquipaRepository;
import pt.ul.fc.css.soccernow.repositories.JogadorRepository;
import pt.ul.fc.css.soccernow.repositories.JogoRepository;

@ExtendWith(MockitoExtension.class)
class JogoServiceTest {

  @Mock private JogoRepository jogoRepository;

  @Mock private EquipaRepository equipaRepository;

  @Mock private JogadorRepository jogadorRepository;

  @Mock private ArbitroRepository arbitroRepository;

  @Mock private EquipaService equipaService;

  @Mock private JogadorService jogadorService;

  @Mock private ArbitroService arbitroService;

  @InjectMocks private JogoService jogoService;

  private JogoDTO jogoDTO;

  @BeforeEach
  void setUp() {
    jogoDTO = new JogoDTO();
    jogoDTO.setDataHora("2024-03-15 14:00");
    jogoDTO.setLocal("Campo Municipal");
    jogoDTO.setEquipaCasaId(1L);
    jogoDTO.setEquipaVisitanteId(2L);
    jogoDTO.setArbitroPrincipalId(1L);
    jogoDTO.setJogadoresCasaIds(new Long[] {1L, 2L, 3L, 4L, 5L});
    jogoDTO.setJogadoresVisitanteIds(new Long[] {6L, 7L, 8L, 9L, 10L});
    jogoDTO.setGoleiroCasaId(1L);
    jogoDTO.setGoleiroVisitanteId(6L);
    jogoDTO.setJogoAmigavel(true);
  }

  @Test
  void deveCriarJogoComSucesso() {
    // Arrange - Setup completo dos mocks
    when(equipaService.existsById(1L)).thenReturn(true);
    when(equipaService.existsById(2L)).thenReturn(true);
    when(arbitroService.existsById(1L)).thenReturn(true);

    when(equipaRepository.findById(1L)).thenReturn(Optional.of(new Equipa()));
    when(equipaRepository.findById(2L)).thenReturn(Optional.of(new Equipa()));
    when(arbitroRepository.findById(1L)).thenReturn(Optional.of(new Arbitro()));

    // Mock para todos os jogadores
    for (Long id : jogoDTO.getJogadoresCasaIds()) {
      when(jogadorRepository.findById(id)).thenReturn(Optional.of(new Jogador()));
    }
    for (Long id : jogoDTO.getJogadoresVisitanteIds()) {
      when(jogadorRepository.findById(id)).thenReturn(Optional.of(new Jogador()));
    }

    // Criar jogo mock com dados necessários
    Jogo jogoMock = new Jogo();
    jogoMock.setId(1L);
    jogoMock.setDataHora(LocalDateTime.of(2024, 3, 15, 14, 0));
    jogoMock.setLocal("Campo Municipal");
    jogoMock.setEstado(EstadoJogo.AGENDADO);

    // Configurar equipes mock
    Equipa equipaCasa = new Equipa();
    equipaCasa.setId(1L);
    Equipa equipaFora = new Equipa();
    equipaFora.setId(2L);

    jogoMock.setEquipaCasa(equipaCasa);
    jogoMock.setEquipaFora(equipaFora);

    // Configurar jogadores mock
    Jogador guardaRedesCasa = new Jogador();
    guardaRedesCasa.setId(1L);
    Jogador guardaRedesFora = new Jogador();
    guardaRedesFora.setId(6L);

    jogoMock.setGuardaRedesCasa(guardaRedesCasa);
    jogoMock.setGuardaRedesFora(guardaRedesFora);

    // Configurar árbitro mock
    Arbitro arbitroPrincipal = new Arbitro();
    arbitroPrincipal.setId(1L);
    jogoMock.setArbitroPrincipal(arbitroPrincipal);

    when(jogoRepository.save(any(Jogo.class))).thenReturn(jogoMock);

    // Act & Assert
    assertDoesNotThrow(
        () -> {
          JogoDTO resultado = jogoService.criarJogo(jogoDTO);
          assertNotNull(resultado);
          assertEquals(1L, resultado.getId());
        });
  }

  @Test
  void deveRejeitarJogoComEquipesIguais() {
    // Arrange - Não precisa de mocks pois falha na validação básica
    jogoDTO.setEquipaVisitanteId(1L); // Mesma equipe para casa e visitante

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> jogoService.criarJogo(jogoDTO));

    assertEquals("As equipes devem ser diferentes", exception.getMessage());
  }

  @Test
  void deveRejeitarJogoComNumeroIncorretoDeJogadores() {
    // Arrange - Não precisa de mocks pois falha na validação básica
    jogoDTO.setJogadoresCasaIds(new Long[] {1L, 2L, 3L}); // Apenas 3 jogadores

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> jogoService.criarJogo(jogoDTO));

    assertEquals("Cada equipe deve ter exatamente 5 jogadores", exception.getMessage());
  }

  @Test
  void deveRejeitarJogoComGuardaRedesNaoNaEquipe() {
    // Arrange - Não precisa de mocks pois falha na validação básica
    jogoDTO.setGoleiroCasaId(99L); // Guarda-redes que não está na lista de jogadores

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> jogoService.criarJogo(jogoDTO));

    assertEquals(
        "Guarda-redes da casa deve estar entre os jogadores da equipe", exception.getMessage());
  }

  @Test
  void deveRejeitarJogoNaoAmigavelSemCampeonato() {
    // Arrange - Não precisa de mocks pois falha na validação básica
    jogoDTO.setJogoAmigavel(false);
    jogoDTO.setCampeonatoId(null);

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> jogoService.criarJogo(jogoDTO));

    assertEquals("Jogos não amigáveis devem pertencer a um campeonato", exception.getMessage());
  }
}
