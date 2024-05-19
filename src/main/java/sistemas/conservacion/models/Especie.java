package sistemas.conservacion.models;

public class Especie {
    public String id;
    public String nombreCientifico;
    public String nombreComun;
    public String caracteristicasFisicas;
    public String habitad;
    public String estadoConservacionId;

    public Especie(String id, String nombreCientifico, String nombreComun, String caracteristicasFisicas, String habitad, String estadoConservacionId) {
        this.id = id;
        this.nombreCientifico = nombreCientifico;
        this.nombreComun = nombreComun;
        this.caracteristicasFisicas = caracteristicasFisicas;
        this.habitad = habitad;
        this.estadoConservacionId = estadoConservacionId;
    }

    public String getId() {
        return id;
    }

    public String getNombreCientifico() {
        return nombreCientifico;
    }

    public String getNombreComun() {
        return nombreComun;
    }

    public String getCaracteristicasFisicas() {
        return caracteristicasFisicas;
    }

    public String getHabitad() {
        return habitad;
    }

    public String getEstadoConservacionId() {
        return estadoConservacionId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombreCientifico(String nombreCientifico) {
        this.nombreCientifico = nombreCientifico;
    }

    public void setNombreComun(String nombreComun) {
        this.nombreComun = nombreComun;
    }

    public void setCaracteristicasFisicas(String caracteristicasFisicas) {
        this.caracteristicasFisicas = caracteristicasFisicas;
    }

    public void setHabitad(String habitad) {
        this.habitad = habitad;
    }

    public void setEstadoConservacionId(String estadoConservacionId) {
        this.estadoConservacionId = estadoConservacionId;
    }
}
