import java.util.*;
import java.util.Scanner;

public class Aeronave {
    private String modelo;
    private int capacidadeMaxima;
    private int numeroRegistro;

    Map<Integer, Voo> voosDaAeronave = new LinkedHashMap<>();
    Map<Integer, Aeronave> aeronaves = new HashMap<>();
    Set<Integer> numRegistro = new HashSet<>();
    Scanner input = new Scanner(System.in);

    public Aeronave(String modelo, int capacidadeMaxima, int numeroRegistro) throws NumeroRegistroInvalidoException, IllegalArgumentException, CapacidadeMaximaInvalida {
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

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(int capacidadeMaxima) throws CapacidadeMaximaInvalida {
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
        } else if (!numRegistro.add(numeroRegistro)) {
            throw new NumeroRegistroDuplicadoException("nao foi possivel fazer a alteração do numero de registro, pois ja existe esse numero no sistema");
        } else {
            this.numeroRegistro = numeroRegistro;
        }
    }

    public void cadastrarAeronave(Aeronave aeronave) throws NumeroRegistroDuplicadoException {
        if (!numRegistro.add(numeroRegistro)) {
            throw new NumeroRegistroDuplicadoException("Não foi possivel cadastrar a aeronave pois o numero de registro esta duplicado");
        } else {
            aeronaves.put(aeronave.getNumeroRegistro(), aeronave);
            numRegistro.add(aeronave.getNumeroRegistro());
            System.out.println("Aeronave do modelo " +aeronave.getModelo()+ " do numero " +aeronave.getNumeroRegistro()+ " foi registrada no sistema");
        }
    }

    public void removerAeronave() throws AeronaveNaoEncontradaException {
        System.out.print("qual o numero de registro da aeronave que vc deseja remover do sistema");
        int num = input.nextInt();

        Aeronave aeronave = aeronaves.values().stream()
                .filter(a -> a.getNumeroRegistro() == num)
                .findFirst()
                .orElseThrow(() -> new AeronaveNaoEncontradaException("nao foi possivel remover a aeronave, pois ela não foi encontrada"));

        aeronaves.remove(aeronave.getNumeroRegistro(), aeronave);
        numRegistro.remove(aeronave.getNumeroRegistro());
        System.out.println("Aeronave do numero " +aeronave.getNumeroRegistro()+ " removido do sistema");
    }

    public void associarVooAeronave(Voo voo) throws NumeroVooDuplicadoException {
        if (!voo.numVoo.add(voo.getNumeroVoo())) {
            throw new NumeroVooDuplicadoException("nao foi possivel associar o voo com a aeronave pois o numero de voo esta duplicado");
        } else {
            voosDaAeronave.put(voo.getNumeroVoo(), voo);
            System.out.println("Voo do numero " +voo.getNumeroVoo()+ " com destino a " +voo.getDestinoVoo()+ " foi associado a aeronave de numero " +this.numeroRegistro);
        }
    }

    public void listarAeronaves() {
        if (aeronaves.isEmpty()) {
            System.out.println("não foi possivel listar as aeronaves, pois a lista de aeronaves do sistema esta vazia");
        } else {
            System.out.println("====== LISTA DE AERONAVES ======");
            for (Aeronave aeronave : aeronaves.values()) {
                exibirDetalhesAeronave();
            }
        }
    }

    public void exibirDetalhesAeronave() {
        System.out.println("NUMERO DE REGISTRO DA AERONAVE " +this.numeroRegistro);
        System.out.println("MODELO DA AERONAVE: " +this.modelo);
        System.out.println("CAPACIDADE MAXIMA DE PASSAGEIROS: " +this.capacidadeMaxima);
        System.out.println("------------------------------");
    }
}
