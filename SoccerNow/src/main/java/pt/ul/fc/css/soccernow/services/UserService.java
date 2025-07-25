package pt.ul.fc.css.soccernow.services;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.dtos.ArbitroDTO;
import pt.ul.fc.css.soccernow.dtos.JogadorDTO;

@Service
public class UserService {

  @Autowired private JogadorService jogadorService;

  @Autowired private ArbitroService arbitroService;

  public Object registerUser(String userType, Object userDTO) {
    if (userType.equals("jogador")) {
      return jogadorService.registerJogador((JogadorDTO) userDTO);
    } else if (userType.equals("arbitro")) {
      return arbitroService.registerArbitro((ArbitroDTO) userDTO);
    }
    throw new IllegalArgumentException("Tipo de utilizador inválido!");
  }

  public Object updateUser(Long id, String userType, Object userDTO) {
    if (userType.equals("jogador")) {
      return jogadorService.updateJogador(id, (JogadorDTO) userDTO);
    } else if (userType.equals("arbitro")) {
      return arbitroService.updateArbitro(id, (ArbitroDTO) userDTO);
    }
    throw new IllegalArgumentException("Tipo de utilizador inválido!");
  }

  public Object getUserById(Long id) {
    Optional<JogadorDTO> jogador = jogadorService.findByIdDTO(id);
    if (jogador.isPresent()) {
      return jogador.get();
    }
    Optional<ArbitroDTO> arbitro = arbitroService.findByIdDTO(id);
    if (arbitro.isPresent()) {
      return arbitro.get();
    }
    throw new EntityNotFoundException("Utilizador não encontrado!");
  }

  public void deleteUser(Long id) {
    if (jogadorService.existsById(id)) {
      jogadorService.deleteById(id);
      return;
    }

    if (arbitroService.existsById(id)) {
      arbitroService.deleteById(id);
      return;
    }
    throw new EntityNotFoundException("Utilizador não encontrado!");
  }
}
