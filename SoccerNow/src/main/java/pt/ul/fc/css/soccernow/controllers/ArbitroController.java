package pt.ul.fc.css.soccernow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.soccernow.dtos.ArbitroDTO;
import pt.ul.fc.css.soccernow.services.ArbitroService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/arbitros")
public class ArbitroController {

    @Autowired
    private ArbitroService arbitroService;

    @GetMapping
    public List<ArbitroDTO> listarArbitros() {
        return arbitroService.listarArbitros();
    }

    @PostMapping
    public ArbitroDTO criarArbitro(@RequestBody ArbitroDTO arbitroDTO) {
        return arbitroService.registerArbitro(arbitroDTO);
    }
    @PutMapping("/{id}")
    public ArbitroDTO atualizarArbitro(@PathVariable Long id, @RequestBody ArbitroDTO arbitroDTO) {
        return arbitroService.updateArbitro(id, arbitroDTO);
    }

    @GetMapping("/{id}")
    public Optional<ArbitroDTO> buscarArbitroPorId(@PathVariable Long id) {
        return arbitroService.findByIdDTO(id);
    }

    @DeleteMapping("/{id}")
    public void apagarArbitro(@PathVariable Long id) {
        arbitroService.deleteById(id);
    }

}