package pt.ul.fc.css.soccernow.controllers;

import java.util.List;
import java.util.Map;

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

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import pt.ul.fc.css.soccernow.dtos.EquipaDTO;
import pt.ul.fc.css.soccernow.dtos.JogadorDTO;
import pt.ul.fc.css.soccernow.services.EquipaService;

@RestController
@RequestMapping("/api/equipas")
public class EquipaController {

  @Autowired private EquipaService equipaService;

  @PostMapping
  public ResponseEntity<EquipaDTO> createEquipa(@RequestBody EquipaDTO equipaDTO) {
    try {
      EquipaDTO savedEquipa = equipaService.registerEquipa(equipaDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(savedEquipa);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping
  public ResponseEntity<List<EquipaDTO>> getAllEquipasDTO() {
    List<EquipaDTO> equipas = equipaService.getAllEquipas();
    return ResponseEntity.ok(equipas);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EquipaDTO> getEquipaDTOById(@PathVariable Long id) {
    return equipaService
        .getEquipaById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<EquipaDTO> updateEquipaDTO(
      @PathVariable Long id, @RequestBody EquipaDTO equipaDTO) {
    try {
      EquipaDTO updatedEquipa = equipaService.updateEquipa(id, equipaDTO);
      return ResponseEntity.ok(updatedEquipa);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/{equipaId}/{jogadorId}")
  public ResponseEntity<EquipaDTO> addJogadorToEquipa(
          @PathVariable Long equipaId, 
          @PathVariable Long jogadorId) {
      try {
          EquipaDTO updatedEquipa = equipaService.addJogadorToEquipa(equipaId, jogadorId);
          return ResponseEntity.ok(updatedEquipa);
      } catch (EntityNotFoundException e) {
          System.out.println("Entity not found: " + e.getMessage()); // Log temporário
          return ResponseEntity.notFound().build();
      } catch (IllegalArgumentException e) {
          System.out.println("Bad request: " + e.getMessage()); // Log temporário
          return ResponseEntity.badRequest().build();
      } catch (Exception e) {
          System.out.println("Unexpected error: " + e.getMessage()); // Log temporário
          e.printStackTrace(); // Mostra stack trace completo
          return ResponseEntity.internalServerError().build();
      }
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete an equipa",
      description = "Deletes an existing equipa.")
  public ResponseEntity<Void> deleteEquipa(@PathVariable Long id) {
    equipaService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{equipaId}/jogadores")
  @Operation(
      summary = "Update jogadores of an equipa",
      description = "Updates the jogadores of an existing equipa.")
  public ResponseEntity<EquipaDTO> atualizarJogadoresDaEquipa(
          @PathVariable Long equipaId,
          @RequestBody Map<String, List<Long>> body) {
      List<Long> jogadoresIds = body.get("jogadoresIds");
      EquipaDTO equipaAtualizada = equipaService.atualizarJogadoresDaEquipa(equipaId, jogadoresIds);
      return ResponseEntity.ok(equipaAtualizada);
  }

  @GetMapping("/{id}/jogadores")
public ResponseEntity<List<JogadorDTO>> getJogadoresDaEquipa(@PathVariable Long id) {
    List<JogadorDTO> jogadores = equipaService.getJogadoresDaEquipa(id);
    if (jogadores == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(jogadores);
}
}
