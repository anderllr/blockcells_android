package br.com.blockcells.blockcells.modelo;

/**
 * Created by Anderson on 08/02/2018.
 */

public class Solicitacao {
    private Boolean aceito;
    private String nome;
    private String username;

    public Boolean getAceito() {
        return aceito;
    }

    public void setAceito(Boolean aceito) {
        this.aceito = aceito;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
