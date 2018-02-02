package br.com.blockcells.blockcells.modelo;

import java.io.Serializable;

/**
 * Created by anderson on 23/01/2018.
 */

public class Justificativa  implements Serializable {
    private Long id;
    private String data_hora;
    private String desc_justificativa;
    private String evento;
    private boolean justificado;
    private double latitude;
    private double longitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData_hora() {
        return data_hora;
    }

    public void setData_hora(String data_hora) {
        this.data_hora = data_hora;
    }

    public String getDesc_justificativa() {
        return desc_justificativa;
    }

    public void setDesc_justificativa(String desc_justificativa) {
        this.desc_justificativa = desc_justificativa;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public boolean isJustificado() {
        return justificado;
    }

    public void setJustificado(boolean justificado) {
        this.justificado = justificado;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
