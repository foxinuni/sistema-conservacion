package sistemas.conservacion.controllers;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sistemas.conservacion.models.Especie;
import sistemas.conservacion.stores.EspecieStore;

public class EspecieController {
    @Inject
    private EspecieStore especieStore;

    @FXML public TableView<Especie> especieTable;
    @FXML public TableColumn<Especie, String> especieId;
    @FXML public TableColumn<Especie, String> especieComun;
    @FXML public TableColumn<Especie, String> especieCientifico;
    @FXML public TableColumn<Especie, String> especieHabitad;
    @FXML public TableColumn<Especie, String> especieEstado;
    @FXML public TableColumn<Especie, String> especieCaracteristicas;

    public void initialize() {
        // Initialize the cells
        especieId.setCellValueFactory(new PropertyValueFactory<>("id"));
        especieComun.setCellValueFactory(new PropertyValueFactory<>("nombreComun"));
        especieCientifico.setCellValueFactory(new PropertyValueFactory<>("nombreCientifico"));
        especieHabitad.setCellValueFactory(new PropertyValueFactory<>("habitad"));
        especieEstado.setCellValueFactory(new PropertyValueFactory<>("estadoConservacionId"));
        especieCaracteristicas.setCellValueFactory(new PropertyValueFactory<>("caracteristicasFisicas"));

        // Load the observations
        loadObservations();
    }

    public void loadObservations() {
        especieTable.getItems().clear();
        especieTable.getItems().addAll(especieStore.getAll());
    }
}
