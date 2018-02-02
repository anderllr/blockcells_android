package br.com.blockcells.blockcells.modelo;

import java.io.Serializable;

/**
 * Created by anderson on 17/11/16.
 */

public class ConfigGeral implements Serializable {
    private Long id;
    private boolean controleRemoto;
    private boolean informaLocal;
    private boolean ativado;
    private boolean enviasms;

    public boolean isEnviasms() {
        return enviasms;
    }

    public void setEnviasms(boolean enviasms) {
        this.enviasms = enviasms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getControleRemoto() {
        return controleRemoto;
    }

    public void setControleRemoto(boolean controleRemoto) {
        this.controleRemoto = controleRemoto;
    }

    public boolean getInformaLocal() {
        return informaLocal;
    }

    public void setInformaLocal(boolean informaLocal) {
        this.informaLocal = informaLocal;
    }

    public boolean getAtivado() {
        return ativado;
    }

    public void setAtivado(boolean ativado) {
        this.ativado = ativado;
    }
}
