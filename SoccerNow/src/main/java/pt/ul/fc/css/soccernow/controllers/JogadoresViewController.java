package pt.ul.fc.css.soccernow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ul.fc.css.soccernow.services.JogadorService;
import pt.ul.fc.css.soccernow.dtos.JogadorDTO;

import java.util.List;

@Controller
public class JogadoresViewController {

    @Autowired
    private JogadorService jogadorService;

    @GetMapping("/jogadores")
    public String mostrarJogadores(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String posicao,
        Model model) {

        List<JogadorDTO> jogadores = jogadorService.getAllJogadores();

        if (nome != null && !nome.isBlank()) {
            jogadores = jogadores.stream()
                .filter(j -> j.getUsername().toLowerCase().contains(nome.toLowerCase()))
                .toList();
        }
        if (posicao != null && !posicao.isBlank()) {
            jogadores = jogadores.stream()
                .filter(j -> j.getPosicao().toLowerCase().contains(posicao.toLowerCase()))
                .toList();
        }

        model.addAttribute("jogadores", jogadores);
        return "jogadores";
    }
}