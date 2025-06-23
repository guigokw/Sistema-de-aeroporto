import java.util.*;
import java.util.Scanner;

public class Aeronave {
    private String modelo;
    private long capacidadeMaxima;
    private int numeroRegistro;

    Map<Integer, Voo> voosDaAeronave = new LinkedHashMap<>();
    Scanner input = new Scanner(System.in);

    public Aeronave(String modelo, long capacidadeMaxima, int numeroRegistro) throws NumeroRegistroInvalidoException, IllegalArgumentException, CapacidadeMaximaInvalida {
        try {
            if (numeroRegistro <= 0) {
                throw new NumeroRegistroInvalidoException("nao foi possivel cadastrar aeronave, pois o numero de registro esta invalido");
            } else if (modelo.isEmpty()) {
                throw new IllegalArgumentException("nao foi possivel cadastrar aeronave pois o modelo esta nulo");
            } else if (capacidadeMaxima <= 0 || capacidadeMaxima > 850) {
                throw new CapacidadeMaximaInvalida("não foi possivel cadastrar aeronave pois a capacidade maxima esta invalida, sendo menor que 0 ou maior que 850");
            } else {
                this.modelo = modelo;
                this.capacidadeMaxima = capacidadeMaxima;
                this.numeroRegistro = numeroRegistro;
            }
        } catch (NumeroRegistroInvalidoException | IllegalArgumentException| CapacidadeMaximaInvalida e) {
            System.out.println(e.getMessage());
        } catch (java.util.InputMismatchException e) {
            System.out.println("entrada invalida, por favor digite novamente");
            input.nextLine();
        }
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) throws IllegalArgumentException {
        if (modelo.isEmpty()) {
            throw new IllegalArgumentException("nao foi possivel cadastrar aeronave pois o modelo esta nulo");
        } else {
            this.modelo = modelo;
        }
    }

    public long getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(long capacidadeMaxima) throws CapacidadeMaximaInvalida {
        if (capacidadeMaxima <= 0 || capacidadeMaxima > 850) {
            throw new CapacidadeMaximaInvalida("não foi possivel cadastrar aeronave pois a capacidade maxima esta invalida, sendo menor que 0 ou maior que 850");
        } else {
            this.capacidadeMaxima = capacidadeMaxima;
        }
    }

    public int getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(int numeroRegistro) throws NumeroRegistroInvalidoException, NumeroRegistroDuplicadoException {
        if (numeroRegistro <= 0) {
            throw new NumeroRegistroInvalidoException("nao foi possivel cadastrar aeronave, pois o numero de registro esta invalido");
        } else {
            this.numeroRegistro = numeroRegistro;
        }
    }

    public void exibirDetalhesAeronave() {
        System.out.println("NUMERO DE REGISTRO DA AERONAVE " +this.numeroRegistro);
        System.out.println("MODELO DA AERONAVE: " +this.modelo);
        System.out.println("CAPACIDADE MAXIMA DE PASSAGEIROS: " +this.capacidadeMaxima);
        System.out.println("------------------------------");
    }
}
