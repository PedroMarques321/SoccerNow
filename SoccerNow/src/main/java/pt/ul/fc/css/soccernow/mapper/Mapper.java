package pt.ul.fc.css.soccernow.mapper;

import java.util.HashSet;
import java.util.Set;

import pt.ul.fc.css.soccernow.dtos.ArbitroDTO;
import pt.ul.fc.css.soccernow.dtos.EquipaDTO;
import pt.ul.fc.css.soccernow.dtos.EstatisticasDTO;
import pt.ul.fc.css.soccernow.dtos.JogadorDTO;
import pt.ul.fc.css.soccernow.models.Arbitro;
import pt.ul.fc.css.soccernow.models.Equipa;
import pt.ul.fc.css.soccernow.models.Estatisticas;
import pt.ul.fc.css.soccernow.models.Jogador;

public class Mapper {
  // Arbitro //
  public static Arbitro mapDtoToArbitro(ArbitroDTO arbitroDTO) {
    if (arbitroDTO == null) {
      return null;
    }
    Arbitro arbitro = new Arbitro(
      arbitroDTO.getUsername(),
      arbitroDTO.getEmail(),
      arbitroDTO.getPassword(),
      arbitroDTO.getCertificado()
    );
    // arbitro.setId(arbitroDTO.getId());
    // arbitro.setUsername(arbitroDTO.getUsername());
    // arbitro.setEmail(arbitroDTO.getEmail());
    // arbitro.setCertificado(arbitroDTO.getCertificado());
    return arbitro;
  }

  public static ArbitroDTO mapToArbitroDTO(Arbitro arbitro) {
    if (arbitro == null) {
      return null;
    }
    ArbitroDTO arbitroDTO = new ArbitroDTO();
    arbitroDTO.setId(arbitro.getId());
    arbitroDTO.setUsername(arbitro.getUsername());
    arbitroDTO.setEmail(arbitro.getEmail());
    arbitroDTO.setCertificado(arbitro.getCertificado());
    return arbitroDTO;
  }

  // Equipa //
  public static Equipa mapDtoToEquipa(EquipaDTO equipaDTO) {
    if (equipaDTO == null) {
      return null;
    }

    Equipa equipa = new Equipa(equipaDTO.getNome(), equipaDTO.getCidade());

    if (equipaDTO.getId() != null) {
      equipa.setId(equipaDTO.getId());
    }

    equipa.setHistoricoJogos(equipaDTO.getHistoricoJogos());
    equipa.setConquistas(equipaDTO.getConquistas());

    return equipa;
  }

  public static EquipaDTO mapToEquipaDTO(Equipa equipa) {
    if (equipa == null) {
      return null;
    }

    EquipaDTO equipaDTO = new EquipaDTO();
    equipaDTO.setId(equipa.getId());
    equipaDTO.setNome(equipa.getNome());
    equipaDTO.setCidade(equipa.getCidade());
    equipaDTO.setHistoricoJogos(equipa.getHistoricoJogos());
    equipaDTO.setConquistas(equipa.getConquistas());
    Set<JogadorDTO> jogadoresConv = new HashSet<>();
    for (Jogador jogador: equipa.getJogadores()) {
      jogadoresConv.add(mapToJogadorDTO(jogador));
    }
    equipaDTO.setJogadores(jogadoresConv);

    // Não mapear jogadores por agora (evitar referências circulares)
    // Se precisares, cria método separado

    return equipaDTO;
  }

  //TODO: Implementar métodos para mapear jogadores de uma equipa

