package br.com.blockcells.blockcells.modelo;

import java.io.Serializable;

/**
 * Created by anderson on 21/11/16.
 */

public class Kilometragem implements Serializable {
    private Long id;
    private Integer kmMinimo;
    private Integer kmMaximo;

    public Integer getKmAlerta() {
        return kmAlerta;
    }

    public void setKmAlerta(Integer kmAlerta) {
        this.kmAlerta = kmAlerta;
    }

    private Integer kmAlerta;
    private boolean ativarBip;
    private boolean enviarAlerta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getKmMinimo() {
        return kmMinimo;
    }

    public void setKmMinimo(Integer kmMinimo) {
        this.kmMinimo = kmMinimo;
    }

    public Integer getKmMaximo() {
        return kmMaximo;
    }

    public void setKmMaximo(Integer kmMaximo) {
        this.kmMaximo = kmMaximo;
    }

    public boolean isAtivarBip() {
        return ativarBip;
    }

    public void setAtivarBip(boolean ativarBip) {
        this.ativarBip = ativarBip;
    }

    public boolean isEnviarAlerta() {
        return enviarAlerta;
    }

    public void setEnviarAlerta(boolean enviarAlerta) {
        this.enviarAlerta = enviarAlerta;
    }
}
