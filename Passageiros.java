import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Passageiros {
    private String nomePassageiro;
    private String cpfPassageiro;

    List<Voo> passageirosVoosCadastrados = new ArrayList<>();
    Scanner input = new Scanner(System.in);

    public Passageiros(String nomePassageiro, String cpfPassageiro) throws IllegalArgumentException, CpfInvalidoException {
        if (nomePassageiro.isEmpty()) {
            throw new IllegalArgumentException("não foi possivel cadastrar o passageiro pois o nome esta nulo");
        } else if (cpfPassageiro.length() != 11) {
            throw new CpfInvalidoException("não foi possivel cadastrar o passageiro pois o cpf é invalido");
        } else {
            this.nomePassageiro = nomePassageiro;
            this.cpfPassageiro = cpfPassageiro;
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


    public void exibirDetalhesPassageiro() {
        System.out.println("NOME DO PASSAGEIRO: " +this.nomePassageiro);
        System.out.println("CPF DO PASSAGEIRO: " +this.cpfPassageiro);
        System.out.println("-----------------------");
    }
}
