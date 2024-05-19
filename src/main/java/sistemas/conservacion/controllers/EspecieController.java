package sistemas.conservacion.controllers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sistemas.conservacion.models.Especie;
import sistemas.conservacion.models.EstadoConservacion;
import sistemas.conservacion.stores.EspecieStore;
import sistemas.conservacion.stores.EstadoConservacionStore;
import sistemas.conservacion.types.NodeLoader;

import java.io.InputStream;
import java.util.function.Function;

public class EspecieController implements NodeLoader {
    private static final Logger log = LogManager.getLogger(EspecieController.class);
    private Function<Node, Boolean> loaderFunc;

    @Inject private Injector injector;
    @Inject private EspecieStore especieStore;
    @Inject private EstadoConservacionStore estadoStore;

    @FXML public Button eliminarButton;
    @FXML public Button detallesButton;
    @FXML public TableView<Especie> especieTable;
    @FXML public TableColumn<Especie, String> especieId;
    @FXML public TableColumn<Especie, String> especieComun;
    @FXML public TableColumn<Especie, String> especieCientifico;
    @FXML public TableColumn<Especie, String> especieHabitad;
    @FXML public TableColumn<Especie, String> especieEstado;
    @FXML public TableColumn<Especie, String> especieCaracteristicas;

    @Override
    public void setLoaderFunc(Function<Node, Boolean> loaderFunc) {
        this.loaderFunc = loaderFunc;
    }

    public void initialize() {
        // Initialize the cells
        especieId.setCellValueFactory(new PropertyValueFactory<>("id"));
        especieComun.setCellValueFactory(new PropertyValueFactory<>("nombreComun"));
        especieCientifico.setCellValueFactory(new PropertyValueFactory<>("nombreCientifico"));
        especieHabitad.setCellValueFactory(new PropertyValueFactory<>("habitad"));
        especieCaracteristicas.setCellValueFactory(new PropertyValueFactory<>("caracteristicasFisicas"));

        especieEstado.setCellValueFactory(cellData -> {
            final Especie especie = cellData.getValue();
            final EstadoConservacion estado = estadoStore.get(especie.getEstadoConservacionId()).orElse(null);
            return new SimpleStringProperty(estado != null ? estado.getEstadoConservacion() : "");
        });

        // Load the observations
        loadObservations();
        onTableSelection();

        // Add event to table
        especieTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            onTableSelection();
        });
    }

    public void loadObservations() {
        especieTable.getItems().clear();
        especieTable.getItems().addAll(especieStore.getAll());
    }

    public void onTableSelection() {
        final Especie especie = especieTable.getSelectionModel().getSelectedItem();
        detallesButton.setVisible(especie != null);
        eliminarButton.setVisible(especie != null);
    }

    public void onCrearClick(ActionEvent actionEvent) {
        if (loaderFunc == null) {
            log.error("Attempted to load node without node loader");
            return;
        }

        // Load the view
        final InputStream resource = getClass().getResourceAsStream("/sistemas/conservacion/especie-form-view.fxml");
        if (resource == null) {
            log.error("Failed to find FXML file for page: {}", "EstadoConservacion");
            return;
        }

        try {
            final FXMLLoader fxmlLoader = injector.getInstance(FXMLLoader.class);
            final Node pane = fxmlLoader.load(resource);

            // Pass data to the controller
            if (fxmlLoader.getController() instanceof NodeLoader nodeLoader) {
                nodeLoader.setLoaderFunc(loaderFunc);
            }

            // Load the pane
            if (!loaderFunc.apply(pane)) {
                log.error("Failed invoke load for FXML file for page: {}", "EstadoConservacion");
            }
        } catch (Exception e) {
            log.error("Failed to load FXML file for page: {}", "EstadoConservacion", e);
        }
    }

    public void onEliminarClick(ActionEvent actionEvent) {
        final Especie especie = especieTable.getSelectionModel().getSelectedItem();
        if (especie == null) {
            log.error("No se selecciono un registro para eliminar");
            return;
        }

        // popup confirmation
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Estas seguro que deseas eliminar el registro?",
                ButtonType.CANCEL,
                ButtonType.OK
        );
        alert.showAndWait();

        if (alert.getResult().getButtonData().isCancelButton()) {
            return;
        }

        if (!especieStore.delete(especie.getId())) {
            log.error("No se pudo eliminar el registro");
            return;
        }

        loadObservations();
    }

    public void onDetallesClick(ActionEvent actionEvent) {
        final Especie especie = especieTable.getSelectionModel().getSelectedItem();
        if (especie == null) {
            log.error("No se selecciono un registro para ver detalles");
            return;
        }

        if (loaderFunc == null) {
            log.error("Attempted to load node without node loader");
            return;
        }

        // Load the view
        final InputStream resource = getClass().getResourceAsStream("/sistemas/conservacion/especie-form-view.fxml");
        if (resource == null) {
            log.error("Failed to find FXML file for page: {}", "EstadoConservacion");
            return;
        }

        try {
            final FXMLLoader fxmlLoader = injector.getInstance(FXMLLoader.class);
            final Node pane = fxmlLoader.load(resource);

            // Pass data to the controller
            if (fxmlLoader.getController() instanceof EspecieFormController formController) {
                formController.setLoaderFunc(loaderFunc);
                formController.setEspecie(especie);
            }

            // Load the pane
            if (!loaderFunc.apply(pane)) {
                log.error("Failed invoke load for FXML file for page: {}", "EstadoConservacion");
            }
        } catch (Exception e) {
            log.error("Failed to load FXML file for page: {}", "EstadoConservacion", e);
        }
    }
}
