package br.com.blockcells.blockcells.modelo;

import java.io.Serializable;

/**
 * Created by anderson on 17/11/16.
 */

public class ConfigGeral implements Serializable {
    private boolean ativado;
    private boolean controle_remoto;
    private boolean envia_sms;
    private boolean informa_local;

    public boolean isEnvia_sms() {
        return envia_sms;
    }

    public void setEnvia_sms(boolean enviasms) {
        this.envia_sms = enviasms;
    }

    public boolean getControle_remoto() {
        return controle_remoto;
    }

    public void setControle_remoto(boolean controle_remoto) {
        this.controle_remoto = controle_remoto;
    }

    public boolean getInforma_local() {
        return informa_local;
    }

    public void setInforma_local(boolean informa_local) {
        this.informa_local = informa_local;
    }

    public boolean getAtivado() {
        return ativado;
    }

    public void setAtivado(boolean ativado) {
        this.ativado = ativado;
    }
}
