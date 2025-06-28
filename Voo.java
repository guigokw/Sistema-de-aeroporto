import java.time.LocalDateTime;
import java.util.*;
import java.util.Scanner;

public class Voo {
    private int numeroVoo;
    private String origemVoo;
    private Localidade destinoVoo;
    private LocalDateTime dataHorarioPartida;
    private LocalDateTime dataHorarioChegada;
    private StatusVoo statusVoo;
    private Aeronave aeronaveAssociada;

    Map<Integer, Passagem> passageirosNoVoo = new LinkedHashMap<>();
    Map<Integer, Passagem> passageirosCheckIn = new HashMap<>();
    Scanner input = new Scanner(System.in);

    public Voo(int numeroVoo, String origemVoo, Localidade destinoVoo, LocalDateTime dataHorarioPartida, LocalDateTime dataHorarioChegada, StatusVoo statusVoo, Aeronave aeronaveAssociada) throws IllegalArgumentException {
        if (numeroVoo <= 0) {
            throw new IllegalArgumentException("nao foi possivel cadastrar voo pois o numero de voo esta invalido");
        } else if (origemVoo.isEmpty()) {
            throw new IllegalArgumentException("nao foi possivel cadastrar voo pois ou a origem ou a chegada do voo estão vazias");
        } else {
            this.numeroVoo = numeroVoo;
            this.origemVoo = "VITORIA";
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

    public Localidade getDestinoVoo() {
        return destinoVoo;
    }

    public void setDestinoVoo(Localidade destinoVoo) {
        this.destinoVoo = destinoVoo;
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

    public void cancelarVoo(Voo voo) throws VooNaoEncontradoException, IllegalArgumentException {
        try {
            System.out.println("===== DETALHES DO VOO =====");
            voo.exibirDetalhesVoo();
            System.out.println("1 - sim");
            System.out.println("2 - não");
            System.out.print("esse é o voo que vc deseja cancelar?");
            int opcao = input.nextInt();

            switch (opcao) {
                case 1:
                    if (voo.statusVoo == StatusVoo.EM_VOO || voo.statusVoo == StatusVoo.FINALIZADO) {
                        throw new IllegalArgumentException("não foi possivel cancelar o voo pois ele ja esta no ar ou finalizado");
                    } else {
                        voo.setStatusVoo(StatusVoo.CANCELADO);
                        System.out.println("o voo " + voo.getNumeroVoo() + " foi cancelado");
                    }
                    break;
                case 2:
                    System.out.println("se vc deseja cancelar outro voo, tente novamente");
                    break;
                default:
                    System.out.println("opção invalida, por favor digite novamente");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (java.util.InputMismatchException e) {
            System.out.println("entrada invalida, por favor digite novamente");
            input.nextLine();
        }
    }


    public void alterarStatusVoo(Voo voo) throws VooNaoEncontradoException {
        input.nextLine();
        System.out.println("====== DETALHES DO VOO ======");
        voo.exibirDetalhesVoo();
        System.out.println("1 - sim");
        System.out.println("2 - não");
        System.out.print("esse é o voo que vc deseja alterar o status?");
        int opcao = input.nextInt();

        switch (opcao) {
            case 1:
                System.out.println("1 - AGENDADO");
                System.out.println("2 - EMBARCANDO");
                System.out.println("3 - EM VOO");
                System.out.println("4 - ATRASADO");
                System.out.println("5 - CANCELADO");
                System.out.println("6 - FINALIZADO");
                System.out.println("--------------------");
                System.out.print("qual desses status vc deseja definir no voo " + voo.numeroVoo);
                int opcao2 = input.nextInt();

                if (opcao2 == 1) {
                    voo.setStatusVoo(StatusVoo.AGENDADO);
                    System.out.println("status do voo mudado para AGENDADO");
                } else if (opcao2 == 2) {
                    voo.setStatusVoo(StatusVoo.EMBARCANDO);
                    System.out.println("status do voo mudado para EMBARCANDO");
                } else if (opcao2 == 3) {
                    voo.setStatusVoo(StatusVoo.EM_VOO);
                    System.out.println("status do voo mudado para EM VOO");
                } else if (opcao2 == 4) {
                    voo.setStatusVoo(StatusVoo.ATRASADO);
                    System.out.println("status do voo mudado para ATRASADO");
                } else if (opcao2 == 5) {
                    voo.setStatusVoo(StatusVoo.CANCELADO);
                    System.out.println("status do voo mudado para CANCELADO");
                } else if (opcao2 == 6) {
                    voo.setStatusVoo(StatusVoo.FINALIZADO);
                    System.out.println("sttaus do voo mudado para finalizado");
                } else {
                    System.out.println("opcao invalida, por favor tente novamente");
                }
                break;
            case 2:
                System.out.println("se vc deseja mudar o status de outro voo, digite novamente");
                break;
            default:
                System.out.println("opcao invalida, por favor tente novamente");
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
