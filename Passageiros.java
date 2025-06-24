import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Passageiros {
    private String nomePassageiro;
    private String cpfPassageiro;
    private String numeroPassaporte;

    List<Voo> passageirosVoosCadastrados = new ArrayList<>();
    List<Passagem> passagensDoPassageiro = new ArrayList<>();
    Scanner input = new Scanner(System.in);

    public Passageiros(String nomePassageiro, String cpfPassageiro, String numeroPassaporte) throws IllegalArgumentException, CpfInvalidoException {
        try {
            if (nomePassageiro.isEmpty()) {
                throw new IllegalArgumentException("não foi possivel cadastrar o passageiro pois o nome esta nulo");
            } else if (cpfPassageiro.length() != 11) {
                throw new CpfInvalidoException("não foi possivel cadastrar o passageiro pois o cpf é invalido");
            } else if (numeroPassaporte.length() != 6) {
                throw new PassaporteInvalidoException("não foi possivel cadastrar o passageiro pois o seu numero de passaporte esta invalido");
            } else {
                this.nomePassageiro = nomePassageiro;
                this.cpfPassageiro = cpfPassageiro;
            }
        } catch (IllegalArgumentException | CpfInvalidoException | PassaporteInvalidoException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getNomePassageiro() {
        return nomePassageiro;
    }

    public void setNomePassageiro(String nomePassageiro) throws IllegalArgumentException {
        if (nomePassageiro.isEmpty()) {
            throw new IllegalArgumentException("não foi possivel cadastrar o passageiro pois o nome esta nulo");
        } else {
            this.nomePassageiro = nomePassageiro;
        }
    }

    public String getCpfPassageiro() {
        return cpfPassageiro;
    }

    public void setCpfPassageiro(String cpfPassageiro) {
        if (cpfPassageiro.length() != 11) {
            throw new CpfInvalidoException("não foi possivel cadastrar o passageiro pois o cpf é invalido");
        } else {
            this.cpfPassageiro = cpfPassageiro;
        }
    }

    public String getNumeroPassaporte() {
        return numeroPassaporte;
    }

    public void setNumeroPassaporte(String numeroPassaporte) {
        if (numeroPassaporte.length() != 6) {
            throw new PassaporteInvalidoException("não foi possivel cadastrar o passageiro pois o numero do passaporte esta invalido");
        } else {
            this.numeroPassaporte = numeroPassaporte;
        }
    }

    public void exibirDetalhesPassageiro() {
        System.out.println("NOME DO PASSAGEIRO: " +this.nomePassageiro);
        System.out.println("CPF DO PASSAGEIRO: " +this.cpfPassageiro);
        System.out.println("NUMERO DO PASSAPORTE: " +this.numeroPassaporte);
        System.out.println("-----------------------");
    }
}
