package br.com.blockcells.blockcells.modelo;

import java.io.Serializable;

/**
 * Created by anderson on 09/12/16.
 */

public class Horario implements Serializable {
    private boolean segunda;
    private boolean terca;
    private boolean quarta;
    private boolean quinta;
    private boolean sexta;
    private String util_inicio;
    private String util_fim;
    private boolean sabado;
    private boolean domingo;
    private String fds_inicio;
    private String fds_fim;

    public boolean isSegunda() {
        return segunda;
    }

    public void setSegunda(boolean segunda) {
        this.segunda = segunda;
    }

    public boolean isTerca() {
        return terca;
    }

    public void setTerca(boolean terca) {
        this.terca = terca;
    }

    public boolean isQuarta() {
        return quarta;
    }

    public void setQuarta(boolean quarta) {
        this.quarta = quarta;
    }

    public boolean isQuinta() {
        return quinta;
    }

    public void setQuinta(boolean quinta) {
        this.quinta = quinta;
    }

    public boolean isSexta() {
        return sexta;
    }

    public void setSexta(boolean sexta) {
        this.sexta = sexta;
    }

    public String getUtil_inicio() {
        return util_inicio;
    }

    public void setUtil_inicio(String util_inicio) {
        this.util_inicio = util_inicio;
    }

    public String getUtil_fim() {
        return util_fim;
    }

    public void setUtil_fim(String util_fim) {
        this.util_fim = util_fim;
    }

    public boolean isSabado() {
        return sabado;
    }

    public void setSabado(boolean sabado) {
        this.sabado = sabado;
    }

    public boolean isDomingo() {
        return domingo;
    }

    public void setDomingo(boolean domingo) {
        this.domingo = domingo;
    }

    public String getFds_inicio() {
        return fds_inicio;
    }

    public void setFds_inicio(String fds_inicio) {
        this.fds_inicio = fds_inicio;
    }

    public String getFds_fim() {
        return fds_fim;
    }

    public void setFds_fim(String fds_fim) {
        this.fds_fim = fds_fim;
    }
}
