package pt.ul.fc.css.soccernow.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import pt.ul.fc.css.soccernow.models.Campeonato;
import pt.ul.fc.css.soccernow.services.CampeonatoService;

@RestController
@RequestMapping("/api/campeonatos")
public class CampeonatoController {
    @Autowired
    private CampeonatoService campeonatoService;

    @PostMapping
    public ResponseEntity<Campeonato> criarCampeonato(@RequestBody Campeonato campeonato) {
        Campeonato novo = campeonatoService.criarCampeonato(campeonato);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @GetMapping
    public List<Campeonato> listarCampeonatos() {
        return campeonatoService.listarCampeonatos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campeonato> getCampeonato(@PathVariable Long id) {
        Campeonato c = campeonatoService.getCampeonato(id);
        if (c == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(c);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Campeonato> atualizarCampeonato(@PathVariable Long id, @RequestBody Campeonato campeonato) {
        Campeonato atualizado = campeonatoService.atualizarCampeonato(id, campeonato);
        if (atualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerCampeonato(@PathVariable Long id) {
        boolean removido = campeonatoService.removerCampeonato(id);
        if (!removido) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
