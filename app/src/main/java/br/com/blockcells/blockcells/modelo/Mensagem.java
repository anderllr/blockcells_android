package br.com.blockcells.blockcells.modelo;

import java.io.Serializable;

/**
 * Created by anderson on 18/11/16.
 */

public class Mensagem implements Serializable {
    private Long id;
    private String descmensagem;
    private String descmensagemVIP;
    private boolean localizacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensagem() {
        return descmensagem;
    }

    public void setMensagem(String descmensagem) {
        this.descmensagem = descmensagem;
    }

    public String getDescmensagemVIP() {
        return descmensagemVIP;
    }

    public void setDescmensagemVIP(String descmensagemVIP) {
        this.descmensagemVIP = descmensagemVIP;
    }

    public boolean isLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(boolean localizacao) {
        this.localizacao = localizacao;
    }
}
