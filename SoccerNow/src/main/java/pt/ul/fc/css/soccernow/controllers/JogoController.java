package pt.ul.fc.css.soccernow.controllers;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ul.fc.css.soccernow.dtos.JogoDTO;
import pt.ul.fc.css.soccernow.services.JogoService;

@RestController
@RequestMapping("/api/jogos")
public class JogoController {

  @Autowired private JogoService jogoService;

  @PostMapping
  @Operation(
      summary = "Criar um jogo",
      description = "Cria um novo jogo com as informações fornecidas.")
  public ResponseEntity<?> criarJogo(@RequestBody JogoDTO jogoDTO) {
    try {
      JogoDTO novoJogo = jogoService.criarJogo(jogoDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(novoJogo);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Erro interno do servidor: " + e.getMessage());
    }
  }

  @GetMapping
  @Operation(
      summary = "Listar todos os jogos",
      description = "Retorna uma lista com todos os jogos cadastrados.")
  public ResponseEntity<List<JogoDTO>> listarJogos() {
    List<JogoDTO> jogos = jogoService.listarJogos();
    return ResponseEntity.ok(jogos);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Buscar jogo por ID",
      description = "Retorna os detalhes de um jogo específico.")
  public ResponseEntity<JogoDTO> buscarJogoPorId(@PathVariable Long id) {
    return jogoService
        .buscarJogoPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
    public ResponseEntity<?> atualizarJogo(@PathVariable Long id, @RequestBody JogoDTO jogoDTO) {
        try {
            Optional<JogoDTO> jogoExistente = jogoService.buscarJogoPorId(id);
            if (jogoExistente.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            JogoDTO atualizado = jogoService.criarJogo(jogoDTO);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerJogo(@PathVariable Long id) {
        if (jogoService.buscarJogoPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        jogoService.removerJogo(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/resultado")
    @Operation(
        summary = "Registar resultado do jogo",
        description = "Regista o resultado (golos) de um jogo, se permitido pelo estado."
    )
    public ResponseEntity<?> registarResultado(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body) {
        try {
            int golosCasa = body.get("golosCasa");
            int golosFora = body.get("golosFora");
            jogoService.registarResultado(id, golosCasa, golosFora);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Não é possível registar resultado neste estado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
  }

  @PutMapping("/{id}/estado")
  public ResponseEntity<?> alterarEstado(
      @PathVariable Long id,
      @RequestBody Map<String, String> body
  ) {
      String novoEstado = body.get("estado");
      String novaDataHora = body.get("dataHora");
      jogoService.alterarEstado(id, novoEstado, novaDataHora);
      return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/cancelar")
  public ResponseEntity<Void> cancelarJogo(@PathVariable Long id) {
      boolean cancelado = jogoService.cancelarJogo(id);
      if (!cancelado) return ResponseEntity.notFound().build();
      return ResponseEntity.noContent().build();
  }

}
