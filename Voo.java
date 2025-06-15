import java.time.LocalDateTime;
import java.util.*;
import java.util.Scanner;

public class Voo {
    private int numeroVoo;
    private String origemVoo;
    private String destinoVoo;
    private LocalDateTime dataHorarioPartida;
    private LocalDateTime dataHorarioChegada;
    private StatusVoo statusVoo;
    private Aeronave aeronaveAssociada;

    Set<Integer> numVoo = new HashSet<>();
    Map<String, Passageiros> passageirosNoVoo = new LinkedHashMap<>();
    Map<Integer, Voo> voosDoAeroporto = new HashMap<>();
    Scanner input = new Scanner(System.in);

    public Voo(int numeroVoo, String origemVoo, String destinoVoo, LocalDateTime dataHorarioPartida, LocalDateTime dataHorarioChegada, StatusVoo statusVoo, Aeronave aeronaveAssociada) throws IllegalArgumentException {
        if (numeroVoo <= 0) {
            throw new IllegalArgumentException("nao foi possivel cadastrar voo pois o numero de voo esta invalido");
        } else if (origemVoo.isEmpty() || destinoVoo.isEmpty()) {
            throw new IllegalArgumentException("nao foi possivel cadastrar voo pois ou a origem ou a chegada do voo estão vazias");
        } else {
            this.numeroVoo = numeroVoo;
            this.origemVoo = origemVoo;
            this.destinoVoo = destinoVoo;
            this.dataHorarioPartida = dataHorarioPartida;
            this.dataHorarioChegada = dataHorarioChegada;
            this.statusVoo = statusVoo;
            this.aeronaveAssociada = aeronaveAssociada;
        }
    }

    public int getNumeroVoo() {
        return numeroVoo;
    }

    public void setNumeroVoo(int numeroVoo) throws IllegalArgumentException {
        if (numeroVoo <= 0) {
            throw new IllegalArgumentException("nao foi possivel alterar o numero do voo, pois o numero esta menor que 0");
        } else {
            this.numeroVoo = numeroVoo;
        }
    }

    public String getOrigemVoo() {
        return origemVoo;
    }

    public void setOrigemVoo(String origemVoo) throws IllegalArgumentException {
        if (origemVoo.isEmpty()) {
            throw new IllegalArgumentException("não foi possivel alterar a origem do voo pois não foi nada escrito");
        } else {
            this.origemVoo = origemVoo;
        }
    }

    public String getDestinoVoo() {
        return destinoVoo;
    }

    public void setDestinoVoo(String destinoVoo) throws IllegalArgumentException {
        if (destinoVoo.isEmpty()) {
            throw new IllegalArgumentException("não foi possivel alterar o destino do voo pois nada foi escrito");
        } else {
            this.destinoVoo = destinoVoo;
        }
    }

    public LocalDateTime getDataHorarioPartida() {
        return dataHorarioPartida;
    }

    public void setDataHorarioPartida(LocalDateTime dataHorarioPartida) {
        this.dataHorarioPartida = dataHorarioPartida;
    }

    public LocalDateTime getDataHorarioChegada() {
        return dataHorarioChegada;
    }

    public void setDataHorarioChegada(LocalDateTime dataHorarioChegada) {
        this.dataHorarioChegada = dataHorarioChegada;
    }

    public StatusVoo getStatusVoo() {
        return statusVoo;
    }

    public void setStatusVoo(StatusVoo statusVoo) {
        this.statusVoo = statusVoo;
    }

    public Aeronave getAeronaveAssociada() {
        return aeronaveAssociada;
    }

    public void setAeronaveAssociada(Aeronave aeronaveAssociada) {
        this.aeronaveAssociada = aeronaveAssociada;
    }

    public void cadastrarVoo(Voo voo) throws VooDuplicadoException {
        if (!numVoo.add(voo.getNumeroVoo())) {
            throw new VooDuplicadoException("não foi possivel cadastrar voo pois o numero de voo esta duplicado");
        } else {
            voosDoAeroporto.put(voo.getNumeroVoo(), voo);
            numVoo.add(voo.getNumeroVoo());
            System.out.println("Voo do numero " +voo.getNumeroVoo()+ " registrado no sistema do aeroporto");
        }
    }

    public void cancelarVoo() {
        System.out.println("qual o numero do voo que vc deseja cancelar?");
        int num = input.nextInt();

        Voo voo = voosDoAeroporto.values().stream()
                .filter(a -> a.getNumeroVoo() == num)
                .findFirst()
                .orElseThrow(() -> new VooNaoEncontradoException("não foi possivel cancelar o voo, pois este voo não foi encontrado nos sistema"));

        System.out.println("===== DETALHES DO VOO =====");
        voo.exibirDetalhesVoo();
        System.out.println("1 - sim");
        System.out.println("2 - não");
        System.out.print("esse é o voo que vc deseja cancelar?");
        int opcao = input.nextInt();

        switch (opcao) {
            case 1:
                voo.setStatusVoo(StatusVoo.CANCELADO);
                System.out.println("o voo " +voo.getNumeroVoo()+ " foi cancelado");
                break;
            case 2:
                System.out.println("se vc deseja cancelar outro voo, tente novamente");
                break;
            default:
                System.out.println("opção invalida, por favor digite novamente");
        }
    }

    public void exibirDetalhesVoo() {
        System.out.println("NUMERO DO VOO: " +this.numeroVoo);
        System.out.println("NUMERO DA AERONAVE: " +this.aeronaveAssociada.getNumeroRegistro());
        System.out.println("ORIGEM DO VOO: " +this.origemVoo);
        System.out.println("DESTINO DO VOO: " +this.destinoVoo);
        System.out.println("DATA DE PARTIDA: " +this.dataHorarioPartida);
        System.out.println("DATA DE CHEGADA: " +this.dataHorarioChegada);
        System.out.println("STATUS DO VOO: " +this.statusVoo);
        System.out.println("-----------------------------");
    }
}
