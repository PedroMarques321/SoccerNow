package pt.ul.fc.css.soccernow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ul.fc.css.soccernow.dtos.CampeonatoDTO;
import pt.ul.fc.css.soccernow.dtos.EquipaDTO;
import pt.ul.fc.css.soccernow.dtos.JogoDTO;
import pt.ul.fc.css.soccernow.models.Campeonato;
import pt.ul.fc.css.soccernow.models.Equipa;
import pt.ul.fc.css.soccernow.services.CampeonatoService;
import pt.ul.fc.css.soccernow.services.EquipaService;
import pt.ul.fc.css.soccernow.services.JogoService;

import java.util.List;

@Controller
public class CampeonatosViewController {

    @Autowired
    private CampeonatoService campeonatoService;

    @Autowired
    private EquipaService equipaService;

    @Autowired
    private JogoService jogoService;

    @GetMapping("/campeonatos")
    public String listarCampeonatos(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) Long equipaId,
        @RequestParam(required = false) Integer numJogosRealizados,
        @RequestParam(required = false) Integer numJogosARealizar,
        Model model) {

        List<Campeonato> campeonatos = campeonatoService.listarCampeonatos();
        List<JogoDTO> jogos = jogoService.listarJogos();

        // Filtros por nome e equipa (antes de calcular os campos)
        if (nome != null && !nome.isBlank()) {
            campeonatos = campeonatos.stream()
                    .filter(c -> c.getNome() != null && c.getNome().toLowerCase().contains(nome.toLowerCase()))
                    .toList();
        }
        if (equipaId != null) {
            campeonatos = campeonatos.stream()
                    .filter(c -> jogos.stream().anyMatch(j ->
                            j.getCampeonatoId() != null &&
                            j.getCampeonatoId().equals(c.getId()) &&
                            (j.getEquipaCasaId().equals(equipaId) || j.getEquipaVisitanteId().equals(equipaId))
                    ))
                    .toList();
        }

        
        List<CampeonatoDTO> lista = campeonatos.stream().map(c -> {
            List<JogoDTO> jogosCamp = jogos.stream()
                    .filter(j -> j.getCampeonatoId() != null && j.getCampeonatoId().equals(c.getId()))
                    .toList();

            long realizados = jogosCamp.stream().filter(j -> "FINALIZADO".equals(j.getEstado())).count();
            long aRealizar = jogosCamp.stream().filter(j -> !"FINALIZADO".equals(j.getEstado())).count();

            // Filtros por número de jogos realizados/a realizar
            if (numJogosRealizados != null && realizados != numJogosRealizados) return null;
            if (numJogosARealizar != null && aRealizar != numJogosARealizar) return null;

            return new CampeonatoDTO(
                    c.getId(),
                    c.getNome(),
                    c.getDataInicio(),
                    c.getDataFim(),
                    c.getModalidade(),
                    (int) realizados,
                    (int) aRealizar
            );
        }).filter(dto -> dto != null).toList();

        List<EquipaDTO> equipas = equipaService.getAllEquipas();

        model.addAttribute("campeonatos", lista);
        model.addAttribute("equipas", equipas);
        return "campeonatos";
    }
}

