package sistemas.conservacion.controllers;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sistemas.conservacion.models.Especie;
import sistemas.conservacion.models.EstadoConservacion;
import sistemas.conservacion.stores.EstadoConservacionStore;

public class EstadoController {
    @Inject
    private EstadoConservacionStore estadoStore;

    @FXML public TableView<EstadoConservacion> estadoTable;
    @FXML public TableColumn<EstadoConservacion, String> estadoId;
    @FXML public TableColumn<EstadoConservacion, String> estadoContenido;
    @FXML public TableColumn<EstadoConservacion, String> estadoDetalles;

    public void initialize() {
        // Initialize the cells
        estadoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        estadoContenido.setCellValueFactory(new PropertyValueFactory<>("estadoConservacion"));
        estadoDetalles.setCellValueFactory(new PropertyValueFactory<>("definicionEstado"));

        // Load the observations
        loadObservations();
    }

    public void loadObservations() {
        estadoTable.getItems().clear();
        estadoTable.getItems().addAll(estadoStore.getAll());
    }
}
