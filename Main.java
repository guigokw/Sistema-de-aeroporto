import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private final Aeroporto aeroporto;
    private final ScheduledExecutorService scheduler;

    public Main(Aeroporto aeroporto) {
        this.aeroporto = aeroporto;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }


    public void iniciarAtualizacaoAutomatica() {
        scheduler.scheduleAtFixedRate(() -> {
            LocalDateTime tempoAtual = LocalDateTime.now();

            for (Voo voo : aeroporto.voosDoAeroporto.values()) {
                if (voo.getDataHorarioPartida().toLocalDate().equals(tempoAtual.toLocalDate())) {
                    long minutos = ChronoUnit.MINUTES.between(voo.getDataHorarioPartida(), tempoAtual);

                    if (minutos >= 0 && minutos < 60) {
                        voo.setStatusVoo(StatusVoo.EMBARCANDO);
                    } else if (tempoAtual.isAfter(voo.getDataHorarioPartida()) && tempoAtual.isBefore(voo.getDataHorarioChegada())) {
                        voo.setStatusVoo(StatusVoo.EM_VOO);
                    } else if (tempoAtual.isAfter(voo.getDataHorarioChegada())) {
                        if (voo.getStatusVoo() != StatusVoo.FINALIZADO) {
                            voo.setStatusVoo(StatusVoo.FINALIZADO);
                            voo.passageirosNoVoo.clear();
                        }
                    }
                }
            }

        }, 0, 1, TimeUnit.MINUTES);
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Aeroporto aeroporto = new Aeroporto();

        Main atualizar = new Main(aeroporto);

        atualizar.iniciarAtualizacaoAutomatica();

        while (true) {
            try {
                System.out.println("===== SISTEMA DO AEROPORTO =====");
                System.out.println("[1] - MENU ADMINISTRATIVO");
                System.out.println("[2] - MENU DO PASSAGEIRO");
                System.out.println("[3] - MENU DE RELATORIOS E CONSULTAS");
                System.out.println("[4] - SAIR DO AEROPORTO");
                System.out.println("======================");
                System.out.print("qual dessas operações você deseja seguir?");
                int opcao = input.nextInt();

                input.nextLine();

                switch (opcao) {
                    case 1 -> menuAdministrativo(input, aeroporto);
                    case 2 -> menuPassageiro(input, aeroporto);
                    case 3 -> menuRelatoriosConsultas(input, aeroporto);
                    case 4 -> {
                        System.out.println("saindo do sistema do aeroporto....");
                        return;
                    }
                    default -> System.out.println("opcção invalida, por favor digite novamente");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("entrada invalida, por favor digite novamente");
                input.nextLine();
            }
        }
    }

    private static void menuAdministrativo(Scanner input, Aeroporto aeroporto) {
        while (true) {
            try {
                System.out.println("===== SISTEMA ADMINISTRATIVO ======");
                System.out.println("[1] - cadastrar aeronave");
                System.out.println("[2] - remover aeronave");
                System.out.println("[3] - cadastrar voo");
                System.out.println("[4] - cancelar voo");
                System.out.println("[5] - cadastrar passageiro");
                System.out.println("[6] - remover passageiros");
                System.out.println("[7] - alterar status do voo");
                System.out.println("[8] - sair do menu admnistrativo");
                System.out.println("_------------------------");
                System.out.print("escolha uma dessas opções:");
                int opcao = input.nextInt();

                switch (opcao) {
                    case 1 -> aeroporto.cadastrarAeronaveAeroporto(); // confere
                    case 2 -> aeroporto.removerAeronaveAeroporto(); // confere
                    case 3 -> aeroporto.cadastrarVooAeroporto(); // confere
                    case 4 -> aeroporto.cancelarVooAeroporto(); // confere
                    case 5 -> aeroporto.cadastrarPassageirosAeroporto();
                    case 6 -> aeroporto.removerPassageiroAeroporto();
                    case 7 -> aeroporto.alterarStatusVooAeroporto();
                    case 8 -> {
                        System.out.println("saindo do menu administrativo....");
                        return;
                    }
                    default -> System.out.println("opcao invalida, por favor digite novamente");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("entrada invalida, por favor digite novamente");
                input.nextLine();
            }
        }
    }

    private static void menuPassageiro(Scanner input, Aeroporto aeroporto) {
        while (true) {
            try {
                System.out.println("===== SISTEMA DO PASSAGEIRO =====");
                System.out.println("[1] - realizar check-in");
                System.out.println("[2] - realizar embarque");
                System.out.println("[3] - consultar seus voos");
                System.out.println("[4] - sair do menu do passageiro");
                System.out.println("-----------------------");
                System.out.print("qual dessa operações vc deseja realizar?");
                int opcao = input.nextInt();

                switch (opcao) {
                    case 1 -> aeroporto.realizarCheckIn();
                    case 2 -> aeroporto.realizarEmbarque();
                    case 3 -> aeroporto.consultarSeusVoos();
                    case 4 -> {
                        System.out.println("saindo do menu do passageiro?");
                        return;
                    }
                    default -> System.out.println("opcao invalida, por favor digite novamente");
                }
           } catch (java.util.InputMismatchException e) {
                System.out.println("entrada invalida, por favor digite novamente");
                input.nextLine();
            }
        }
    }

    private static void menuRelatoriosConsultas(Scanner input, Aeroporto aeroporto) {
        while (true) {
            try {
                System.out.println("===== SISTEMA DE RELATORIOS E CONSULTAS =====");
                System.out.println("[1] - listar voos por alguma caracteristica");
                System.out.println("[2] - listar aeronaves");
                System.out.println("[3] - listar passageiros de um voo");
                System.out.println("[4] - mostrar historico de voos de um passageiro");
                System.out.println("[5] - listar passageiros cadastrados no aeroporto");
                System.out.println("[6] - sair do menu de relatorios e consultas");
                System.out.println("--------------------");
                System.out.print("qual dessas operações você deseja seguir?");
                int opcao = input.nextInt();

                switch (opcao) {
                    case 1 -> aeroporto.listarVooPorCaracteristica();
                    case 2 -> aeroporto.listarAeronaves();
                    case 3 -> aeroporto.listarPassageirosVoo();
                    case 4 -> aeroporto.listarVoosDoPassageiro();
                    case 5 -> aeroporto.consultarInformacoesPassageiro();
                    case 6 -> {
                        System.out.println("saindo do menu de relatorios e passageiros");
                        return;
                    }
                    default -> System.out.println("opção invalida, por favor digite novamente");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("entrada invalida, por favir digite novamente");
                input.nextLine();
            }
        }
    }
}

