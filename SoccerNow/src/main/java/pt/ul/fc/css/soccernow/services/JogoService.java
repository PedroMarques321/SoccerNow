package pt.ul.fc.css.soccernow.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import pt.ul.fc.css.soccernow.dtos.JogoDTO;
import pt.ul.fc.css.soccernow.enums.EstadoJogo;
import pt.ul.fc.css.soccernow.models.Arbitro;
import pt.ul.fc.css.soccernow.models.Campeonato;
import pt.ul.fc.css.soccernow.models.Equipa;
import pt.ul.fc.css.soccernow.models.Jogador;
import pt.ul.fc.css.soccernow.models.Jogo;
import pt.ul.fc.css.soccernow.repositories.ArbitroRepository;
import pt.ul.fc.css.soccernow.repositories.CampeonatoRepository;
import pt.ul.fc.css.soccernow.repositories.EquipaRepository;
import pt.ul.fc.css.soccernow.repositories.JogadorRepository;
import pt.ul.fc.css.soccernow.repositories.JogoRepository;

@Service
public class JogoService {

  @Autowired private JogoRepository jogoRepository;

  @Autowired private EquipaRepository equipaRepository;

  @Autowired private JogadorRepository jogadorRepository;

  @Autowired private ArbitroRepository arbitroRepository;

  @Autowired private CampeonatoRepository campeonatoRepository;

  @Autowired private EquipaService equipaService;

  @Autowired private JogadorService jogadorService;

  @Autowired private ArbitroService arbitroService;

  public JogoDTO criarJogo(JogoDTO jogoDTO) {
    validarJogo(jogoDTO);

    Jogo jogo = new Jogo();
    atualizarCamposJogo(jogo, jogoDTO);

    Jogo jogoSalvo = jogoRepository.save(jogo);
    return converterParaDTO(jogoSalvo);
  }

  public List<JogoDTO> listarJogos() {
    return jogoRepository.findAll().stream()
        .map(this::converterParaDTO)
        .collect(Collectors.toList());
  }

  public Optional<JogoDTO> buscarJogoPorId(Long id) {
    return jogoRepository.findById(id).map(this::converterParaDTO);
  }

  private void validarJogo(JogoDTO jogoDTO) {
    // 1. Validações básicas de dados (não dependem da BD)
    if (jogoDTO.getEquipaCasaId().equals(jogoDTO.getEquipaVisitanteId())) {
      throw new IllegalArgumentException("As equipes devem ser diferentes");
    }

    // Validar se tem exatamente 5 jogadores por equipe
    if (jogoDTO.getJogadoresCasaIds() == null
        || jogoDTO.getJogadoresVisitanteIds() == null
        || jogoDTO.getJogadoresCasaIds().length != 5
        || jogoDTO.getJogadoresVisitanteIds().length != 5) {
      throw new IllegalArgumentException("Cada equipe deve ter exatamente 5 jogadores");
    }

    // Validar se os goleiros estão entre os jogadores
    if (!contemJogador(jogoDTO.getJogadoresCasaIds(), jogoDTO.getGoleiroCasaId())) {
      throw new IllegalArgumentException(
          "Guarda-redes da casa deve estar entre os jogadores da equipe");
    }

    if (!contemJogador(jogoDTO.getJogadoresVisitanteIds(), jogoDTO.getGoleiroVisitanteId())) {
      throw new IllegalArgumentException(
          "Guarda-redes visitante deve estar entre os jogadores da equipe");
    }

    // Validar regras específicas para jogos de campeonato
    if (!jogoDTO.isJogoAmigavel() && jogoDTO.getCampeonatoId() == null) {
      throw new IllegalArgumentException("Jogos não amigáveis devem pertencer a um campeonato");
    }

    // 2. Validações de existência (dependem da BD)
    if (!equipaService.existsById(jogoDTO.getEquipaCasaId())
        || !equipaService.existsById(jogoDTO.getEquipaVisitanteId())) {
      throw new IllegalArgumentException("Equipe não encontrada");
    }

    // Validar se o árbitro principal existe
    if (!arbitroService.existsById(jogoDTO.getArbitroPrincipalId())) {
      throw new IllegalArgumentException("Árbitro principal não encontrado");
    }

    // Validar árbitros auxiliares se houver
    if (jogoDTO.getArbitrosAuxiliaresIds() != null) {
      for (Long arbitroId : jogoDTO.getArbitrosAuxiliaresIds()) {
        if (!arbitroService.existsById(arbitroId)) {
          throw new IllegalArgumentException("Árbitro auxiliar não encontrado");
        }
      }
    }
  }

