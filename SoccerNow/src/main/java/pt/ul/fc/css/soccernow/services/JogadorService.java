package pt.ul.fc.css.soccernow.services;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.dtos.JogadorDTO;
import pt.ul.fc.css.soccernow.mapper.Mapper;
import pt.ul.fc.css.soccernow.models.Jogador;
import pt.ul.fc.css.soccernow.repositories.JogadorRepository;

@Service
public class JogadorService {

  @Autowired private JogadorRepository jogadorRepository;

  public JogadorDTO registerJogador(JogadorDTO jogadorDTO) {
    // Validações de negócio
    validateJogadorData(jogadorDTO);

    // Converter DTO para Entity
    Jogador jogador = Mapper.mapDtoToJogador(jogadorDTO);

    // Guardar na BD
    Jogador savedJogador = jogadorRepository.save(jogador);

    // Converter Entity para DTO
    return Mapper.mapToJogadorDTO(savedJogador);
  }

  public List<JogadorDTO> getAllJogadores() {
    return jogadorRepository.findAll().stream()
        .map(
            j ->
                new JogadorDTO(
                    j.getId(),
                    j.getUsername(),
                    j.getEmail(),
                    j.getPassword(),
                    j.getPosicao(),
                    j.getNumeroCamisa()))
        .collect(Collectors.toList());
  }

  public Optional<JogadorDTO> getJogadorById(Long id) {
    if (id != null) {
      return jogadorRepository.findById(id).map(Mapper::mapToJogadorDTO);
    }
    throw new IllegalArgumentException("ID não pode ser nulo");
  }

  public JogadorDTO updateJogador(Long id, JogadorDTO jogadorDTO) {
    Jogador existingJogador =
        jogadorRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Jogador não encontrado"));

    // Validações específicas para o update
    validateJogadorUpdate(jogadorDTO, id);

    // Atualizar os campos do jogador existente
    updateJogadorFields(existingJogador, jogadorDTO);

    Jogador savedJogador = jogadorRepository.save(existingJogador);

    return Mapper.mapToJogadorDTO(savedJogador);
  }

  // Usado para ao registar novo jogador verificar se o número de camisa já existe
  private void validateJogadorData(JogadorDTO dto) {
    if (dto.getNumeroCamisa() != null
        && jogadorRepository.existsByNumeroCamisa(dto.getNumeroCamisa())) {
      throw new IllegalArgumentException("Número de camisa já existe");
    }
  }

  // Usado para ao atualizar um jogador verificar se o número de camisa já existe
  private void validateJogadorUpdate(JogadorDTO dto, Long id) {
    if (dto.getNumeroCamisa() != null
        && jogadorRepository.existsByNumeroCamisaAndIdNot(dto.getNumeroCamisa(), id)) {
      throw new IllegalArgumentException("Número de camisa já existe");
    }
  }

  private void updateJogadorFields(Jogador jogador, JogadorDTO dto) {
    if (dto.getUsername() != null) jogador.setUsername(dto.getUsername());
    if (dto.getEmail() != null) jogador.setEmail(dto.getEmail());
    if (dto.getPassword() != null) jogador.setPassword(dto.getPassword());
    if (dto.getPosicao() != null) jogador.setPosicao(dto.getPosicao());
    if (dto.getNumeroCamisa() != null) jogador.setNumeroCamisa(dto.getNumeroCamisa());
  }

  public Optional<JogadorDTO> findByIdDTO(Long id) {
    return jogadorRepository.findById(id).map(Mapper::mapToJogadorDTO);
  }

  public List<JogadorDTO> findAllDTO() {
    return jogadorRepository.findAll().stream()
        .map(Mapper::mapToJogadorDTO)
        .collect(Collectors.toList());
  }

  public boolean existsById(Long id) {
    return jogadorRepository.existsById(id);
  }

  public void deleteById(Long id) {
    if (!jogadorRepository.existsById(id)) {
      throw new EntityNotFoundException("Jogador não encontrado");
    }
    jogadorRepository.deleteById(id);
  }
}
