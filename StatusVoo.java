public enum StatusVoo {
    AGENDADO("Voo programado em uma data e horario especificos"),
    EMBARCANDO("Processo de entrada dos passageiros na aeronave esta acontecendo"),
    EM_VOO("Aeronave neste momento esta fazendo a sua trajetoria ate seu destino"),
    ATRASADO("A decolagem ou chegada de um avião está prevista para ocorrer fora do horário originalmente agendado"),
    CANCELADO("O voo programado nao sera realizado mais"),
    FINALIZADO("O voo ja chegou ao seu destino");


    private String descricao;

    StatusVoo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
