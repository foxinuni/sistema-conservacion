package sistemas.conservacion.controllers;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sistemas.conservacion.models.Observacion;
import sistemas.conservacion.stores.ObservacionStore;

import java.sql.Timestamp;

public class ObservacionController {
    @Inject
    private ObservacionStore observacionStore;

    @FXML public TableView<Observacion> observationTable;
    @FXML public TableColumn<Observacion, String> observationId;
    @FXML public TableColumn<Observacion, String> observationEspecie;
    @FXML public TableColumn<Observacion, Integer> observationCantidad;
    @FXML public TableColumn<Observacion, String> observationLatitud;
    @FXML public TableColumn<Observacion, String> observationLongitud;
    @FXML public TableColumn<Observacion, Timestamp> observationFecha;
    @FXML public TableColumn<Observacion, String> observationNota;

    public void initialize() {
        // Initialize the cells
        observationId.setCellValueFactory(new PropertyValueFactory<>("id"));
        observationEspecie.setCellValueFactory(new PropertyValueFactory<>("especieId"));
        observationCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidadObservada"));
        observationLatitud.setCellValueFactory(new PropertyValueFactory<>("latitud"));
        observationLongitud.setCellValueFactory(new PropertyValueFactory<>("longitud"));
        observationFecha.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
        observationNota.setCellValueFactory(new PropertyValueFactory<>("nota"));

        // Load the observations
        loadObservations();
    }

    public void loadObservations() {
        observationTable.getItems().clear();
        observationTable.getItems().addAll(observacionStore.getAll());
    }
}
