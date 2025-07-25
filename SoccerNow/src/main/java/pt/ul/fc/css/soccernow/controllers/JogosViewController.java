package pt.ul.fc.css.soccernow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ul.fc.css.soccernow.services.EquipaService;
import pt.ul.fc.css.soccernow.services.JogoService;
import pt.ul.fc.css.soccernow.dtos.EquipaDTO;
import pt.ul.fc.css.soccernow.dtos.JogoDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
public class JogosViewController {

    @Autowired
    private JogoService jogoService;

    @Autowired
    private EquipaService equipaService;

    @GetMapping("/jogos")
    public String listarJogos(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer minGolosCasa,
            @RequestParam(required = false) Integer maxGolosCasa,
            @RequestParam(required = false) Integer minGolosFora,
            @RequestParam(required = false) Integer maxGolosFora,
            @RequestParam(required = false) String localizacao,
            @RequestParam(required = false) String turno,
            Model model) {

        List<JogoDTO> jogos = jogoService.listarJogos();

        // Filtro por estado
        if ("FINALIZADO".equalsIgnoreCase(estado)) {
            jogos = jogos.stream()
                    .filter(j -> "FINALIZADO".equals(j.getEstado()))
                    .toList();
        } else if ("A_REALIZAR".equalsIgnoreCase(estado)) {
            jogos = jogos.stream()
                    .filter(j -> "AGENDADO".equals(j.getEstado()) || "ADIADO".equals(j.getEstado()))
                    .toList();
        }

        // Filtros por golos casa
        if (minGolosCasa != null) {
            jogos = jogos.stream()
                    .filter(j -> j.getGolosCasa() != null && j.getGolosCasa() >= minGolosCasa)
                    .toList();
        }
        if (maxGolosCasa != null) {
            jogos = jogos.stream()
                    .filter(j -> j.getGolosCasa() != null && j.getGolosCasa() <= maxGolosCasa)
                    .toList();
        }

        // Filtros por golos fora
        if (minGolosFora != null) {
            jogos = jogos.stream()
                    .filter(j -> j.getGolosFora() != null && j.getGolosFora() >= minGolosFora)
                    .toList();
        }
        if (maxGolosFora != null) {
            jogos = jogos.stream()
                    .filter(j -> j.getGolosFora() != null && j.getGolosFora() <= maxGolosFora)
                    .toList();
        }

        // Filtro por localização
        if (localizacao != null && !localizacao.isBlank()) {
            jogos = jogos.stream()
                    .filter(j -> j.getLocal() != null &&
                            j.getLocal().toLowerCase().contains(localizacao.toLowerCase()))
                    .toList();
        }

        // Filtro por turno (calculado pela hora da data)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (turno != null && !turno.isBlank()) {
            jogos = jogos.stream()
                    .filter(j -> {
                        if (j.getDataHora() == null) return false;
                        try {
                            LocalDateTime dataHora = LocalDateTime.parse(j.getDataHora(), formatter);
                            LocalTime hora = dataHora.toLocalTime();
                            String t;
                            if (hora.isBefore(LocalTime.of(12, 0))) {
                                t = "MANHA";
                            } else if (hora.isBefore(LocalTime.of(19, 0))) {
                                t = "TARDE";
                            } else {
                                t = "NOITE";
                            }
                            return t.equalsIgnoreCase(turno);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .toList();
        }

        // Map de nomes das equipas
        Map<Long, String> nomesEquipas = equipaService.getAllEquipas().stream()
            .collect(Collectors.toMap(EquipaDTO::getId, EquipaDTO::getNome));

        // Map de turnos dos jogos
        Map<Long, String> turnosJogos = new HashMap<>();
        for (JogoDTO jogo : jogos) {
            String turnoStr = "";
            try {
                if (jogo.getDataHora() != null) {
                    LocalDateTime dataHora = LocalDateTime.parse(jogo.getDataHora(), formatter);
                    LocalTime hora = dataHora.toLocalTime();
                    if (hora.isBefore(LocalTime.of(12, 0))) turnoStr = "Manhã";
                    else if (hora.isBefore(LocalTime.of(19, 0))) turnoStr = "Tarde";
                    else turnoStr = "Noite";
                }
            } catch (Exception ignored) {}
            turnosJogos.put(jogo.getId(), turnoStr);
        }

        model.addAttribute("jogos", jogos);
        model.addAttribute("nomesEquipas", nomesEquipas);
        model.addAttribute("turnosJogos", turnosJogos);
        return "jogos";
    }
}