package sistemas.conservacion.models;

import java.sql.Timestamp;

public class Observacion {
    public String id;
    public String especieId;
    public int cantidadObservada;
    public float latitud;
    public float longitud;
    public Timestamp fechaHora;
    public String nota;

    public Observacion(String id, String especieId, Timestamp fechaHora, int cantidadObservada, float latitud, float longitud, String nota) {
        this.id = id;
        this.especieId = especieId;
        this.fechaHora = fechaHora;
        this.cantidadObservada = cantidadObservada;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nota = nota;
    }

    public String getId() {
        return id;
    }

    public String getEspecieId() {
        return especieId;
    }

    public int getCantidadObservada() {
        return cantidadObservada;
    }

    public float getLatitud() {
        return latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public String getNota() {
        return nota;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEspecieId(String especieId) {
        this.especieId = especieId;
    }

    public void setCantidadObservada(int cantidadObservada) {
        this.cantidadObservada = cantidadObservada;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}
