import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Aeroporto {
    Scanner input = new Scanner(System.in);

    Map<Integer, Aeronave> aeronaves = new HashMap<>();
    Set<Integer> numRegistro = new HashSet<>();
    Map<Integer, Voo> voosDoAeroporto = new HashMap<>();
    Set<Integer> numVoo = new HashSet<>();
    Set<String> cpf = new HashSet<>();
    Map<String, Passageiros> passageirosCadastrados = new HashMap<>();
    Set<Integer> numPassagem = new HashSet<>();

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
        } catch (NumeroRegistroDuplicadoException | IllegalArgumentException | NumeroRegistroInvalidoException |
                 CapacidadeMaximaInvalida e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
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
                System.out.println("Aeronave " + aeronave.getModelo() + " do numero " + aeronave.getNumeroRegistro() + " removido do sistema");
            }
        } catch (AeronaveNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
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
        try {
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

                input.nextLine();

                Localidade destinoVoo = switch (opcao) {
                    case 1 -> Localidade.SAO_PAULO;
                    case 2 -> Localidade.RIO_DE_JANEIRO;
                    case 3 -> Localidade.FLORIANOPOLIS;
                    case 4 -> Localidade.GOIAS;
                    case 5 -> Localidade.BELO_HORIZONTE;
                    default -> throw new IllegalArgumentException("opcao invalida, por favor digite novamente");
                };

                System.out.print("qual a data e o horario de partida?");
                String dataPartida = input.nextLine().trim();

                System.out.print("qual a data e o horario de chegada?");
                String dataChegada = input.nextLine().trim();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime dataConvertidaPartida = LocalDateTime.parse(dataPartida, formatter);

                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime dataConvertidaChegada = LocalDateTime.parse(dataChegada, formatter2);

                if (dataConvertidaChegada.isBefore(dataConvertidaPartida)) {
                    throw new DataInvalidaException("não foi possivel cadastrar o voo a data de chegada foi registrada antes da data de partida");
                }

                System.out.print("qual o numero da aeronave que ira realizar o voo?");
                int num = input.nextInt();

                Aeronave aeronave = aeronaves.values().stream()
                        .filter(a -> a.getNumeroRegistro() == num)
                        .findFirst()
                        .orElseThrow(() -> new AeronaveNaoEncontradaException("não foi possivel cadastrar voo, pois não há nenhuma aeronave com esse numero cadastrada no aeroporto"));

                Voo voo = new Voo(numeroVoo, "VITORIA", destinoVoo, dataConvertidaPartida, dataConvertidaChegada, StatusVoo.AGENDADO, aeronave);

                if (!numVoo.add(voo.getNumeroVoo())) {
                    throw new VooDuplicadoException("não foi possivel cadastrar voo pois o numero de voo esta duplicado");
                } else {
                    voosDoAeroporto.put(voo.getNumeroVoo(), voo);
                    numVoo.add(voo.getNumeroVoo());
                    voo.getAeronaveAssociada().voosDaAeronave.put(voo.getNumeroVoo(), voo);
                    System.out.println("Voo do numero " + voo.getNumeroVoo() + " registrado no sistema do aeroporto");
                    System.out.println("Voo do numero " + voo.getNumeroVoo() + " com destino a " + voo.getDestinoVoo() + " foi associado a aeronave de numero " + voo.getAeronaveAssociada().getNumeroRegistro());
                }
            }

        } catch (VooDuplicadoException | AeronaveNaoEncontradaException | IllegalArgumentException |
                 DataInvalidaException e) {
            System.out.println(e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("não foi possivel cadastrar voo, pois o formato de data esta incorreto");
        } catch (InputMismatchException e) {
            System.out.println("entrada invalida, por favor digite novamente");
            input.nextLine();
        }
    }

    public void cancelarVooAeroporto() throws VooNaoEncontradoException {
        try {
            if (voosDoAeroporto.isEmpty()) {
                System.out.println("nã foi possivel cancelar um voo do aeroporto pois não há ennhum registrado");
            } else {
                System.out.print("qual o numero do voo que vc deseja cancelar?");
                int num = input.nextInt();

                Voo voo = voosDoAeroporto.values().stream()
                        .filter(a -> a.getNumeroVoo() == num)
                        .findFirst()
                        .orElseThrow(() -> new VooNaoEncontradoException("não foi possivel cancelar o voo, pois este voo não foi encontrado nos sistema"));

                voo.cancelarVoo(voo);
            }
        } catch (VooNaoEncontradoException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("entrada invalida, por favor digite novamente");
            input.nextLine();
        }
    }

    public void alterarStatusVooAeroporto() throws VooNaoEncontradoException {
        try {
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
        } catch (VooNaoEncontradoException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("entrada invalida, por favor digite novamente");
            input.nextLine();
        }
    }


    public void listarVooPorCaracteristica() throws VooNaoEncontradoException {
        try {
            if (voosDoAeroporto.isEmpty()) {
                System.out.println("nao foi possivel listar voos pois não há nenhum registrado no sistema");
            } else {
                System.out.println("1 - status");
                System.out.println("2 - destino");
                System.out.println("3 - data");
                System.out.println("-------------------");
                System.out.print("qual dessas caracteristicas de voo vc deseja fazer uma listagem?");
                int opcao = input.nextInt();

                input.nextLine();

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

                        StatusVoo statusVoo = switch (opcao2) {
                            case 1 -> StatusVoo.AGENDADO;
                            case 2 -> StatusVoo.EMBARCANDO;
                            case 3 -> StatusVoo.EM_VOO;
                            case 4 -> StatusVoo.ATRASADO;
                            case 5 -> StatusVoo.CANCELADO;
                            case 6 -> StatusVoo.FINALIZADO;
                            default -> throw new IllegalArgumentException("opção invalida, por favor tente novamente");
                        };
                           List<Voo> voo = voosDoAeroporto.values().stream()
                                    .filter(a -> a.getStatusVoo() == statusVoo)
                                    .toList();

                           if (voo.isEmpty()) {
                               throw new VooNaoEncontradoException("não foi possivel listar voos " +statusVoo+ ", pois não há nenhum registrado");
                           } else {
                               System.out.println("===== DETALHES DOS VOOS " +statusVoo+ " ======");
                               for (Voo voos : voo) {
                                   voos.exibirDetalhesVoo();
                               }
                           }
                           break;
                    case 2:
                        System.out.println("1 - são paulo");
                        System.out.println("2 - rio de janeiro");
                        System.out.println("3 - florianopolis");
                        System.out.println("4 - goias");
                        System.out.println("5 - belo horizonte");
                        System.out.print("qual o destino dos voos que vc deseja listar?");
                        int opcao3 = input.nextInt();

                        Localidade local = switch (opcao3) {
                            case 1 -> Localidade.SAO_PAULO;
                            case 2 -> Localidade.RIO_DE_JANEIRO;
                            case 3 -> Localidade.FLORIANOPOLIS;
                            case 4 -> Localidade.GOIAS;
                            case 5 -> Localidade.BELO_HORIZONTE;
                            default -> throw new IllegalArgumentException("opcao invalida, por favor digite novamente");
                        };

                        List<Voo> voo2 = voosDoAeroporto.values().stream()
                                .filter(a -> a.getDestinoVoo() == local)
                                .toList();

                        if (voo2.isEmpty()) {
                        throw new VooNaoEncontradoException("não foi possivel listar os voos com destino " + local + " pois nenhum foi encontrado");
                        } else {
                            System.out.println("====== DETALHES DOS VOOS COM DESTINO DE " + local + " =====");
                            for (Voo voos : voo2) {
                                voos.exibirDetalhesVoo();
                            }
                        }
                        break;
                    case 3:
                        System.out.print("qual a data dos voos que vc deseja listar");
                        String dataVoo = input.nextLine().trim();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        LocalDate dataConvertida = LocalDate.parse(dataVoo, formatter);

                        List<Voo> voo3 = voosDoAeroporto.values().stream()
                                .filter(a -> a.getDataHorarioChegada().toLocalDate().isEqual(ChronoLocalDate.from(dataConvertida)))
                                .filter(b -> b.getDataHorarioPartida().toLocalDate().isEqual(ChronoLocalDate.from(dataConvertida)))
                                .toList();


                        if (voo3.isEmpty()) {
                           throw new VooNaoEncontradoException("não foi posivel listar os voos do dia " + dataConvertida + " pois nenhuma foi registrada");
                        } else {
                            System.out.println("===== DETALHES DOS VOOS DO DIA " + dataConvertida + " ======");
                            for (Voo voos : voo3) {
                                voos.exibirDetalhesVoo();
                            }
                        }
                        break;
                    default:
                        System.out.println("opcao invalida, por favor digite novamente");
                }
            }
        } catch (VooNaoEncontradoException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("entrada invalida, por favor digite novamente");
            input.nextLine();
        } catch (DateTimeParseException e) {
            System.out.println("não foi possivel encontrar o voo pois o formato de data inserido esta incorreto");
        }
    }

    public void cadastrarPassageirosAeroporto() throws CpfDuplicadoException, CpfInvalidoException {
        try {
            input.nextLine();

            System.out.print("qual o nome do passageiro?");
            String nomePassageiro = input.nextLine();

            System.out.print("qual o cpf do passageiro?");
            String cpfPassageiro = input.nextLine().trim();

            System.out.print("qual o numero do passaporte do passageiro?");
            String numeroPassaporte = input.nextLine().trim();

            Passageiros passageiro = new Passageiros(nomePassageiro, cpfPassageiro.replaceAll("[a-zA-Z]", "").strip(), numeroPassaporte.replaceAll("[a-zA-Z]", "").strip());

            if (!cpf.add(passageiro.getCpfPassageiro())) {
                throw new CpfDuplicadoException("não foi possivel adicionar o passageiro a lista de passageiros, pois o cpf esta duplicado");
            } else {
                passageirosCadastrados.put(passageiro.getCpfPassageiro(), passageiro);
                cpf.add(passageiro.getCpfPassageiro());
                System.out.println("passageiro " + passageiro.getNomePassageiro() + " adicionado ao aeroporto");
            }
        } catch (CpfDuplicadoException | IllegalArgumentException | CpfInvalidoException |
                 PassaporteInvalidoException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("entrada invalida, por favor digite novamente");
            input.nextLine();
        }
    }

    public void removerPassageiroAeroporto() throws PassageiroNaoEncontradoException {
        try {
            if (passageirosCadastrados.isEmpty()) {
                System.out.println("não há passageiros cadastrados no aeroporto");
            } else {
                input.nextLine();

                System.out.print("qual o cpf do passageiro que vc deseja remover do aeroporto?");
                String cpfPessoa = input.nextLine().trim();

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
        } catch (PassageiroNaoEncontradoException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("entrada invalida, por favor digite novamente");
            input.nextLine();
        }
    }

    public void consultarInformacoesPassageiro() throws PassageiroNaoEncontradoException {
        try {
            if (passageirosCadastrados.isEmpty()) {
                System.out.println("não foi possivel consultar as informações de um passageiro pois nenhum foi encontrado");
            } else {
                System.out.print("qual o cpf do passageiro que vc deseja consultar informacoes?");
                String cpf = input.nextLine().trim();

                input.nextLine();

                Passageiros passageiro = passageirosCadastrados.values().stream()
                        .filter(a -> a.getCpfPassageiro().equalsIgnoreCase(cpf))
                        .findFirst()
                        .orElseThrow(() -> new PassageiroNaoEncontradoException("não foi possivel consultar as informações do passageiro, pois ele não foi encontrado"));

                System.out.println("===== DETALHES DO PASSAGEIRO =====");
                passageiro.exibirDetalhesPassageiro();
            }
        } catch (PassageiroNaoEncontradoException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("entrada invalida, por favor digite novamente");
            input.nextLine();
        }
    }

    public void realizarCheckIn() throws PassageiroNaoEncontradoException, VooNaoEncontradoException, VagasIndisponiveisException {
        try {
            if (passageirosCadastrados.isEmpty()) {
                System.out.println("não foi possivel realizar o check in pois não há nenhum passageiro cadastrado no sistema");
            } else {

                input.nextLine();

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

                long total = voo.passageirosCheckIn.values().size();

                long assentosRestantes = voo.getAeronaveAssociada().getCapacidadeMaxima() - total;

                if (assentosRestantes <= 0) {
                    throw new VagasIndisponiveisException("não há mais vagas no voo " + voo.getNumeroVoo());
                } else {
                    System.out.println("ainda há " + assentosRestantes + " assentos restantes no voo " + voo.getNumeroVoo());
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
                    if (!numPassagem.add(passagem.getNumeroPassagem())) {
                        throw new NumeroPassagemDuplicadoException("não foi possivel adicionar o passageiro nesse voo pois o numero da passagem esta duplicado");
                    } else {
                        passageiro.passageirosVoosCadastrados.add(voo);
                        passageiro.passagensDoPassageiro.add(passagem);
                        voo.passageirosCheckIn.put(passagem.getNumeroPassagem(), passagem);
                        System.out.println("passageiro " + passageiro.getNomePassageiro() + " registrado no voo " + voo.getNumeroVoo() + " com destino a " + voo.getDestinoVoo() + " com a passagem custando " + precoPassagem);
                    }
                }
            }
        } catch (PassageiroNaoEncontradoException | VooNaoEncontradoException | VagasIndisponiveisException | NumeroPassagemDuplicadoException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("entrada invalida, por favor digite novamente");
            input.nextLine();
        }
    }


    public void realizarEmbarque() {
        try {
            if (passageirosCadastrados.isEmpty() || voosDoAeroporto.isEmpty() || aeronaves.isEmpty()) {
                System.out.println("não foi possivel realizar embarque pois não há passageiros cadastrados |OU| não há nenhum voo cadastrado |OU| não há nenhuma aeronave cadastrada");
            } else {
                System.out.print("qual o numero da passagem?");
                int numPassagem = input.nextInt();

                input.nextLine();

                System.out.print("qual o numero do passaporte?");
                String numPassaporte = input.nextLine().trim();

                Passageiros passageiro = passageirosCadastrados.values().stream()
                        .filter(a -> a.getNumeroPassaporte().trim().equalsIgnoreCase(numPassaporte))
                        .findFirst()
                        .orElseThrow(() -> new PassageiroNaoEncontradoException("não foi possivel realizar embarque pois nenhum passageiro foi encontrado com esse numero de passaporte"));

                Passagem passagem = passageiro.passagensDoPassageiro.stream()
                        .filter(a -> a.getNumeroPassagem() == numPassagem)
                        .filter(b -> b.getVooPassagem().getStatusVoo() == StatusVoo.EMBARCANDO)
                        .filter(c -> c.getPassageiro().getNumeroPassaporte().equalsIgnoreCase(passageiro.getNumeroPassaporte()))
                        .findFirst()
                        .orElseThrow(() -> new PassagemNaoEncontradaException("não foi possivel realizar embarque pois o numero da passagem não foi encontrado |OU| o voo ja não esta mais embarcando |OU| o numero da passagem fornecida não corresponde ao passageiro fornecido"));

                passagem.getVooPassagem().passageirosNoVoo.put(passagem.getNumeroPassagem(), passagem);
                System.out.println("check-in realizado pelo passageiro " + passageiro.getNomePassageiro() + " no voo " + passagem.getVooPassagem().getNumeroVoo()+ " com destino a " +passagem.getVooPassagem().getDestinoVoo());
            }
        } catch (PassagemNaoEncontradaException | PassageiroNaoEncontradoException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("entrada invalida, por favor digite novamente");
            input.nextLine();
        }
    }

    public void consultarSeusVoos() {
        try {
            if (passageirosCadastrados.isEmpty()) {
                System.out.println("não foi possivel consultar voos, pois não há nenhum passageiro cadastrado");
            } else {
                System.out.print("qual o numero do seu passaporte?");
                String numPassaporte = input.nextLine().trim();

                input.nextLine();

                Passageiros passageiro = passageirosCadastrados.values().stream()
                        .filter(a -> a.getNumeroPassaporte().trim().equalsIgnoreCase(numPassaporte))
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
                            System.out.println("===== DETALHES DOS VOOS DO PASSAGEIRO " + passageiro.getNomePassageiro().toUpperCase() + " =====");
                            for (Voo voos : passageiro.passageirosVoosCadastrados) {
                                voos.exibirDetalhesVoo();
                            }
                        }
                        break;
                    case 2:
                        System.out.println("se vc deseja consultar seus voos corretamente, por favor tente realizar a operação novamente");
                        input.nextLine();
                        break;
                    default:
                        System.out.println("opcao invalida, por favor digite novamente");
                }
            }
        } catch (PassaporteNaoEncontradoException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("entrada invalida, pir favor digite novamente");
            input.nextLine();
        }
    }

    public void listarPassageirosVoo() {
        try {
            if (voosDoAeroporto.isEmpty()) {
                System.out.println("não foi possivel listar passageiros de um voo, pois nenhum voo foi registrado no sistema");
            } else {
                System.out.print("qual o numero do voo que você deseja listar os passageiros?");
                int numVoo = input.nextInt();

                Voo voo = voosDoAeroporto.values().stream()
                        .filter(a -> a.getNumeroVoo() == numVoo)
                        .findFirst()
                        .orElseThrow(() -> new VooNaoEncontradoException("não foi possivel listar os passageiros desse voo, pois esse voo não foi encontrado"));

                System.out.println("===== DETALHES DO VOO =====");
                voo.exibirDetalhesVoo();
                System.out.println("1 - sim");
                System.out.println("2 - não");
                System.out.println("-----------------");
                System.out.print("esse é o voo que você deseja listar os passageiros?");
                int opcao = input.nextInt();

                switch (opcao) {
                    case 1:
                        if (voo.passageirosNoVoo.isEmpty()) {
                            System.out.println("não há passageiros cadastrados nesse voo");
                        } else {
                            System.out.println("===== PASSAGEIROS DO VOO " + voo.getNumeroVoo() + " =====");
                            for (Passagem passagem : voo.passageirosNoVoo.values()) {
                                passagem.getPassageiro().exibirDetalhesPassageiro();
                            }
                        }
                        break;
                    case 2:
                        System.out.println("se você deseja listar os passageiros de um outro voo, por favor tente inserir novamente");
                        input.nextLine();
                        break;
                    default:
                        System.out.println("opção invalida, por favor digite novamente");
                }
            }
        } catch (VooNaoEncontradoException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("entrada invalida, po favor digite novamente");
            input.nextLine();
        }
    }

    public void listarVoosDoPassageiro() {
        try {
            if (passageirosCadastrados.isEmpty()) {
                System.out.println("não foi possivel listar os voos de um passageiro, pois nenhum passageiro foi registrado no sistema");
            } else {
                System.out.print("qual o cpf do passageiro?");
                String cpf = input.nextLine().trim();

                input.nextLine();

                Passageiros passageiros = passageirosCadastrados.values().stream()
                        .filter(a -> a.getCpfPassageiro().trim().equalsIgnoreCase(cpf))
                        .findFirst()
                        .orElseThrow(() -> new PassageiroNaoEncontradoException("não foi possivel listar os voos de passageiro, pois nenhum passageiro com esse cpf foi encontrado"));

                System.out.println("===== DETALHES DO PASSAGEIRO =====");
                passageiros.exibirDetalhesPassageiro();
                System.out.println("1 - sim");
                System.out.println("2 - não");
                System.out.println("--------------------");
                System.out.print("esse é o passageiro que você deseja listar voos?");
                int opcao = input.nextInt();

                switch (opcao) {
                    case 1:
                        if (passageiros.passageirosVoosCadastrados.isEmpty()) {
                            System.out.println("esse passageiro não foi registrado em nenhum voo");
                        } else {
                            System.out.println("===== VOOS DO PASSAGEIRO " + passageiros.getCpfPassageiro().toUpperCase() + " =====");
                            for (Voo voo : passageiros.passageirosVoosCadastrados) {
                                voo.exibirDetalhesVoo();
                            }
                        }
                        break;
                    case 2:
                        System.out.println("se você deseja listar os voos de outro passageiro, por favor tente inserir novamente");
                        break;
                    default:
                        System.out.println("opcao invalida, por favor digite novamente");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("entrada invalida, por favor digite novamente");
            input.nextLine();
        } catch (PassageiroNaoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }
}


