package pt.ul.fc.css.soccernow.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import pt.ul.fc.css.soccernow.dtos.EstatisticasDTO;
import pt.ul.fc.css.soccernow.services.EstatisticasService;

@RestController
@RequestMapping("/api/estatisticas")
public class EstatisticasController {

  @Autowired private EstatisticasService estatisticasService;

  @PostMapping
  @Operation(
      summary = "Registar estatísticas",
      description = "Regista novas estatísticas para um jogador num jogo específico.")
  public ResponseEntity<?> registrarEstatisticas(@RequestBody EstatisticasDTO estatisticasDTO) {
    try {
      EstatisticasDTO novasEstatisticas = estatisticasService.registrarEstatisticas(estatisticasDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(novasEstatisticas);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Erro interno do servidor: " + e.getMessage());
    }
  }

  @GetMapping
  @Operation(
      summary = "Listar todas as estatísticas",
      description = "Retorna uma lista com todas as estatísticas registadas.")
  public ResponseEntity<List<EstatisticasDTO>> getAllEstatisticas() {
    List<EstatisticasDTO> estatisticas = estatisticasService.getAllEstatisticas();
    return ResponseEntity.ok(estatisticas);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Buscar estatísticas por ID",
      description = "Retorna as estatísticas com o ID especificado.")
  public ResponseEntity<EstatisticasDTO> getEstatisticasById(@PathVariable Long id) {
    try {
      return estatisticasService
          .getEstatisticasById(id)
          .map(ResponseEntity::ok)
          .orElse(ResponseEntity.notFound().build());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/jogador/{jogadorId}")
  @Operation(
      summary = "Buscar estatísticas por jogador",
      description = "Retorna todas as estatísticas de um jogador específico.")
  public ResponseEntity<?> getEstatisticasByJogador(@PathVariable Long jogadorId) {
    try {
      List<EstatisticasDTO> estatisticas = estatisticasService.getEstatisticasByJogadorId(jogadorId);
      return ResponseEntity.ok(estatisticas);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
    }
  }

  @GetMapping("/jogo/{jogoId}")
  @Operation(
      summary = "Buscar estatísticas por jogo",
      description = "Retorna todas as estatísticas de um jogo específico.")
  public ResponseEntity<?> getEstatisticasByJogo(@PathVariable Long jogoId) {
    try {
      List<EstatisticasDTO> estatisticas = estatisticasService.getEstatisticasByJogoId(jogoId);
      return ResponseEntity.ok(estatisticas);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
    }
  }

  @GetMapping("/equipa/{equipaId}")
  @Operation(
      summary = "Buscar estatísticas por equipa",
      description = "Retorna todas as estatísticas dos jogadores de uma equipa específica.")
  public ResponseEntity<?> getEstatisticasByEquipa(@PathVariable Long equipaId) {
    try {
      List<EstatisticasDTO> estatisticas = estatisticasService.getEstatisticasByEquipaId(equipaId);
      return ResponseEntity.ok(estatisticas);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
    }
  }

  @GetMapping("/data/{data}")
  @Operation(
      summary = "Buscar estatísticas por data",
      description = "Retorna todas as estatísticas de jogos numa data específica.")
  public ResponseEntity<?> getEstatisticasByData(
      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
    try {
      List<EstatisticasDTO> estatisticas = estatisticasService.getEstatisticasByData(data);
      return ResponseEntity.ok(estatisticas);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
    }
  }

  @GetMapping("/periodo")
  @Operation(
      summary = "Buscar estatísticas por período",
      description = "Retorna todas as estatísticas de jogos entre duas datas.")
  public ResponseEntity<?> getEstatisticasByPeriodo(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
    try {
      List<EstatisticasDTO> estatisticas = estatisticasService.getEstatisticasByPeriodo(dataInicio, dataFim);
      return ResponseEntity.ok(estatisticas);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
    }
  }

  @GetMapping("/golos")
  @Operation(
      summary = "Buscar estatísticas com golos",
      description = "Retorna todas as estatísticas que contêm golos marcados.")
  public ResponseEntity<List<EstatisticasDTO>> getEstatisticasComGolos() {
    List<EstatisticasDTO> estatisticas = estatisticasService.getEstatisticasComGolos();
    return ResponseEntity.ok(estatisticas);
  }

  @GetMapping("/cartoes")
  @Operation(
      summary = "Buscar estatísticas com cartões",
      description = "Retorna todas as estatísticas que contêm cartões (amarelos ou vermelhos).")
  public ResponseEntity<List<EstatisticasDTO>> getEstatisticasComCartoes() {
    List<EstatisticasDTO> estatisticas = estatisticasService.getEstatisticasComCartoes();
    return ResponseEntity.ok(estatisticas);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Atualizar estatísticas",
      description = "Atualiza as estatísticas existentes com o ID especificado.")
  public ResponseEntity<?> updateEstatisticas(
      @PathVariable Long id, @RequestBody EstatisticasDTO estatisticasDTO) {
    try {
      EstatisticasDTO estatisticasAtualizadas = estatisticasService.updateEstatisticas(id, estatisticasDTO);
      return ResponseEntity.ok(estatisticasAtualizadas);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Remover estatísticas",
      description = "Remove as estatísticas com o ID especificado.")
  public ResponseEntity<?> deleteEstatisticas(@PathVariable Long id) {
    try {
      estatisticasService.deleteById(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }
} 