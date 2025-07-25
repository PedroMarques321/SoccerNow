package pt.ul.fc.css.soccernow.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import pt.ul.fc.css.soccernow.dtos.EquipaDTO;
import pt.ul.fc.css.soccernow.dtos.JogadorDTO;
import pt.ul.fc.css.soccernow.mapper.Mapper;
import pt.ul.fc.css.soccernow.models.Equipa;
import pt.ul.fc.css.soccernow.models.Jogador;
import pt.ul.fc.css.soccernow.repositories.EquipaRepository;
import pt.ul.fc.css.soccernow.repositories.JogadorRepository;

@Service
public class EquipaService {

  @Autowired
  private EquipaRepository equipaRepository;

  @Autowired
  private JogadorService jogadorService;

  @Autowired
  private JogadorRepository jogadorRepository;

  public EquipaDTO registerEquipa(EquipaDTO equipaDTO) {
    // Validações de negócio
    validateEquipaData(equipaDTO);

    // Converter DTO para Entity
    Equipa equipa = Mapper.mapDtoToEquipa(equipaDTO);

    // Guardar na BD
    Equipa savedEquipa = equipaRepository.save(equipa);

    // Converter Entity para DTO
    return Mapper.mapToEquipaDTO(savedEquipa);
  }

  public List<EquipaDTO> getAllEquipas() {
    return equipaRepository.findAll().stream()
        .map(Mapper::mapToEquipaDTO)
        .collect(Collectors.toList());
  }

  public Optional<EquipaDTO> getEquipaById(Long id) {
    if (id != null) {
      return equipaRepository.findById(id).map(Mapper::mapToEquipaDTO);
    }
    throw new IllegalArgumentException("ID não pode ser nulo");
  }

  public EquipaDTO updateEquipa(Long id, EquipaDTO equipaDTO) {
    Equipa existingEquipa =
        equipaRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Equipa não encontrada"));

    // Validações específicas para o update
    validateEquipaUpdate(equipaDTO, id);

    // Atualizar os campos da equipa existente
    updateEquipaFields(existingEquipa, equipaDTO);

    Equipa savedEquipa = equipaRepository.save(existingEquipa);

    return Mapper.mapToEquipaDTO(savedEquipa);
  }

  public void deleteById(Long id) {
    if (!equipaRepository.existsById(id)) {
      throw new EntityNotFoundException("Equipa não encontrada");
    }
    equipaRepository.deleteById(id);
  }

  public EquipaDTO addJogadorToEquipa(Long equipaId, Long jogadorId) {
    
    Optional<Equipa> equipaOptional = equipaRepository.findById(equipaId);
    if (equipaOptional.isEmpty()) {
      throw new EntityNotFoundException("Equipa não encontrada");
    }
    // Obter a equipa e o jogador
    Equipa equipa = equipaOptional.get();
    Optional<JogadorDTO> jogadorOptional = jogadorService.getJogadorById(jogadorId);
    
    if (jogadorOptional.isEmpty()) {
      throw new EntityNotFoundException("Jogador não encontrado");
    }
    Jogador jogador = jogadorRepository.findById(jogadorId)
        .orElseThrow(() -> new EntityNotFoundException("Jogador não encontrado"));
    
    // Validações
    validadeJogadorAdd(equipa, jogador);

    // Adicionar o jogador à equipa
    equipa.addJogador(jogador);
    // Atualizar a equipa na base de dados
    Equipa updatedEquipa = equipaRepository.save(equipa);
    
    return Mapper.mapToEquipaDTO(updatedEquipa);
  }

  // Validações
  private void validateEquipaData(EquipaDTO dto) {
    if (dto.getNome() != null && equipaRepository.existsByNome(dto.getNome())) {
      throw new IllegalArgumentException("Nome da equipa já existe");
    }
  }

  private void validateEquipaUpdate(EquipaDTO dto, Long id) {
    if (dto.getNome() != null && equipaRepository.existsByNomeAndIdNot(dto.getNome(), id)) {
      throw new IllegalArgumentException("Nome da equipa já existe");
    }
  }

  private void updateEquipaFields(Equipa equipa, EquipaDTO dto) {
    if (dto.getNome() != null) equipa.setNome(dto.getNome());
    if (dto.getCidade() != null) equipa.setCidade(dto.getCidade());
    if (dto.getHistoricoJogos() != null) equipa.setHistoricoJogos(dto.getHistoricoJogos());
    if (dto.getConquistas() != null) equipa.setConquistas(dto.getConquistas());
  }

  private void validadeJogadorAdd(Equipa equipa, Jogador jogador) {
    if (equipa.getJogadores().contains(jogador)) {
      throw new IllegalArgumentException("Jogador já pertence à equipa");
    }
  }
  // Métodos auxiliares (manter compatibilidade)
  public List<Equipa> findAll() {
    return equipaRepository.findAll();
  }

  public Optional<Equipa> findById(Long id) {
    return equipaRepository.findById(id);
  }

  public Equipa save(Equipa equipa) {
    return equipaRepository.save(equipa);
  }

  public boolean existsById(Long id) {
    return equipaRepository.existsById(id);
  }

  public EquipaDTO atualizarJogadoresDaEquipa(Long equipaId, List<Long> jogadoresIds) {
    Equipa equipa = equipaRepository.findById(equipaId)
        .orElseThrow(() -> new EntityNotFoundException("Equipa não encontrada"));

    // Limpa todos os jogadores atuais
    equipa.getJogadores().clear();

    // Adiciona os novos jogadores
    for (Long jogadorId : jogadoresIds) {
        Jogador jogador = jogadorRepository.findById(jogadorId)
            .orElseThrow(() -> new EntityNotFoundException("Jogador não encontrado: " + jogadorId));
        equipa.getJogadores().add(jogador);
    }

    equipaRepository.save(equipa);
    return Mapper.mapToEquipaDTO(equipa);
  }
  
  public List<JogadorDTO> getJogadoresDaEquipa(Long equipaId) {
    Equipa equipa = equipaRepository.findById(equipaId)
        .orElseThrow(() -> new EntityNotFoundException("Equipa não encontrada"));
    return equipa.getJogadores()
        .stream()
        .map(Mapper::mapToJogadorDTO)
        .collect(Collectors.toList());
  }
}
