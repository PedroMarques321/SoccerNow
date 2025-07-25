package pt.ul.fc.css.soccernow.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import pt.ul.fc.css.soccernow.dtos.EstatisticasDTO;
import pt.ul.fc.css.soccernow.mapper.Mapper;
import pt.ul.fc.css.soccernow.models.Estatisticas;
import pt.ul.fc.css.soccernow.models.Jogador;
import pt.ul.fc.css.soccernow.models.Jogo;
import pt.ul.fc.css.soccernow.repositories.EstatisticasRepository;
import pt.ul.fc.css.soccernow.repositories.JogadorRepository;
import pt.ul.fc.css.soccernow.repositories.JogoRepository;

@Service
public class EstatisticasService {

  @Autowired private EstatisticasRepository estatisticasRepository;

  @Autowired private JogadorRepository jogadorRepository;

  @Autowired private JogoRepository jogoRepository;

  public EstatisticasDTO registrarEstatisticas(EstatisticasDTO estatisticasDTO) {
    // Validações de negócio
    validateEstatisticasData(estatisticasDTO);

    // Converter DTO para Entity
    Estatisticas estatisticas = Mapper.mapDtoToEstatisticas(estatisticasDTO);

    // Buscar e associar o jogador
    if (estatisticasDTO.getJogadorId() != null) {
      Jogador jogador = jogadorRepository
          .findById(estatisticasDTO.getJogadorId())
          .orElseThrow(() -> new EntityNotFoundException("Jogador não encontrado"));
      estatisticas.setJogador(jogador);
    }

    // Buscar e associar o jogo
    if (estatisticasDTO.getJogoId() != null) {
      Jogo jogo = jogoRepository
          .findById(estatisticasDTO.getJogoId())
          .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado"));
      estatisticas.setJogo(jogo);
    }

    // Guardar na BD
    Estatisticas savedEstatisticas = estatisticasRepository.save(estatisticas);

    // Converter Entity para DTO
    return Mapper.mapToEstatisticasDTO(savedEstatisticas);
  }

  public List<EstatisticasDTO> getAllEstatisticas() {
    return estatisticasRepository.findAll().stream()
        .map(Mapper::mapToEstatisticasDTO)
        .collect(Collectors.toList());
  }

  public Optional<EstatisticasDTO> getEstatisticasById(Long id) {
    if (id != null) {
      return estatisticasRepository.findById(id).map(Mapper::mapToEstatisticasDTO);
    }
    throw new IllegalArgumentException("ID não pode ser nulo");
  }

  public List<EstatisticasDTO> getEstatisticasByJogadorId(Long jogadorId) {
    if (jogadorId == null) {
      throw new IllegalArgumentException("ID do jogador não pode ser nulo");
    }
    return estatisticasRepository.findByJogadorId(jogadorId).stream()
        .map(Mapper::mapToEstatisticasDTO)
        .collect(Collectors.toList());
  }

  public List<EstatisticasDTO> getEstatisticasByJogoId(Long jogoId) {
    if (jogoId == null) {
      throw new IllegalArgumentException("ID do jogo não pode ser nulo");
    }
    return estatisticasRepository.findByJogoId(jogoId).stream()
        .map(Mapper::mapToEstatisticasDTO)
        .collect(Collectors.toList());
  }

  public List<EstatisticasDTO> getEstatisticasByData(LocalDate data) {
    if (data == null) {
      throw new IllegalArgumentException("Data não pode ser nula");
    }
    return estatisticasRepository.findByData(data).stream()
        .map(Mapper::mapToEstatisticasDTO)
        .collect(Collectors.toList());
  }

  public List<EstatisticasDTO> getEstatisticasByPeriodo(LocalDate dataInicio, LocalDate dataFim) {
    if (dataInicio == null || dataFim == null) {
      throw new IllegalArgumentException("Datas de início e fim não podem ser nulas");
    }
    if (dataInicio.isAfter(dataFim)) {
      throw new IllegalArgumentException("Data de início deve ser anterior à data de fim");
    }
    return estatisticasRepository.findByDataBetween(dataInicio, dataFim).stream()
        .map(Mapper::mapToEstatisticasDTO)
        .collect(Collectors.toList());
  }

  public List<EstatisticasDTO> getEstatisticasByEquipaId(Long equipaId) {
    if (equipaId == null) {
      throw new IllegalArgumentException("ID da equipa não pode ser nulo");
    }
    return estatisticasRepository.findByEquipaId(equipaId).stream()
        .map(Mapper::mapToEstatisticasDTO)
        .collect(Collectors.toList());
  }

  public List<EstatisticasDTO> getEstatisticasComGolos() {
    return estatisticasRepository.findEstatisticasComGolos().stream()
        .map(Mapper::mapToEstatisticasDTO)
        .collect(Collectors.toList());
  }

  public List<EstatisticasDTO> getEstatisticasComCartoes() {
    return estatisticasRepository.findEstatisticasComCartoes().stream()
        .map(Mapper::mapToEstatisticasDTO)
        .collect(Collectors.toList());
  }

