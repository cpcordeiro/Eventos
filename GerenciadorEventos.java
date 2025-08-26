import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class GerenciadorEventos {
    private List<Evento> eventos = new ArrayList<>();
    private Path arquivo;

    public GerenciadorEventos(String nomeArquivo){
        this.arquivo = Paths.get(nomeArquivo);
        carregar();
    }

    public void addEvento(Evento e){
        eventos.add(e);
        salvar();
    }

    public List<Evento> listarOrdenado(){
        return eventos.stream()
            .sorted(Comparator.comparing(Evento::getDataHora))
            .collect(Collectors.toList());
    }

    public Evento procurarPorId(String id){
        for(Evento e : eventos){
            if(e.getId().equals(id)) return e;
        }
        return null;
    }

    public void confirmar(String id, String usuario){
        Evento e = procurarPorId(id);
        if(e != null && !e.getParticipantes().contains(usuario)){
            e.getParticipantes().add(usuario);
            salvar();
        }
    }

    public void cancelar(String id, String usuario){
        Evento e = procurarPorId(id);
        if(e != null){
            e.getParticipantes().remove(usuario);
            salvar();
        }
    }

    public List<Evento> meusEventos(String usuario){
        return eventos.stream().filter(e -> e.getParticipantes().contains(usuario)).collect(Collectors.toList());
    }

    private void salvar(){
        try{
            List<String> linhas = eventos.stream().map(Evento::salvarLinha).collect(Collectors.toList());
            Files.write(arquivo, linhas);
        } catch(IOException ex){
            System.out.println("Erro ao salvar arquivo...");
        }
    }

    private void carregar(){
        if(!Files.exists(arquivo)) return;
        try{
            for(String l : Files.readAllLines(arquivo)){
                Evento e = Evento.lerLinha(l);
                if(e != null) eventos.add(e);
            }
        } catch(Exception ex){
            System.out.println("Erro carregando eventos");
        }
    }
}