  private boolean contemJogador(Long[] jogadores, Long jogadorId) {
    for (Long id : jogadores) {
      if (id.equals(jogadorId)) {
        return true;
      }
    }
    return false;
  }

  private void atualizarCamposJogo(Jogo jogo, JogoDTO jogoDTO) {
    try {
      // Converter string de data/hora para LocalDateTime
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
      jogo.setDataHora(LocalDateTime.parse(jogoDTO.getDataHora(), formatter));
    } catch (Exception e) {
      throw new IllegalArgumentException("Formato de data/hora inválido. Use: yyyy-MM-dd HH:mm");
    }

    jogo.setLocal(jogoDTO.getLocal());

    // Definir estado inicial
    jogo.setEstado(EstadoJogo.AGENDADO);

    // Buscar e definir as equipes
    Equipa equipaCasa =
        equipaRepository
            .findById(jogoDTO.getEquipaCasaId())
            .orElseThrow(() -> new IllegalArgumentException("Equipe casa não encontrada"));
    Equipa equipaFora =
        equipaRepository
            .findById(jogoDTO.getEquipaVisitanteId())
            .orElseThrow(() -> new IllegalArgumentException("Equipe visitante não encontrada"));

    jogo.setEquipaCasa(equipaCasa);
    jogo.setEquipaFora(equipaFora);

    // Buscar e definir os jogadores titulares
    Set<Jogador> titularesCasa = new HashSet<>();
    for (Long jogadorId : jogoDTO.getJogadoresCasaIds()) {
      Jogador jogador =
          jogadorRepository
              .findById(jogadorId)
              .orElseThrow(
                  () -> new IllegalArgumentException("Jogador não encontrado: " + jogadorId));
      titularesCasa.add(jogador);
    }
    jogo.setTitularesCasa(titularesCasa);

    Set<Jogador> titularesFora = new HashSet<>();
    for (Long jogadorId : jogoDTO.getJogadoresVisitanteIds()) {
      Jogador jogador =
          jogadorRepository
              .findById(jogadorId)
              .orElseThrow(
                  () -> new IllegalArgumentException("Jogador não encontrado: " + jogadorId));
      titularesFora.add(jogador);
    }
    jogo.setTitularesFora(titularesFora);

    // Buscar e definir os guarda-redes
    Jogador guardaRedesCasa =
        jogadorRepository
            .findById(jogoDTO.getGoleiroCasaId())
            .orElseThrow(() -> new IllegalArgumentException("Guarda-redes casa não encontrado"));
    Jogador guardaRedesFora =
        jogadorRepository
            .findById(jogoDTO.getGoleiroVisitanteId())
            .orElseThrow(
                () -> new IllegalArgumentException("Guarda-redes visitante não encontrado"));

    jogo.setGuardaRedesCasa(guardaRedesCasa);
    jogo.setGuardaRedesFora(guardaRedesFora);

    // Buscar e definir os árbitros
    Set<Arbitro> arbitros = new HashSet<>();

    // Adicionar árbitro principal
    Arbitro arbitroPrincipal =
        arbitroRepository
            .findById(jogoDTO.getArbitroPrincipalId())
            .orElseThrow(() -> new IllegalArgumentException("Árbitro principal não encontrado"));
    arbitros.add(arbitroPrincipal);
    jogo.setArbitroPrincipal(arbitroPrincipal);

    // Adicionar árbitros auxiliares se houver
    if (jogoDTO.getArbitrosAuxiliaresIds() != null
        && jogoDTO.getArbitrosAuxiliaresIds().length > 0) {
      for (Long arbitroId : jogoDTO.getArbitrosAuxiliaresIds()) {
        Arbitro arbitro =
            arbitroRepository
                .findById(arbitroId)
                .orElseThrow(
                    () ->
                        new IllegalArgumentException(
                            "Árbitro auxiliar não encontrado: " + arbitroId));
        arbitros.add(arbitro);
      }
    }
    jogo.setArbitros(arbitros);

    // Definir campeonato se não for jogo amigável
    if (!jogoDTO.isJogoAmigavel() && jogoDTO.getCampeonatoId() != null) {
      Campeonato campeonato =
          campeonatoRepository
              .findById(jogoDTO.getCampeonatoId())
              .orElseThrow(() -> new IllegalArgumentException("Campeonato não encontrado"));
      jogo.setCampeonato(campeonato);
    }
  }

