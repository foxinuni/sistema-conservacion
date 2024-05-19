package sistemas.conservacion.models;

public class EstadoConservacion {
    public String id;
    public String estadoConservacion;
    public String definicionEstado;

    public EstadoConservacion(String id, String estadoConservacion, String definicionEstado) {
        this.id = id;
        this.estadoConservacion = estadoConservacion;
        this.definicionEstado = definicionEstado;
    }

    public String getId() {
        return id;
    }

    public String getEstadoConservacion() {
        return estadoConservacion;
    }

    public String getDefinicionEstado() {
        return definicionEstado;
    }
}
