package pt.ul.fc.css.soccernow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.css.soccernow.services.EquipaService;
import pt.ul.fc.css.soccernow.dtos.EquipaDTO;
import pt.ul.fc.css.soccernow.dtos.JogadorDTO;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class EquipasViewController {

    @Autowired
    private EquipaService equipaService;

    @GetMapping("/equipas")
    public String mostrarEquipas(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer numJogadores,
            @RequestParam(required = false) String conquistas,
            @RequestParam(required = false) String ausenciaPosicao,
            Model model) {

        List<EquipaDTO> equipas = equipaService.getAllEquipas();

        if (nome != null && !nome.isBlank()) {
            equipas = equipas.stream()
                    .filter(e -> e.getNome() != null && e.getNome().toLowerCase().startsWith(nome.toLowerCase()))
                    .toList();
        }
        if (numJogadores != null) {
            equipas = equipas.stream()
                    .filter(e -> e.getJogadores() != null && e.getJogadores().size() == numJogadores)
                    .toList();
        }
        if (conquistas != null && !conquistas.isBlank()) {
            equipas = equipas.stream()
                    .filter(e -> e.getConquistas() != null && e.getConquistas().toLowerCase().contains(conquistas.toLowerCase()))
                    .toList();
        }
        if (ausenciaPosicao != null && !ausenciaPosicao.isBlank()) {
            equipas = equipas.stream()
                    .filter(e -> e.getJogadores() == null ||
                            e.getJogadores().stream().noneMatch(j ->
                                    j.getPosicao() != null &&
                                    j.getPosicao().equalsIgnoreCase(ausenciaPosicao)
                            ))
                    .toList();
        }

        model.addAttribute("equipas", equipas);
        return "equipas";
    }
}