  public EstatisticasDTO updateEstatisticas(Long id, EstatisticasDTO estatisticasDTO) {
    Estatisticas existingEstatisticas = estatisticasRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Estatísticas não encontradas"));

    // Validações específicas para update
    validateEstatisticasUpdate(estatisticasDTO, id);

    // Atualizar os campos
    updateEstatisticasFields(existingEstatisticas, estatisticasDTO);

    // Atualizar jogador se fornecido
    if (estatisticasDTO.getJogadorId() != null) {
      Jogador jogador = jogadorRepository
          .findById(estatisticasDTO.getJogadorId())
          .orElseThrow(() -> new EntityNotFoundException("Jogador não encontrado"));
      existingEstatisticas.setJogador(jogador);
    }

    // Atualizar jogo se fornecido
    if (estatisticasDTO.getJogoId() != null) {
      Jogo jogo = jogoRepository
          .findById(estatisticasDTO.getJogoId())
          .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado"));
      existingEstatisticas.setJogo(jogo);
    }

    Estatisticas savedEstatisticas = estatisticasRepository.save(existingEstatisticas);
    return Mapper.mapToEstatisticasDTO(savedEstatisticas);
  }

  public void deleteById(Long id) {
    if (!estatisticasRepository.existsById(id)) {
      throw new EntityNotFoundException("Estatísticas não encontradas");
    }
    estatisticasRepository.deleteById(id);
  }

  // Métodos de validação privados
  private void validateEstatisticasData(EstatisticasDTO dto) {
    if (dto.getJogadorId() == null) {
      throw new IllegalArgumentException("Jogador é obrigatório");
    }
    if (dto.getJogoId() == null) {
      throw new IllegalArgumentException("Jogo é obrigatório");
    }

    // Verificar se já existem estatísticas para este jogador neste jogo
    if (estatisticasRepository.existsByJogadorIdAndJogoId(dto.getJogadorId(), dto.getJogoId())) {
      throw new IllegalArgumentException("Já existem estatísticas para este jogador neste jogo");
    }

    validateEstatisticasValues(dto);
  }

  private void validateEstatisticasUpdate(EstatisticasDTO dto, Long id) {
    // Verificar se já existem estatísticas para outro registro neste jogo (excluindo o atual)
    if (dto.getJogadorId() != null && dto.getJogoId() != null) {
      Estatisticas existing = estatisticasRepository.findById(id).orElse(null);
      if (existing != null && 
          (!existing.getJogador().getId().equals(dto.getJogadorId()) || 
           !existing.getJogo().getId().equals(dto.getJogoId()))) {
        if (estatisticasRepository.existsByJogadorIdAndJogoId(dto.getJogadorId(), dto.getJogoId())) {
          throw new IllegalArgumentException("Já existem estatísticas para este jogador neste jogo");
        }
      }
    }

    validateEstatisticasValues(dto);
  }

  private void validateEstatisticasValues(EstatisticasDTO dto) {
    // Validar valores não negativos
    if (dto.getGolos() != null && dto.getGolos() < 0) {
      throw new IllegalArgumentException("Golos não pode ser negativo");
    }
    if (dto.getCartoesAmarelos() != null && dto.getCartoesAmarelos() < 0) {
      throw new IllegalArgumentException("Cartões amarelos não pode ser negativo");
    }
    if (dto.getCartoesVermelhos() != null && dto.getCartoesVermelhos() < 0) {
      throw new IllegalArgumentException("Cartões vermelhos não pode ser negativo");
    }

    // Validar limites dos cartões conforme regras do futebol
    if (dto.getCartoesAmarelos() != null && dto.getCartoesAmarelos() > 2) {
      throw new IllegalArgumentException("Um jogador pode receber no máximo 2 cartões amarelos num jogo");
    }
    if (dto.getCartoesVermelhos() != null && dto.getCartoesVermelhos() > 1) {
      throw new IllegalArgumentException("Um jogador pode receber no máximo 1 cartão vermelho num jogo");
    }

    // Validação adicional: se tem cartão vermelho, não pode ter mais de 1 cartão amarelo
    // (porque o segundo amarelo resulta em vermelho, não em amarelo + vermelho)
    if (dto.getCartoesVermelhos() != null && dto.getCartoesVermelhos() == 1 && 
        dto.getCartoesAmarelos() != null && dto.getCartoesAmarelos() > 1) {
      throw new IllegalArgumentException("Se um jogador recebe cartão vermelho, não pode ter mais de 1 cartão amarelo no mesmo jogo");
    }
  }

  private void updateEstatisticasFields(Estatisticas estatisticas, EstatisticasDTO dto) {
    if (dto.getGolos() != null) estatisticas.setGolos(dto.getGolos());
    if (dto.getCartoesAmarelos() != null) estatisticas.setCartoesAmarelos(dto.getCartoesAmarelos());
    if (dto.getCartoesVermelhos() != null) estatisticas.setCartoesVermelhos(dto.getCartoesVermelhos());
  }
} 