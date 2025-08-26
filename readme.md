// Main.java
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GerenciadorEventos gerenciador = new GerenciadorEventos();
        gerenciador.carregarEventos();

        System.out.println("Bem vindo ao sistema de Eventos do RJ!");
        System.out.print("Digite seu nome: ");
        String nome = sc.nextLine();

        System.out.print("Digite seu email: ");
        String email = sc.nextLine();

        System.out.print("Digite sua idade: ");
        int idade = 0;
        try {
            idade = Integer.parseInt(sc.nextLine());
        } catch(Exception e) {
            System.out.println("Idade inválida, vou considerar 18.");
            idade = 18;
        }

        Usuario usuario = new Usuario(nome, email, idade);

        int opcao = -1;
        while(opcao != 0) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Cadastrar evento");
            System.out.println("2. Listar eventos");
            System.out.println("3. Participar de evento");
            System.out.println("4. Meus eventos confirmados");
            System.out.println("5. Cancelar participação");
            System.out.println("6. Eventos ocorrendo agora");
            System.out.println("7. Eventos passados");
            System.out.println("0. Sair");

            try {
                opcao = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                opcao = -1;
            }

            switch(opcao) {
                case 1: gerenciador.cadastrarEvento(sc); break;
                case 2: gerenciador.listarEventos(); break;
                case 3: gerenciador.participarEvento(usuario, sc); break;
                case 4: usuario.listarConfirmados(); break;
                case 5: usuario.cancelarEvento(sc); break;
                case 6: gerenciador.listarEventosOcorrendoAgora(); break;
                case 7: gerenciador.listarEventosPassados(); break;
                case 0: 
                    gerenciador.salvarEventos();
                    System.out.println("Saindo... Obrigado!");
                    break;
                default: System.out.println("Opção inválida.");
            }
        }
    }
}
// model/Usuario.java
import java.util.ArrayList;
import java.util.Scanner;

public class Usuario {
    private String nome;
    private String email;
    private int idade;
    private ArrayList<Evento> confirmados;

    public Usuario(String nome, String email, int idade) {
        this.nome = nome;
        this.email = email;
        this.idade = idade;
        this.confirmados = new ArrayList<>();
    }

    public void confirmar(Evento e) {
        if (!confirmados.contains(e)) {
            confirmados.add(e);
            System.out.println("Você confirmou presença em: " + e.getNome());
        } else {
            System.out.println("Você já está confirmado neste evento.");
        }
    }

    public void listarConfirmados() {
        if (confirmados.isEmpty()) {
            System.out.println("Nenhum evento confirmado ainda.");
        } else {
            System.out.println("Eventos confirmados:");
            for (Evento e : confirmados) {
                System.out.println(e);
            }
        }
    }

    public void cancelarEvento(Scanner sc) {
        if (confirmados.isEmpty()) {
            System.out.println("Você não está em nenhum evento.");
            return;
        }
        System.out.println("Digite o nome do evento que deseja cancelar:");
        String nome = sc.nextLine();

        Evento encontrado = null;
        for (Evento e : confirmados) {
            if (e.getNome().equalsIgnoreCase(nome)) {
                encontrado = e;
                break;
            }
        }
        if (encontrado != null) {
            confirmados.remove(encontrado);
            System.out.println("Cancelado com sucesso!");
        } else {
            System.out.println("Evento não encontrado.");
        }
    }
}
// model/Evento.java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Evento {
    private String nome;
    private String endereco;
    private String categoria;
    private LocalDateTime horario;
    private String descricao;

    public Evento(String nome, String endereco, String categoria, LocalDateTime horario, String descricao) {
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.horario = horario;
        this.descricao = descricao;
    }

    public String getNome() { return nome; }
    public LocalDateTime getHorario() { return horario; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return nome + " | " + categoria + " | " + horario.format(fmt) + " | " + endereco + " | " + descricao;
    }

    public String toFile() {
        return nome + ";" + endereco + ";" + categoria + ";" + horario + ";" + descricao;
    }

    public static Evento fromFile(String linha) {
        try {
            String[] partes = linha.split(";");
            LocalDateTime h = LocalDateTime.parse(partes[3]);
            return new Evento(partes[0], partes[1], partes[2], h, partes[4]);
        } catch (Exception e) {
            return null; // erro de parsing, ignora
        }
    }
}
// service/GerenciadorEventos.java
import java.util.*;
import java.io.*;
import java.time.LocalDateTime;

public class GerenciadorEventos {
    private ArrayList<Evento> eventos = new ArrayList<>();
    private final String arquivo = "events.data";

    public void cadastrarEvento(Scanner sc) {
        System.out.print("Nome do evento: ");
        String nome = sc.nextLine();

        System.out.print("Endereço: ");
        String end = sc.nextLine();

        System.out.println("Categorias: Festa, Show, Esporte, Teatro");
        String cat = sc.nextLine();

        System.out.print("Data e hora (yyyy-MM-ddTHH:mm): ");
        String data = sc.nextLine();
        LocalDateTime horario;
        try {
            horario = LocalDateTime.parse(data);
        } catch(Exception e) {
            System.out.println("Data inválida. Evento não cadastrado.");
            return;
        }

        System.out.print("Descrição: ");
        String desc = sc.nextLine();

        Evento e = new Evento(nome, end, cat, horario, desc);
        eventos.add(e);
        System.out.println("Evento cadastrado!");
    }

    public void listarEventos() {
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado.");
        } else {
            eventos.sort(Comparator.comparing(Evento::getHorario));
            for (Evento e : eventos) {
                System.out.println(e);
            }
        }
    }

    public void participarEvento(Usuario u, Scanner sc) {
        listarEventos();
        System.out.print("Digite o nome do evento para participar: ");
        String nome = sc.nextLine();

        for (Evento e : eventos) {
            if (e.getNome().equalsIgnoreCase(nome)) {
                u.confirmar(e);
                return;
            }
        }
        System.out.println("Evento não encontrado.");
    }

    public void listarEventosOcorrendoAgora() {
        LocalDateTime agora = LocalDateTime.now();
        boolean achou = false;
        for (Evento e : eventos) {
            if (e.getHorario().getDayOfYear() == agora.getDayOfYear() && 
                e.getHorario().getHour() == agora.getHour()) {
                System.out.println("Está rolando agora: " + e);
                achou = true;
            }
        }
        if (!achou) System.out.println("Nenhum evento ocorrendo nesse instante.");
    }

    public void listarEventosPassados() {
        LocalDateTime agora = LocalDateTime.now();
        for (Evento e : eventos) {
            if (e.getHorario().isBefore(agora)) {
                System.out.println("Evento já passou: " + e);
            }
        }
    }

    public void salvarEventos() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo))) {
            for (Evento e : eventos) {
                pw.println(e.toFile());
            }
        } catch(IOException e) {
            System.out.println("Erro ao salvar eventos.");
        }
    }

    public void carregarEventos() {
        File f = new File(arquivo);
        if (!f.exists()) return;
        try (Scanner sc = new Scanner(f)) {
            while(sc.hasNextLine()) {
                Evento e = Evento.fromFile(sc.nextLine());
                if (e != null) eventos.add(e);
            }
        } catch(Exception e) {
            System.out.println("Erro ao carregar eventos.");
        }
    }
}
