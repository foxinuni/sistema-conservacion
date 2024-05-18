package sistemas.conservacion.models;

import java.sql.Timestamp;

public class Observacion {
    public String id;
    public String id_especie;
    public int cantidad_observada;
    public float latitud;
    public float longitud;
    public Timestamp fecha_hora;
    public String nota;

    public Observacion(String id, String id_especie, Timestamp fecha_hora, int cantidad_observada, float latitud, float longitud, String nota) {
        this.id = id;
        this.id_especie = id_especie;
        this.fecha_hora = fecha_hora;
        this.cantidad_observada = cantidad_observada;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nota = nota;
    }
}
