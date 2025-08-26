import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static GerenciadorEventos gm = new GerenciadorEventos("events.data");
    static Usuario usuarioAtual;

    public static void main(String[] args) {
        System.out.println("===== Sistema de Eventos RJ =====");

        login();

        int op = -1;
        while(op != 0){
            menu();
            try{
                op = Integer.parseInt(sc.nextLine());
            } catch(Exception e){ op = -1; }
            switch(op){
                case 1: listar(); break;
                case 2: novoEvento(); break;
                case 3: participar(); break;
                case 4: cancelar(); break;
                case 5: meus(); break;
                case 0: System.out.println("Saindo..."); break;
                default: System.out.println("Opcao invalida.");
            }
        }
    }

    private static void login(){
        System.out.print("Digite seu nome de usuario: ");
        String nome = sc.nextLine();
        usuarioAtual = new Usuario(nome, nome, nome+"@mail.com", "");
    }

    private static void menu(){
        System.out.println("\n--- MENU ---");
        System.out.println("1 - Listar eventos");
        System.out.println("2 - Cadastrar evento");
        System.out.println("3 - Confirmar participação");
        System.out.println("4 - Cancelar participação");
        System.out.println("5 - Meus eventos");
        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
    }

    private static void listar(){
        for(Evento e : gm.listarOrdenado()){
            System.out.println(e);
        }
    }

    private static void novoEvento(){
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Endereco: ");
        String end = sc.nextLine();
        System.out.print("Categoria (FESTA, ESPORTE, SHOW, CULTURA, OUTROS): ");
        String cat = sc.nextLine();
        System.out.print("Data e hora (yyyy-MM-dd HH:mm): ");
        LocalDateTime dt = LocalDateTime.parse(sc.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.print("Duracao em minutos: ");
        int dur = Integer.parseInt(sc.nextLine());
        System.out.print("Descricao: ");
        String desc = sc.nextLine();
        Evento e = new Evento(UUID.randomUUID().toString(), nome, end, CategoriaEvento.pegar(cat), dt, dur, desc);
        gm.addEvento(e);
        System.out.println("Evento criado com id: " + e.getId());
    }

    private static void participar(){
        System.out.print("Digite id do evento: ");
        String id = sc.nextLine();
        gm.confirmar(id, usuarioAtual.getNomeUser());
        System.out.println("Participacao registrada (se id existir).");
    }

    private static void cancelar(){
        System.out.print("Digite id do evento: ");
        String id = sc.nextLine();
        gm.cancelar(id, usuarioAtual.getNomeUser());
        System.out.println("Cancelamento feito (se id existir).");
    }

    private static void meus(){
        List<Evento> lista = gm.meusEventos(usuarioAtual.getNomeUser());
        if(lista.isEmpty()) System.out.println("Voce nao tem eventos confirmados");
        for(Evento e: lista) System.out.println(e);
    }
}
