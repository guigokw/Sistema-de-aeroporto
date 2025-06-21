import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Aeroporto aeroporto = new Aeroporto();

        LocalDateTime tempoAtual = LocalDateTime.now();

        for (Voo voo : aeroporto.voosDoAeroporto.values()) {
            if (voo.getDataHorarioPartida().getDayOfMonth() == LocalDateTime.now().getDayOfMonth()) {
                long minutos = ChronoUnit.MINUTES.between(voo.getDataHorarioPartida(), LocalDateTime.now());
                if (minutos < 60 && minutos > 0) {
                    voo.setStatusVoo(StatusVoo.EMBARCANDO);
                } else if (tempoAtual.isAfter(voo.getDataHorarioPartida()) && tempoAtual.isBefore(voo.getDataHorarioChegada())) {
                    voo.setStatusVoo(StatusVoo.EM_VOO);
                } else if (tempoAtual.isAfter(voo.getDataHorarioChegada())) {
                    voo.setStatusVoo(StatusVoo.FINALIZADO);
                }
            }
        }
        while (true) {
            try {
                System.out.println("1 - MENU ADMINISTRATIVO");
                System.out.println("2 - MENU DO PASSAGEIRO");
                System.out.println("3 - MENU DE RELATORIOS E CONSULTAS");
                System.out.println("4 - SAIR DO AEROPORTO");
                System.out.println("======================");
                System.out.print("qual dessas opcoes vc escohe?");
                int opcao = input.nextInt();

                input.nextLine();

                switch (opcao) {
                    case 1 -> menuAdministrativo(input, aeroporto);
                    case 2 -> menuPassgeiro(input, aeroporto);
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
                System.out.println("1 - cadastrar aeronave");
                System.out.println("2 - remover aeronave");
                System.out.println("3 - cadastrar voo");
                System.out.println("4 - cancelar voo");
                System.out.println("5 - cadastrar passageiro");
                System.out.println("6 - remover passageiros");
                System.out.println("7 - alterar status do voo");
                System.out.println("8 - sair do menu admnistrativo");
                System.out.println("_------------------------");
                System.out.print("escolha uma dessas opções:");
                int opcao = input.nextInt();

                switch (opcao) {
                    case 1 -> aeroporto.cadastrarAeronaveAeroporto();
                    case 2 -> aeroporto.removerAeronaveAeroporto();
                    case 3 -> aeroporto.cadastrarVooAeroporto();
                    case 4 -> aeroporto.cancelarVooAeroporto();
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
            } catch (NumeroRegistroInvalidoException | AeronaveNaoEncontradaException |
                     VooDuplicadoException | NumeroVooDuplicadoException | VooNaoEncontradoException |
                     CpfInvalidoException | CpfDuplicadoException
                     | PassageiroNaoEncontradoException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void menuPassgeiro(Scanner input, Aeroporto aeroporto) {

    }
}

