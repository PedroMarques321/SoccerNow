package pt.ul.fc.css.soccernow.enums;

public enum EstadoJogo {
    AGENDADO("Agendado"),
    EM_ANDAMENTO("Em Andamento"),
    FINALIZADO("Finalizado"),
    CANCELADO("Cancelado"),
    ADIADO("Adiado");
    
    private final String nome;
    
    EstadoJogo(String nome) {
        this.nome = nome;
    }
    
    public String getNome() {
        return nome;
    }
    
    // Métodos de conveniência para regras de negócio
    public boolean permiteAlteracaoEquipas() {
        return this == AGENDADO || this == ADIADO;
    }
    
    public boolean permiteRegistoResultado() {
        return this == EM_ANDAMENTO || this == AGENDADO;
    }
    
    public boolean jogoRealizado() {
        return this == FINALIZADO;
    }
    
    public boolean jogoAtivo() {
        return this == AGENDADO || this == EM_ANDAMENTO || this == ADIADO;
    }
    
    @Override
    public String toString() {
        return nome;
    }
}