  private JogoDTO converterParaDTO(Jogo jogo) {
    JogoDTO dto = new JogoDTO();

    dto.setId(jogo.getId());
    dto.setDataHora(jogo.getDataHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    dto.setLocal(jogo.getLocal());
    dto.setJogoAmigavel(jogo.isJogoAmigavel());
    dto.setEstado(jogo.getEstado().name());
    dto.setGolosCasa(jogo.getGolosCasa());
    dto.setGolosFora(jogo.getGolosFora());

    // IDs das equipes (mapeando de equipaFora para equipaVisitante no DTO)
    dto.setEquipaCasaId(jogo.getEquipaCasa().getId());
    dto.setEquipaVisitanteId(jogo.getEquipaFora().getId());

    // IDs dos jogadores (mapeando de titulares para jogadores no DTO)
    Long[] jogadoresCasaIds =
        jogo.getTitularesCasa().stream().map(Jogador::getId).toArray(Long[]::new);
    dto.setJogadoresCasaIds(jogadoresCasaIds);

    Long[] jogadoresVisitanteIds =
        jogo.getTitularesFora().stream().map(Jogador::getId).toArray(Long[]::new);
    dto.setJogadoresVisitanteIds(jogadoresVisitanteIds);

    // IDs dos guarda-redes (mapeando de guardaRedes para goleiro no DTO)
    dto.setGoleiroCasaId(jogo.getGuardaRedesCasa() != null ? jogo.getGuardaRedesCasa().getId() : null);
    dto.setGoleiroVisitanteId(jogo.getGuardaRedesFora() != null ? jogo.getGuardaRedesFora().getId() : null);
    // ID do árbitro principal
    dto.setArbitroPrincipalId(jogo.getArbitroPrincipal().getId());

    // IDs dos árbitros auxiliares (excluindo o principal)
    Set<Arbitro> arbitrosAuxiliares = new HashSet<>(jogo.getArbitros());
    arbitrosAuxiliares.remove(jogo.getArbitroPrincipal());

    if (!arbitrosAuxiliares.isEmpty()) {
      Long[] arbitrosAuxiliaresIds =
          arbitrosAuxiliares.stream().map(Arbitro::getId).toArray(Long[]::new);
      dto.setArbitrosAuxiliaresIds(arbitrosAuxiliaresIds);
    }

    // ID do campeonato se houver
    if (jogo.getCampeonato() != null) {
      dto.setCampeonatoId(jogo.getCampeonato().getId());
    }

    return dto;
  }

  public void removerJogo(Long id) {
    jogoRepository.deleteById(id);
  }

  public void registarResultado(Long id, int golosCasa, int golosFora) {
    Jogo jogo = jogoRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado"));
    if (!jogo.podeRegistarResultado()) {
        throw new IllegalStateException("Não é possível registar resultado neste estado.");
    }
    jogo.definirResultado(golosCasa, golosFora);
    jogoRepository.save(jogo);
  }

  public void alterarEstado(Long id, String novoEstado, String novaDataHora) {
    Jogo jogo = jogoRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Jogo não encontrado"));

    if (novoEstado != null && !novoEstado.isBlank()) {
        jogo.setEstado(EstadoJogo.valueOf(novoEstado));
    }
    if (novaDataHora != null && !novaDataHora.isBlank()) {
        jogo.setDataHora(LocalDateTime.parse(novaDataHora, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
    jogoRepository.save(jogo);
  }

  public boolean cancelarJogo(Long id) {
    Optional<Jogo> opt = jogoRepository.findById(id);
    if (opt.isEmpty()) return false;
    Jogo jogo = opt.get();
    jogo.setEstado(EstadoJogo.CANCELADO);
    jogoRepository.save(jogo);
    return true;
}
}
