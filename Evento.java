import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Evento {
    private String id; // meio sem uso mas coloquei para diferenciar
    private String nome;
    private String endereco;
    private CategoriaEvento categoria;
    private LocalDateTime dataHora;
    private int duracaoMin;
    private String descricao;
    private List<String> participantes = new ArrayList<>();

    private static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Evento(String id, String nome, String endereco, CategoriaEvento categoria, LocalDateTime dataHora, int duracaoMin, String descricao){
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.dataHora = dataHora;
        this.duracaoMin = duracaoMin;
        this.descricao = descricao;
    }

    public String getId(){ return id; }
    public String getNome(){ return nome; }
    public LocalDateTime getDataHora(){ return dataHora; }
    public int getDuracao(){ return duracaoMin; }
    public List<String> getParticipantes(){ return participantes; }

    public boolean acontecendoAgora(){
        LocalDateTime agora = LocalDateTime.now();
        return !agora.isBefore(dataHora) && agora.isBefore(dataHora.plusMinutes(duracaoMin));
    }

    public boolean jaPassou(){
        return LocalDateTime.now().isAfter(dataHora.plusMinutes(duracaoMin));
    }

    // salva como linha de texto
    public String salvarLinha(){
        return id + "|" + nome + "|" + endereco + "|" + categoria + "|" + dataHora.format(fmt) + "|" + duracaoMin + "|" + descricao + "|" + String.join(",", participantes);
    }

    public static Evento lerLinha(String linha){
        try {
            String[] p = linha.split("\\|");
            Evento e = new Evento(
                p[0],
                p[1],
                p[2],
                CategoriaEvento.pegar(p[3]),
                LocalDateTime.parse(p[4], fmt),
                Integer.parseInt(p[5]),
                p[6]
            );
            if(p.length > 7 && !p[7].isEmpty()){
                e.participantes.addAll(Arrays.asList(p[7].split(",")));
            }
            return e;
        } catch(Exception e){
            System.out.println("Erro lendo evento: " + linha);
            return null;
        }
    }

    public String toString(){
        String status = acontecendoAgora()? " (OCORRENDO)" : (jaPassou()? " (PASSOU)" : "");
        return id + " - " + nome + " [" + categoria + "] em " + dataHora.format(fmt) + " no " + endereco + status;
    }
}
