import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Aeroporto {
    Scanner input = new Scanner(System.in);

    Map<Integer, Aeronave> aeronaves = new HashMap<>();
    Set<Integer> numRegistro = new HashSet<>();
    Map<Integer, Voo> voosDoAeroporto = new HashMap<>();
    Set<Integer> numVoo = new HashSet<>();
    Set<String> cpf = new HashSet<>();
    Map<String, Passageiros> passageirosCadastrados = new HashMap<>();

    // MENU DE ADMINISTRACAO

    public void cadastrarAeronaveAeroporto() throws NumeroRegistroDuplicadoException {
        try {
            System.out.print("qual o numero de registro da aeronave?");
            int numeroRegistro = input.nextInt();

            input.nextLine();

            System.out.print("qual o modelo da aeronave que vc deseja cadastrar?");
            String modelo = input.nextLine();

            System.out.print("qual a capacidade maxima dessa aeronave?");
            long capacidadeMaxima = input.nextLong();

            Aeronave aeronave = new Aeronave(modelo, capacidadeMaxima, numeroRegistro);

            if (!numRegistro.add(numeroRegistro)) {
                throw new NumeroRegistroDuplicadoException("Não foi possivel cadastrar a aeronave pois o numero de registro esta duplicado");
            } else {
                aeronaves.put(aeronave.getNumeroRegistro(), aeronave);
                numRegistro.add(aeronave.getNumeroRegistro());
                System.out.println("Aeronave do modelo " + aeronave.getModelo() + " do numero " + aeronave.getNumeroRegistro() + " foi registrada no sistema");
            }
        } catch (NumeroRegistroDuplicadoException e) {
            System.out.println(e.getMessage());
        } catch (java.util.InputMismatchException e) {
            System.out.println("entrada invalida, por favor digite novamente");
            input.nextLine();
        }
    }

    public void removerAeronaveAeroporto() throws AeronaveNaoEncontradaException {
        try {
            if (aeronaves.isEmpty()) {
                System.out.println("não foi possivel remover uma aeronave pois não há nenhuma cadastrada");
            } else {
                System.out.print("qual o numero de registro da aeronave que vc deseja remover do sistema");
                int num = input.nextInt();

                Aeronave aeronave = aeronaves.values().stream()
                        .filter(a -> a.getNumeroRegistro() == num)
                        .findFirst()
                        .orElseThrow(() -> new AeronaveNaoEncontradaException("nao foi possivel remover a aeronave, pois ela não foi encontrada"));

                aeronaves.remove(aeronave.getNumeroRegistro(), aeronave);
                numRegistro.remove(aeronave.getNumeroRegistro());
                System.out.println("Aeronave do numero " + aeronave.getNumeroRegistro() + " removido do sistema");
            }
        } catch (AeronaveNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (java.util.InputMismatchException e) {
            System.out.println("entrada invalida, por favor digite novamente");
            input.nextLine();
        }
    }

    public void listarAeronaves() {
        if (aeronaves.isEmpty()) {
            System.out.println("não foi possivel listar as aeronaves, pois a lista de aeronaves do sistema esta vazia");
        } else {
            System.out.println("====== LISTA DE AERONAVES ======");
            for (Aeronave aeronave : aeronaves.values()) {
                aeronave.exibirDetalhesAeronave();
            }
        }
    }

    public void cadastrarVooAeroporto() throws AeronaveNaoEncontradaException, VooDuplicadoException {
        if (aeronaves.isEmpty()) {
            System.out.println("não foi possivel cadastrar um voo pois não ha aeronaves cadastradas");
        } else {
            System.out.print("qual o numero do voo que vc deseja cadastrar?");
            int numeroVoo = input.nextInt();

            input.nextLine();

            System.out.println("1 - são paulo");
            System.out.println("2 - rio de janeiro");
            System.out.println("3 - florianopolis");
            System.out.println("4 - goias");
            System.out.println("5 - belo horizonte");
            System.out.println("----------------------");
            System.out.print("qual desses destinos o voo seguirá");
            int opcao = input.nextInt();

            Localidade destinoVoo = switch (opcao) {
                case 1 -> Localidade.SAO_PAULO;
                case 2 -> Localidade.RIO_DE_JANEIRO;
                case 3 -> Localidade.FLORIANOPOLIS;
                case 4 -> Localidade.GOIAS;
                case 6 -> Localidade.BELO_HORIZONTE;
                default -> throw new IllegalArgumentException("opcao invalida, por favor digite novamente");
            };

            System.out.print("qual a data e o horario de partida?");
            String dataHorarioPartida = input.nextLine().trim();

            System.out.print("qual a data e o horario de chegada?");
            String dataHorarioChegada = input.nextLine().trim();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
            LocalDateTime dataConvertidaPartida = LocalDateTime.parse(dataHorarioPartida, formatter);

            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
            LocalDateTime dataConvertidaChegada = LocalDateTime.parse(dataHorarioChegada, formatter2);

            System.out.print("qual o numero da aeronave que ira realizar o voo?");
            int num = input.nextInt();

            Aeronave aeronave = aeronaves.values().stream()
                    .filter(a -> a.getNumeroRegistro() == num)
                    .findFirst()
                    .orElseThrow(() -> new AeronaveNaoEncontradaException("não foi possivel cadastrar voo, pois não há nenhuma aeronave cadastrada no aeroporto"));

            Voo voo = new Voo(numeroVoo, "VITORIA", destinoVoo, dataConvertidaPartida, dataConvertidaChegada, StatusVoo.AGENDADO, aeronave);

            if (!numVoo.add(voo.getNumeroVoo())) {
                throw new VooDuplicadoException("não foi possivel cadastrar voo pois o numero de voo esta duplicado");
            } else {
                voosDoAeroporto.put(voo.getNumeroVoo(), voo);
                numVoo.add(voo.getNumeroVoo());
                associarVooAeronave(voo);
                System.out.println("Voo do numero " + voo.getNumeroVoo() + " registrado no sistema do aeroporto");
            }
        }
    }

    private void associarVooAeronave(Voo voo) throws NumeroVooDuplicadoException {
        if (!numVoo.add(voo.getNumeroVoo())) {
            throw new NumeroVooDuplicadoException("nao foi possivel associar o voo com a aeronave pois o numero de voo esta duplicado");
        } else {
            voo.getAeronaveAssociada().voosDaAeronave.put(voo.getNumeroVoo(), voo);
            System.out.println("Voo do numero " +voo.getNumeroVoo()+ " com destino a " +voo.getDestinoVoo()+ " foi associado a aeronave de numero " +voo.getAeronaveAssociada().getNumeroRegistro());
        }
    }

    public void cancelarVooAeroporto() throws VooNaoEncontradoException {
        if (voosDoAeroporto.isEmpty()) {
            System.out.println("nã foi possivel cancelar um voo do aeroporto pois não há ennhum registrado");
        } else {
            System.out.println("qual o numero do voo que vc deseja cancelar?");
            int num = input.nextInt();

            Voo voo = voosDoAeroporto.values().stream()
                    .filter(a -> a.getNumeroVoo() == num)
                    .findFirst()
                    .orElseThrow(() -> new VooNaoEncontradoException("não foi possivel cancelar o voo, pois este voo não foi encontrado nos sistema"));

            voo.cancelarVoo(voo);
        }
    }

    public void alterarStatusVooAeroporto() throws VooNaoEncontradoException {
        if (voosDoAeroporto.isEmpty()) {
            System.out.println("não foi possivel alterar o status de um voo pois não há nenhum reistrado");
        } else {
            System.out.print("qual o numero do voo que vc deseja alterar o status?");
            int num = input.nextInt();

            Voo voo = voosDoAeroporto.values().stream()
                    .filter(a -> a.getNumeroVoo() == num)
                    .findFirst()
                    .orElseThrow(() -> new VooNaoEncontradoException("nao foi possivel alterar o status do voo, pois ele nao foi encontrado no sistema"));

            voo.alterarStatusVoo(voo);
        }
    }


    public void listarVooPorCaracteristica() throws VooNaoEncontradoException {
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
                    String dataVoo = input.nextLine().trim();

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

    private void verificarDisponibilidadeAeronave() throws VooNaoEncontradoException {
        System.out.print("qual o numero voo vc deseja verificar a disponibilidade de assento?");
        int num = input.nextInt();

        Voo voo = voosDoAeroporto.values().stream()
                .filter(a -> a.getNumeroVoo() == num)
                .findFirst()
                .orElseThrow(() -> new VooNaoEncontradoException("não foi possivel verificar a disponibilidade de assento do voo " +num+ " pois este voo não foi encontrado"));

        voo.verificarDisponibilidadeAssento(voo);
    }

    public void cadastrarPassageirosAeroporto() throws CpfDuplicadoException, CpfInvalidoException {
        System.out.print("qual o nome do passageiro?");
        String nomePassageiro = input.nextLine();

        System.out.print("qual o cpf do passageiro?");
        String cpfPassageiro = input.nextLine().trim();

        System.out.print("qual o numero do passaporte do passageiro?");
        String numeroPassaporte = input.nextLine().trim();

        Passageiros passageiro = new Passageiros(nomePassageiro, cpfPassageiro.replaceAll("\\d", ""), numeroPassaporte.replaceAll("\\d", ""));

        if (!cpf.add(passageiro.getCpfPassageiro())) {
            throw new CpfDuplicadoException("não foi possivel adicionar o passageiro a lista de passageiros, pois o cpf esta duplicado");
        } else {
            passageirosCadastrados.put(passageiro.getCpfPassageiro(), passageiro);
            cpf.add(passageiro.getCpfPassageiro());
            System.out.println("passageiro " +passageiro.getNomePassageiro()+ " adicionado ao aeroporto");
        }
    }

    public void removerPassageiroAeroporto() throws PassageiroNaoEncontradoException {
        if (passageirosCadastrados.isEmpty()) {
            System.out.println("não há passageiros cadastrados no aeroporto");
        } else {
            System.out.print("qual o cpf do passageiro que vc deseja remover do voo?");
            String cpfPessoa = input.nextLine();

            Passageiros passageiro = passageirosCadastrados.values().stream()
                    .filter(a -> a.getCpfPassageiro().equalsIgnoreCase(cpfPessoa))
                    .findFirst()
                    .orElseThrow(() -> new PassageiroNaoEncontradoException("não foi possivel remover o passageiro pois ele não foi encontrado"));

            System.out.println("====== DETALHES DO PASSAGEIRO ======");
            passageiro.exibirDetalhesPassageiro();

            System.out.println("1 - sim");
            System.out.println("2 - não");
            System.out.println("----------------------------");
            System.out.print("esse é o passageiro que vc deseja remover?");
            int opcao = input.nextInt();

            switch (opcao) {
                case 1:
                    passageirosCadastrados.remove(passageiro.getCpfPassageiro(), passageiro);
                    cpf.remove(passageiro.getCpfPassageiro());
                    System.out.println("passageiro " + passageiro.getNomePassageiro() + " removido do aeroporto");
                    break;
                case 2:
                    System.out.println("se deseja remover um passageiro, por favor insira novamente");
                    break;
                default:
                    System.out.println("opcao invalida, por favor digite novamente");
            }
        }
    }

    public void consultarInformacoesPassageiro() throws PassageiroNaoEncontradoException {
        if (passageirosCadastrados.isEmpty()) {
            System.out.println("não foi possivel consultar as informações de um passageiro pois nenhum foi encontrado");
        } else {
            System.out.print("qual o cpf do passageiro que vc deseja consultar infromacoes?");
            String cpf = input.nextLine();

            Passageiros passageiro = passageirosCadastrados.values().stream()
                    .filter(a -> a.getCpfPassageiro().equalsIgnoreCase(cpf))
                    .findFirst()
                    .orElseThrow(() -> new PassageiroNaoEncontradoException("não foi possivel consultar as informações do passageiro, pois ele não foi encontrado"));

            System.out.println("===== DETALHES DO PASSAGEIRO =====");
            passageiro.exibirDetalhesPassageiro();
        }
    }

    // MENU DE PASSAGEIRO

    public void realizarCheckIn() throws PassageiroNaoEncontradoException, VooNaoEncontradoException, VagasIndisponiveisException {
        double precoPassagem;
        double precoDistancia;
        double precoDia;
        double precoHora;

        System.out.print("informe o seu cpf:");
        String cpf = input.nextLine().trim();

        System.out.print("informe o numero do voo que vc ira embarcar");
        int numeroVoo = input.nextInt();

        Passageiros passageiro = passageirosCadastrados.values().stream()
                .filter(a -> a.getCpfPassageiro().equalsIgnoreCase(cpf))
                .findFirst()
                .orElseThrow(() -> new PassageiroNaoEncontradoException("não foi possivel cadastrar o passageiro no voo pois o cpf nao foi encontrado"));

        Voo voo = voosDoAeroporto.values().stream()
                .filter(a -> a.getNumeroVoo() == numeroVoo)
                .filter(b -> b.getStatusVoo() == StatusVoo.AGENDADO)
                .findFirst()
                .orElseThrow(() -> new VooNaoEncontradoException("não foi possivel o passageiro embarcar no voo pois o numero do voo não foi encontrado |OU| o voo ja passou do agendamento"));

        verificarDisponibilidadeAeronave();

        System.out.print("qual o numero da passagem?");
        int numeroPassagem = input.nextInt();

        if (voo.getDestinoVoo().getDistancia() < 600) {
            precoDistancia = 41.89;
        } else if (voo.getDestinoVoo().getDistancia() < 1200) {
            precoDistancia = 60.87;
        } else {
            precoDistancia = 79.49;
        }

        DayOfWeek dia = voo.getDataHorarioPartida().getDayOfWeek();

        if (dia.getValue() == 2 || dia.getValue() == 3 || dia.getValue() == 4) {
            precoDia = 19.99;
        } else if (dia.getValue() == 6 || dia.getValue() == 7 || dia.getValue() == 5) {
            precoDia = 35.85;
        } else {
            precoDia = 23.94;
        }

        int horaVoo = voo.getDataHorarioPartida().getHour();

        if (horaVoo >= 17 && horaVoo <= 21) {
            precoHora = 50;
        } else if (horaVoo > 0 && horaVoo <= 6) {
            precoHora = 22;
        } else if (horaVoo > 6 && horaVoo <= 13) {
            precoHora = 34;
        } else if (horaVoo > 13 && horaVoo < 17) {
            precoHora = 41;
        } else {
            precoHora = 28;
        }

        precoPassagem = precoDia + precoDistancia + precoHora;

        Passagem passagem = new Passagem(numeroPassagem, passageiro, voo, precoPassagem);
        passageiro.passageirosVoosCadastrados.add(voo);
        passageiro.passagensDoPassageiro.add(passagem);
        System.out.println("passageiro " +passageiro.getNomePassageiro()+ " registrado no voo " +voo.getNumeroVoo()+ " com destino a " +voo.getDestinoVoo());
    }

    public void realizarEmbarque() {
        System.out.print("qual o numero da passagem?");
        int numPassagem = input.nextInt();

        input.nextLine();

        System.out.print("qual o numero do passaporte?");
        String numPassaporte = input.nextLine();

        Passageiros passageiro = passageirosCadastrados.values().stream()
                .filter(a -> a.getNumeroPassaporte().equalsIgnoreCase(numPassaporte))
                .findFirst()
                .orElseThrow(() -> new PassageiroNaoEncontradoException("não foi possivel realizar embarque pois nenhum passageiro foi encontrado com esse numero de passaporte"));

        Passagem passagem = passageiro.passagensDoPassageiro.stream()
                .filter(a -> a.getNumeroPassagem() == numPassagem)
                .filter(b -> b.getVooPassagem().getStatusVoo() == StatusVoo.EMBARCANDO)
                .filter(c -> c.getPassageiro().getNumeroPassaporte().equalsIgnoreCase(passageiro.getNumeroPassaporte()))
                .findFirst()
                .orElseThrow(() -> new PassagemNaoEncontradaException("não foi possivel realizar embarque pois o numero da passagem não foi encontrado |OU| o voo ja não esta mais embarcando |OU| o numero da passagem fornecida não corresponde ao passageiro fornecido"));

        passagem.getVooPassagem().passageirosNoVoo.put(passagem.getNumeroPassagem(), passagem);
    }

    public void consultarSeusVoos() {
        if (passageirosCadastrados.isEmpty()) {
            System.out.println("não foi possivel consultar voos, pois não há nenhum passageiro cadastrado");
        } else {
            System.out.print("qual o numero do seu passaporte?");
            String numPassaporte = input.nextLine();

            Passageiros passageiro = passageirosCadastrados.values().stream()
                    .filter(a -> a.getNumeroPassaporte().equalsIgnoreCase(numPassaporte))
                    .findFirst()
                    .orElseThrow(() -> new PassaporteNaoEncontradoException("não foi possivel consultar seus voos, pois seu passaporte não foi encontrado no sistema"));

            System.out.println("===== DETALHES DO PASSAGEIRO =====");
            passageiro.exibirDetalhesPassageiro();

            System.out.println("1 - sim");
            System.out.println("2 - não");
            System.out.print("esse é o seu perfil?");
            int opcao = input.nextInt();

            switch (opcao) {
                case 1:
                    if (passageiro.passageirosVoosCadastrados.isEmpty()) {
                        System.out.println("você não foi registrado em nenhum voo");
                    } else {
                        System.out.println("===== DETALHES DOS VOOS DO PASSAGEIRO " +passageiro.getNomePassageiro().toUpperCase()+ " =====");
                        for (Voo voos : passageiro.passageirosVoosCadastrados) {
                            voos.exibirDetalhesVoo();
                        }
                    }
                    break;
                case 2:
                    System.out.println("se vc deseja consultar seus voos corretamente, por favor tente realizar a operação novamente");
                    break;
                default:
                    System.out.println("opcao invalida, por favor digite novamente");
            }
        }
    }
}
