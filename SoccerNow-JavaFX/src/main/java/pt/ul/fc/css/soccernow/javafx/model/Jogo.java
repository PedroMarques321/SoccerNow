package pt.ul.fc.css.soccernow.javafx.model;

import java.util.List;

public class Jogo {
    private Long id;
    private String dataHora;
    private String local;
    private Long equipaCasaId;
    private Long equipaVisitanteId;
    private List<Long> jogadoresCasaIds;
    private Long guardaRedesCasaId;
    private List<Long> jogadoresVisitanteIds;
    private Long guardaRedesVisitanteId;
    private Long arbitroPrincipalId;
    private List<Long> arbitrosAuxiliaresIds;
    private String tipoJogo; // "Amigável" ou "Competitivo"
    private String campeonato;

    // Getters e Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public Long getEquipaCasaId() { return equipaCasaId; }
    public void setEquipaCasaId(Long equipaCasaId) { this.equipaCasaId = equipaCasaId; }

    public Long getEquipaVisitanteId() { return equipaVisitanteId; }
    public void setEquipaVisitanteId(Long equipaVisitanteId) { this.equipaVisitanteId = equipaVisitanteId; }

    public List<Long> getJogadoresCasaIds() { return jogadoresCasaIds; }
    public void setJogadoresCasaIds(List<Long> jogadoresCasaIds) { this.jogadoresCasaIds = jogadoresCasaIds; }

    public Long getGuardaRedesCasaId() { return guardaRedesCasaId; }
    public void setGuardaRedesCasaId(Long guardaRedesCasaId) { this.guardaRedesCasaId = guardaRedesCasaId; }

    public List<Long> getJogadoresVisitanteIds() { return jogadoresVisitanteIds; }
    public void setJogadoresVisitanteIds(List<Long> jogadoresVisitanteIds) { this.jogadoresVisitanteIds = jogadoresVisitanteIds; }

    public Long getGuardaRedesVisitanteId() { return guardaRedesVisitanteId; }
    public void setGuardaRedesVisitanteId(Long guardaRedesVisitanteId) { this.guardaRedesVisitanteId = guardaRedesVisitanteId; }

    public Long getArbitroPrincipalId() { return arbitroPrincipalId; }
    public void setArbitroPrincipalId(Long arbitroPrincipalId) { this.arbitroPrincipalId = arbitroPrincipalId; }

    public List<Long> getArbitrosAuxiliaresIds() { return arbitrosAuxiliaresIds; }
    public void setArbitrosAuxiliaresIds(List<Long> arbitrosAuxiliaresIds) { this.arbitrosAuxiliaresIds = arbitrosAuxiliaresIds; }

    public String getTipoJogo() { return tipoJogo; }
    public void setTipoJogo(String tipoJogo) { this.tipoJogo = tipoJogo; }

    public String getCampeonato() { return campeonato; }
    public void setCampeonato(String campeonato) { this.campeonato = campeonato; }
}