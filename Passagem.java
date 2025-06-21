public class Passagem {
    private int numeroPassagem;
    private Passageiros passageiro;
    private Voo vooPassagem;

    public Passagem(int numeroPassagem, Passageiros passageiro, Voo vooPassagem) throws NumeroPassagemInvalidoExcepton {
        if (numeroPassagem <= 0) {
            throw new NumeroPassagemInvalidoExcepton("não foi possivel fornecer a passagem pois o numero da passagem é invalido");
        } else {
            this.numeroPassagem = numeroPassagem;
            this.passageiro = passageiro;
            this.vooPassagem = vooPassagem;
        }
    }

    public int getNumeroPassagem() {
        return numeroPassagem;
    }

    public void setNumeroPassagem(int numeroPassagem) throws NumeroPassagemInvalidoExcepton {
        if (numeroPassagem <= 0) {
            throw new NumeroPassagemInvalidoExcepton("não foi possivel fornecer a passagem, pois o numero esta invalido");
        } else {
            this.numeroPassagem = numeroPassagem;
        }
    }

    public Passageiros getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(Passageiros passageiro) {
        this.passageiro = passageiro;
    }

    public Voo getVooPassagem() {
        return vooPassagem;
    }

    public void setVooPassagem(Voo vooPassagem) {
        this.vooPassagem = vooPassagem;
    }
}
