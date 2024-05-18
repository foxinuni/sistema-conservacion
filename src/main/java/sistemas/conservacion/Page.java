package sistemas.conservacion;

public enum Page {
    ESTADO("estado","Estado", "/sistemas/conservacion/estado-view.fxml"),
    ESPECIE("especie","Especie", "/sistemas/conservacion/especie-view.fxml"),
    OBSERVACION("observacion", "Observación", "/sistemas/conservacion/observacion-view.fxml");

    private final String id;
    private final String title;
    private final String fxml;

    Page(String id, String title, String fxml) {
        this.id = id;
        this.title = title;
        this.fxml = fxml;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getFxml() {
        return fxml;
    }
}
