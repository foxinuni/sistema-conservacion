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

    public void setId(String id) {
        this.id = id;
    }

    public void setEstadoConservacion(String estadoConservacion) {
        this.estadoConservacion = estadoConservacion;
    }

    public void setDefinicionEstado(String definicionEstado) {
        this.definicionEstado = definicionEstado;
    }
}
