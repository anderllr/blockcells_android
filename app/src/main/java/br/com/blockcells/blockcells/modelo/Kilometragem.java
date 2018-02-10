package br.com.blockcells.blockcells.modelo;

import java.io.Serializable;

/**
 * Created by anderson on 21/11/16.
 */

public class Kilometragem implements Serializable {
    private Integer velocidade_min;
    private Integer velocidade_max;
    private Integer velocidade_alerta;
    private boolean emite_bip;
    private boolean envia_alerta;

    public Integer getVelocidade_alerta() {
        return velocidade_alerta;
    }

    public void setVelocidade_alerta(Integer velocidade_alerta) {
        this.velocidade_alerta = velocidade_alerta;
    }

    public Integer getVelocidade_min() {
        return velocidade_min;
    }

    public void setVelocidade_min(Integer velocidade_min) {
        this.velocidade_min = velocidade_min;
    }

    public Integer getVelocidade_max() {
        return velocidade_max;
    }

    public void setVelocidade_max(Integer velocidade_max) {
        this.velocidade_max = velocidade_max;
    }

    public boolean isEmite_bip() {
        return emite_bip;
    }

    public void setEmite_bip(boolean emite_bip) {
        this.emite_bip = emite_bip;
    }

    public boolean isEnvia_alerta() {
        return envia_alerta;
    }

    public void setEnvia_alerta(boolean envia_alerta) {
        this.envia_alerta = envia_alerta;
    }
}
