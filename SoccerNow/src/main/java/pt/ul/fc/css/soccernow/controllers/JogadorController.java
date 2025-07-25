package pt.ul.fc.css.soccernow.controllers;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
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
import pt.ul.fc.css.soccernow.dtos.JogadorDTO;
import pt.ul.fc.css.soccernow.services.JogadorService;

@RestController
@RequestMapping("/api/jogadores")
public class JogadorController {

  @Autowired private JogadorService jogadorService;

  @GetMapping
  @Operation(summary = "Get all jogadores", description = "Returns a list of all jogadores.")
  public ResponseEntity<List<JogadorDTO>> getAllJogadores() {
    List<JogadorDTO> jogadores = jogadorService.getAllJogadores();
    return ResponseEntity.ok(jogadores);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a jogador by id", description = "Returns a jogador given its id.")
  public ResponseEntity<JogadorDTO> getJogadorById(@PathVariable Long id) {
    try {
      return jogadorService
          .getJogadorById(id)
          .map(ResponseEntity::ok)
          .orElseGet(() -> ResponseEntity.notFound().build());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new JogadorDTO());
    }
  }

  @PostMapping
  @Operation(
      summary = "Create a jogador",
      description = "Creates a new jogador and returns the created jogador DTO.")
  public ResponseEntity<JogadorDTO> createJogador(@RequestBody JogadorDTO jogadorDTO) {
    JogadorDTO responseDTO = jogadorService.registerJogador(jogadorDTO);
    return ResponseEntity.ok(responseDTO);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update a jogador",
      description = "Updates an existing jogador and returns the updated jogador DTO.")
  public ResponseEntity<JogadorDTO> updateJogador(
      @PathVariable Long id, @RequestBody JogadorDTO jogadorDTO) {
    JogadorDTO responseDTO = jogadorService.updateJogador(id, jogadorDTO);
    return ResponseEntity.ok(responseDTO);
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete a jogador",
      description = "Deletes an existing jogador.")
  public ResponseEntity<Void> deleteJogador(@PathVariable Long id) {
      jogadorService.deleteById(id);
      return ResponseEntity.noContent().build();
  }
}
