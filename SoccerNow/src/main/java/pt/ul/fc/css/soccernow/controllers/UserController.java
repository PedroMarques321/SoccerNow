package pt.ul.fc.css.soccernow.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ul.fc.css.soccernow.dtos.ArbitroDTO;
import pt.ul.fc.css.soccernow.dtos.JogadorDTO;
import pt.ul.fc.css.soccernow.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired private UserService userService;

  @PostMapping("/jogadores")
  @Operation(
      summary = "Register a new player",
      description = "Registers a new player and returns the created player DTO.")
  public ResponseEntity<JogadorDTO> registerJogador(@Valid @RequestBody JogadorDTO jogadorDTO) {
    JogadorDTO responseDTO = (JogadorDTO) userService.registerUser("jogador", jogadorDTO);
    return ResponseEntity.ok(responseDTO);
  }

  @PostMapping("/arbitros")
  @Operation(
      summary = "Register a new referee",
      description = "Registers a new referee and returns the created referee DTO.")
  public ResponseEntity<ArbitroDTO> registerArbitro(@Valid @RequestBody ArbitroDTO arbitroDTO) {
    ArbitroDTO responseDTO = (ArbitroDTO) userService.registerUser("arbitro", arbitroDTO);
    return ResponseEntity.ok(responseDTO);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a user by id", description = "Returns a user given its id.")
  public ResponseEntity<?> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @PutMapping("/jogadores/{id}")
  public ResponseEntity<JogadorDTO> updateJogador(
      @PathVariable Long id, @Valid @RequestBody JogadorDTO jogadorDTO) {
    JogadorDTO responseDTO = (JogadorDTO) userService.updateUser(id, "jogador", jogadorDTO);
    return ResponseEntity.ok(responseDTO);
  }

  @PutMapping("/arbitros/{id}")
  public ResponseEntity<ArbitroDTO> updateArbitro(
      @PathVariable Long id, @Valid @RequestBody ArbitroDTO arbitroDTO) {
    ArbitroDTO responseDTO = (ArbitroDTO) userService.updateUser(id, "arbitro", arbitroDTO);
    return ResponseEntity.ok(responseDTO);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