  public static EquipaDTO mapDtoToEquipaJogadores(EquipaDTO equipaDTO) {
    if (equipaDTO == null) {
      return null;
    }

    EquipaDTO equipaJogadoresDTO = new EquipaDTO();
    equipaJogadoresDTO.setId(equipaDTO.getId());
    equipaJogadoresDTO.setNome(equipaDTO.getNome());
    equipaJogadoresDTO.setCidade(equipaDTO.getCidade());
    equipaJogadoresDTO.setHistoricoJogos(equipaDTO.getHistoricoJogos());
    equipaJogadoresDTO.setConquistas(equipaDTO.getConquistas());
    equipaJogadoresDTO.setJogadores(equipaDTO.getJogadores());

    return equipaJogadoresDTO;
  }
  // Jogador //
  public static Jogador mapDtoToJogador(JogadorDTO jogadorDTO) {
    if (jogadorDTO == null) {
      return null;
    }
    Jogador jogador =
        new Jogador(
            jogadorDTO.getUsername(),
            jogadorDTO.getEmail(),
            jogadorDTO.getPassword(),
            jogadorDTO.getPosicao(),
            jogadorDTO.getNumeroCamisa());
    // jogador.setId(jogadorDTO.getId());
    // jogador.setUsername(jogadorDTO.getUsername());
    // jogador.setEmail(jogadorDTO.getEmail());
    // jogador.setPassword(jogadorDTO.getPassword());
    // jogador.setPosicao(jogadorDTO.getPosicao());
    // jogador.setNumeroCamisa(jogadorDTO.getNumeroCamisa());
    return jogador;
  }

  // Nao colocar password no DTO por questoes de seguranca
  public static JogadorDTO mapToJogadorDTO(Jogador jogador) {
    if (jogador == null) {
      return null;
    }
    JogadorDTO jogadorDTO = new JogadorDTO();
    jogadorDTO.setId(jogador.getId());
    jogadorDTO.setUsername(jogador.getUsername());
    jogadorDTO.setEmail(jogador.getEmail());
    jogadorDTO.setPosicao(jogador.getPosicao());
    jogadorDTO.setNumeroCamisa(jogador.getNumeroCamisa());
    return jogadorDTO;
  }

  // Estatisticas //
  public static Estatisticas mapDtoToEstatisticas(EstatisticasDTO estatisticasDTO) {
    if (estatisticasDTO == null) {
      return null;
    }
    Estatisticas estatisticas = new Estatisticas();
    estatisticas.setId(estatisticasDTO.getId());
    estatisticas.setGolos(estatisticasDTO.getGolos());
    estatisticas.setCartoesAmarelos(estatisticasDTO.getCartoesAmarelos());
    estatisticas.setCartoesVermelhos(estatisticasDTO.getCartoesVermelhos());
    return estatisticas;
  }

  public static EstatisticasDTO mapToEstatisticasDTO(Estatisticas estatisticas) {
    if (estatisticas == null) {
      return null;
    }
    EstatisticasDTO estatisticasDTO = new EstatisticasDTO();
    estatisticasDTO.setId(estatisticas.getId());
    estatisticasDTO.setGolos(estatisticas.getGolos());
    estatisticasDTO.setCartoesAmarelos(estatisticas.getCartoesAmarelos());
    estatisticasDTO.setCartoesVermelhos(estatisticas.getCartoesVermelhos());

    // Mapear informações do jogador
    if (estatisticas.getJogador() != null) {
      estatisticasDTO.setJogadorId(estatisticas.getJogador().getId());
      estatisticasDTO.setJogadorNome(estatisticas.getJogador().getUsername());
    }

    // Mapear informações do jogo
    if (estatisticas.getJogo() != null) {
      estatisticasDTO.setJogoId(estatisticas.getJogo().getId());
      estatisticasDTO.setJogoDataHora(estatisticas.getJogo().getDataHora());
      estatisticasDTO.setJogoLocal(estatisticas.getJogo().getLocal());
      
      // Mapear informações das equipas
      if (estatisticas.getJogo().getEquipaCasa() != null) {
        estatisticasDTO.setEquipaCasa(estatisticas.getJogo().getEquipaCasa().getNome());
      }
      if (estatisticas.getJogo().getEquipaFora() != null) {
        estatisticasDTO.setEquipaFora(estatisticas.getJogo().getEquipaFora().getNome());
      }
    }

    return estatisticasDTO;
  }
}
