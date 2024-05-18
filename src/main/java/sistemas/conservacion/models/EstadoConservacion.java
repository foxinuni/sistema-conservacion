package sistemas.conservacion.models;

public class EstadoConservacion {
    public String id;
    public String estado_conservacion;
    public String definicion_estado;

    public EstadoConservacion(String id, String estado_conservacion, String definicion_estado) {
        this.id = id;
        this.estado_conservacion = estado_conservacion;
        this.definicion_estado = definicion_estado;
    }
}
