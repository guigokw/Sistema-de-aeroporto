import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            voo.aeronaveAssociada.associarVooAeronave(voo);
            System.out.println("Voo do numero " +voo.getNumeroVoo()+ " registrado no sistema do aeroporto");
        }
    }

    public void cancelarVoo() throws VooNaoEncontradoException, IllegalArgumentException {
        if (voosDoAeroporto.isEmpty()) {
            System.out.println("nã foi possivel cancelar um voo do aeroporto pois não há ennhum registrado");
        } else {
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
        }
    }

    public void alterarStatusVoo() throws VooNaoEncontradoException {
        if (voosDoAeroporto.isEmpty()) {
            System.out.println("não foi possivel alterar o status de um voo pois não há nenhum reistrado");
        } else {
            System.out.print("qual o numero do voo que vc deseja alterar o status?");
        int num = input.nextInt();

        Voo voo = voosDoAeroporto.values().stream()
                .filter(a -> a.getNumeroVoo() == num)
                .findFirst()
                .orElseThrow(() -> new VooNaoEncontradoException("nao foi possivel alterar o status do voo, pois ele nao foi encontrado no sistema"));

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
    }

    public void listarVooPorCaracteristica() {
        if (voosDoAeroporto.isEmpty()) {
            System.out.println("nao foi possivel listar voos pois não há nenhum registrado no sistema");
        } else {
            System.out.println("1 - status");
            System.out.println("2 - origem");
            System.out.println("3 - destino");
            System.out.println("4 - data");
            System.out.println("-------------------");
            System.out.print("qual dessas caracteristicas de voo vc deseja fazer uma listagem?");
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
                    System.out.print("qual desses status de voo vc deseja listar os voos?");
                    int opcao2 = input.nextInt();

                    if (opcao2 == 1) {
                        Voo voo = voosDoAeroporto.values().stream()
                                .filter(a -> a.getStatusVoo() == StatusVoo.AGENDADO)
                                .findAny()
                                .orElseThrow(() -> new VooNaoEncontradoException("não foi possivel listar voos agendados, pois não há nenhum"));

                        System.out.println("===== DETALHES DOS VOO AGENDADO ======");
                        voo.exibirDetalhesVoo();
                    } else if (opcao2 == 2) {
                        Voo voo = voosDoAeroporto.values().stream()
                                .filter(a -> a.getStatusVoo() == StatusVoo.EMBARCANDO)
                                .findAny()
                                .orElseThrow(() -> new VooNaoEncontradoException("nao foi possivel listar os voos embarcando pois não há nenhum"));

                        System.out.println("===== DETALHES DOS VOOS EMBARCANDO ======");
                        voo.exibirDetalhesVoo();
                    } else if (opcao2 == 3) {
                        Voo voo = voosDoAeroporto.values().stream()
                                .filter(a -> a.getStatusVoo() == StatusVoo.EM_VOO)
                                .findAny()
                                .orElseThrow(() -> new VooNaoEncontradoException("não foi possivel listar os voos em voo pois nenhum foi encontrado"));

                        System.out.println("===== DETALHES DOS VOOS EM VOO =====");
                        voo.exibirDetalhesVoo();
                    } else if (opcao2 == 4) {
                        Voo voo = voosDoAeroporto.values().stream()
                                .filter(a -> a.getStatusVoo() == StatusVoo.ATRASADO)
                                .findAny()
                                .orElseThrow(() -> new VooNaoEncontradoException("não foi possivel listar os voos atrasados pois nenhum foi registrado"));

                        System.out.println("===== DETALHES DOS VOOS ATRASADOS ======");
                        voo.exibirDetalhesVoo();
                    } else if (opcao2 == 5) {
                        Voo voo = voosDoAeroporto.values().stream()
                                .filter(a -> a.getStatusVoo() == StatusVoo.CANCELADO)
                                .findAny()
                                .orElseThrow(() -> new VooNaoEncontradoException("não foi possivel listar os voos cancelados, pois nenhum foi registrado"));

                        System.out.println("====== DETALHES DOS VOOS CANCELADOS ======");
                        voo.exibirDetalhesVoo();
                    } else if (opcao2 == 6) {
                        Voo voo = voosDoAeroporto.values().stream()
                                .filter(a -> a.getStatusVoo() == StatusVoo.FINALIZADO)
                                .findAny()
                                .orElseThrow(() -> new VooNaoEncontradoException("não foi possivel listar os voos finalizados, pois nenhum foi registrado"));

                        System.out.println("====== DETALHES DOS VOOS FINALIZADOS ======");
                        voo.exibirDetalhesVoo();
                    } else {
                        System.out.println("opcao invalida, por favor digite novamente");
                    }
                    break;
                case 2:
                    System.out.print("qual a origem dos voos que vc deseja listar?");
                    String origem = input.nextLine();

                    Voo voo = voosDoAeroporto.values().stream()
                            .filter(a -> a.getOrigemVoo().equalsIgnoreCase(origem))
                            .findAny()
                            .orElseThrow(() -> new VooNaoEncontradoException("não foi possivel listar os voos com origem " +origem+ " pois nenhum foi encontrado"));

                    System.out.println("====== DETALHES DOS VOOS COM ORIGEM DE " +origem.toUpperCase()+ " ======");
                    voo.exibirDetalhesVoo();
                    break;
                case 3:
                    System.out.print("qual o destino dos voos que vc deseja listar?");
                    String destino = input.nextLine();

                    Voo voo2 = voosDoAeroporto.values().stream()
                            .filter(a -> a.getOrigemVoo().equalsIgnoreCase(destino))
                            .findAny()
                            .orElseThrow(() -> new VooNaoEncontradoException("não foi possivel listar os voos com origem " +destino+ " pois nenhum foi encontrado"));

                    System.out.println("====== DETALHES DOS VOOS COM ORIGEM DE " +destino.toUpperCase()+ " =====");
                    voo2.exibirDetalhesVoo();
                    break;
                case 4:
                    System.out.print("qual a data dos voos que vc deseja listar");
                    String dataVoo = input.nextLine();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDateTime dataConvertida = LocalDateTime.parse(dataVoo, formatter);

                    Voo voo3 =  voosDoAeroporto.values().stream()
                            .filter(a -> a.getDataHorarioChegada().equals(dataConvertida))
                            .filter(b -> b.getDataHorarioPartida().equals(dataConvertida))
                            .findAny()
                            .orElseThrow(() -> new VooNaoEncontradoException("não foi posivel listar os voos do dia " +dataConvertida+ " pois nenhuma foi registrada"));

                    System.out.println("===== DETALHES DOS VOOS DO DIA " +dataConvertida+ " ======");
                    voo3.exibirDetalhesVoo();
                    break;
                default:
                    System.out.println("opcao invalida, por favor digite novamente");
            }
        }
    }

    public void verificarDisponibilidadeAssento() {
        System.out.print("qual o numero voo vc deseja verificar a disponibilidade de assento?");
        int num = input.nextInt();

        Voo voo = voosDoAeroporto.values().stream()
                .filter(a -> a.getNumeroVoo() == num)
                .findFirst()
                .orElseThrow(() -> new VooNaoEncontradoException("não foi possivel verificar a disponibilidade de assento do voo " +num+ " pois este voo não foi encontrado"));

        long total = voo.passageirosNoVoo.values().size();

        long assentosTotais = voo.aeronaveAssociada.getCapacidadeMaxima();
        long assentosRestantes = assentosTotais - total;

        if (assentosTotais > assentosRestantes) {
            System.out.println("ainda há " +assentosRestantes+ " assentos restantes no voo " +voo.getNumeroVoo());
        } else {
            System.out.println("não há mais vagas no voo " +voo.getNumeroVoo());
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
