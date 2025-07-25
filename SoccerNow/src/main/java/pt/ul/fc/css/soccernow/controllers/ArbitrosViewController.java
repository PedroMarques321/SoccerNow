package pt.ul.fc.css.soccernow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ul.fc.css.soccernow.services.ArbitroService;
import pt.ul.fc.css.soccernow.services.JogoService;
import pt.ul.fc.css.soccernow.dtos.ArbitroDTO;

import java.util.List;
import java.util.Map;

@Controller
public class ArbitrosViewController {

    @Autowired
    private ArbitroService arbitroService;

    @Autowired
    private JogoService jogoService;

    @GetMapping("/arbitros")
    public String listarArbitros(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer numJogos,
            Model model) {

        List<ArbitroDTO> arbitros = arbitroService.listarArbitros();
        var jogos = jogoService.listarJogos();

        Map<Long, Long> numJogosPorArbitro = new java.util.HashMap<>();
        for (ArbitroDTO arb : arbitros) {
            long count = jogos.stream().filter(jogo ->
                "FINALIZADO".equals(jogo.getEstado()) &&
                (
                    (jogo.getArbitroPrincipalId() != null && jogo.getArbitroPrincipalId().equals(arb.getId())) ||
                    (jogo.getArbitrosAuxiliaresIds() != null && java.util.Arrays.asList(jogo.getArbitrosAuxiliaresIds()).contains(arb.getId()))
                )
            ).count();
            numJogosPorArbitro.put(arb.getId(), count);
        }

        if (nome != null && !nome.isBlank()) {
            arbitros = arbitros.stream()
                    .filter(a -> a.getUsername() != null && a.getUsername().toLowerCase().contains(nome.toLowerCase()))
                    .toList();
        }

        if (numJogos != null) {
            arbitros = arbitros.stream()
                    .filter(a -> numJogosPorArbitro.getOrDefault(a.getId(), 0L).intValue() == numJogos)
                    .toList();
        }

        model.addAttribute("arbitros", arbitros);
        model.addAttribute("numJogosPorArbitro", numJogosPorArbitro);
        return "arbitros";
    }

}