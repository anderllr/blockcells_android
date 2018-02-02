package br.com.blockcells.blockcells.modelo;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by anderson on 27/01/17.
 */

public class ContatosExcecao implements Serializable {
    private Long id;
    private String nome;
    private String fone;
    private String foneNormalize;
    private Bitmap foto;

    public String getFoneNormalize() {
        return foneNormalize;
    }

    public void setFoneNormalize(String foneNormalize) {
        this.foneNormalize = foneNormalize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }
}
