// Classe basica do usuario
public class Usuario {
    private String nomeUser; // apelido
    private String nomeCompleto;
    private String email;
    private String telefone;

    public Usuario(String nomeUser, String nomeCompleto, String email, String telefone){
        this.nomeUser = nomeUser;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.telefone = telefone;
    }

    public String getNomeUser(){
        return nomeUser;
    }

    public String toString(){
        return nomeCompleto + " (" + nomeUser + ") - " + email;
    }
}
