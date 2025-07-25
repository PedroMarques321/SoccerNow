package pt.ul.fc.css.soccernow.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.models.Campeonato;
import pt.ul.fc.css.soccernow.repositories.CampeonatoRepository;

@Service
public class CampeonatoService {
    @Autowired
    private CampeonatoRepository campeonatoRepository;

    public Campeonato criarCampeonato(Campeonato campeonato) {
        return campeonatoRepository.save(campeonato);
    }

    public List<Campeonato> listarCampeonatos() {
        return campeonatoRepository.findAll();
    }

    public Campeonato getCampeonato(Long id) {
        return campeonatoRepository.findById(id).orElse(null);
    }

    public Campeonato atualizarCampeonato(Long id, Campeonato dados) {
        return campeonatoRepository.findById(id).map(c -> {
            c.setNome(dados.getNome());
            c.setDataInicio(dados.getDataInicio());
            c.setDataFim(dados.getDataFim());
            c.setModalidade(dados.getModalidade());
            return campeonatoRepository.save(c);
        }).orElse(null);
    }

    public boolean removerCampeonato(Long id) {
        if (!campeonatoRepository.existsById(id)) return false;
        campeonatoRepository.deleteById(id);
        return true;
    }
}
