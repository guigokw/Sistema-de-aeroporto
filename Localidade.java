public enum Localidade {
    SAO_PAULO(932),
    RIO_DE_JANEIRO(518),
    FLORIANOPOLIS(1638),
    GOIAS(1406),
    BELO_HORIZONTE(512);


    private double distancia;

    Localidade (double distancia) {
        this.distancia = distancia;
    }

    public double getDistancia() {
        return distancia;
    }
}
