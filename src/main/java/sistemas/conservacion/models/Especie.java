package sistemas.conservacion.models;

public class Especie {
    public String id;
    public String nombre_cientifico;
    public String nombre_comun;
    public String caracteristicas_fisicas;
    public String habitad;
    public String id_estado_conservacion;

    public Especie(String id, String nombre_cientifico, String nombre_comun, String caracteristicas_fisicas, String habitad, String id_estado_conservacion) {
        this.id = id;
        this.nombre_cientifico = nombre_cientifico;
        this.nombre_comun = nombre_comun;
        this.caracteristicas_fisicas = caracteristicas_fisicas;
        this.habitad = habitad;
        this.id_estado_conservacion = id_estado_conservacion;
    }
}
