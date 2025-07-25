package pt.ul.fc.css.soccernow.services;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.dtos.ArbitroDTO;
import pt.ul.fc.css.soccernow.mapper.Mapper;
import pt.ul.fc.css.soccernow.models.Arbitro;
import pt.ul.fc.css.soccernow.repositories.ArbitroRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArbitroService {

  @Autowired private ArbitroRepository arbitroRepository;

  public ArbitroDTO registerArbitro(ArbitroDTO arbitroDTO) {
    validateArbitroData(arbitroDTO);

    // Converter DTO para Entity
    Arbitro arbitro = Mapper.mapDtoToArbitro(arbitroDTO);

    // Guardar na BD
    Arbitro savedArbitro = arbitroRepository.save(arbitro);

    // Converter Entity para DTO
    return Mapper.mapToArbitroDTO(savedArbitro);
  }

  public ArbitroDTO updateArbitro(Long id, ArbitroDTO arbitroDTO) {
    Arbitro existingArbitro =
        arbitroRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Árbitro não encontrado"));

    updateArbitroFields(existingArbitro, arbitroDTO);

    Arbitro savedArbitro = arbitroRepository.save(existingArbitro);
    return Mapper.mapToArbitroDTO(savedArbitro);
  }

  private void validateArbitroData(ArbitroDTO dto) {
    if (dto.getCertificado() != true && dto.getCertificado() != false) {
      throw new IllegalArgumentException("Estado de certificação é obrigatório");
    }
  }

  private void updateArbitroFields(Arbitro arbitro, ArbitroDTO dto) {
    if (dto.getUsername() != null) arbitro.setUsername(dto.getUsername());
    if (dto.getEmail() != null) arbitro.setEmail(dto.getEmail());
    if (dto.getCertificado() == true || dto.getCertificado() == false)
      arbitro.setCertificado(dto.getCertificado());
  }

  public Optional<ArbitroDTO> findByIdDTO(Long id) {
    return arbitroRepository.findById(id).map(Mapper::mapToArbitroDTO);
  }

  public boolean existsById(Long id) {
    return arbitroRepository.existsById(id);
  }

  public void deleteById(Long id) {
    if (!arbitroRepository.existsById(id)) {
      throw new EntityNotFoundException("Árbitro não encontrado");
    }
    arbitroRepository.deleteById(id);
  }

  public List<ArbitroDTO> listarArbitros() {
    return arbitroRepository.findAll()
            .stream()
            .map(Mapper::mapToArbitroDTO)
            .collect(Collectors.toList());
  }
}
