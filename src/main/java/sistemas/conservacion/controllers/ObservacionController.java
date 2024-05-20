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
import sistemas.conservacion.models.Observacion;
import sistemas.conservacion.stores.EspecieStore;
import sistemas.conservacion.stores.ObservacionStore;
import sistemas.conservacion.types.NodeLoader;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.function.Function;

public class ObservacionController implements NodeLoader {
    private static final Logger log = LogManager.getLogger(ObservacionController.class);
    private Function<Node, Boolean> loaderFunc;

    @Inject private Injector injector;
    @Inject private ObservacionStore observacionStore;
    @Inject private EspecieStore especieStore;

    @FXML public Button eliminarButton;
    @FXML public Button detallesButton;
    @FXML public TableView<Observacion> observationTable;
    @FXML public TableColumn<Observacion, String> observationId;
    @FXML public TableColumn<Observacion, String> observationEspecie;
    @FXML public TableColumn<Observacion, Integer> observationCantidad;
    @FXML public TableColumn<Observacion, String> observationLatitud;
    @FXML public TableColumn<Observacion, String> observationLongitud;
    @FXML public TableColumn<Observacion, Timestamp> observationFecha;
    @FXML public TableColumn<Observacion, String> observationNota;

    @Override
    public void setLoaderFunc(Function<Node, Boolean> loaderFunc) {
        this.loaderFunc = loaderFunc;
    }

    public void initialize() {
        // Initialize the cells
        observationId.setCellValueFactory(new PropertyValueFactory<>("id"));
        observationCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidadObservada"));
        observationLatitud.setCellValueFactory(new PropertyValueFactory<>("latitud"));
        observationLongitud.setCellValueFactory(new PropertyValueFactory<>("longitud"));
        observationFecha.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
        observationNota.setCellValueFactory(new PropertyValueFactory<>("nota"));

        observationEspecie.setCellValueFactory(cellData -> {
            final Observacion observacion = cellData.getValue();
            final Especie especie = especieStore.get(observacion.getEspecieId()).orElse(null);
            return new SimpleStringProperty(especie != null ? especie.getNombreComun() : "");
        });

        // Load the observations
        loadObservations();
        onTableSelection();

        // Add event to table
        observationTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            onTableSelection();
        });
    }

    public void loadObservations() {
        observationTable.getItems().clear();
        observationTable.getItems().addAll(observacionStore.getAll());
    }

    public void onTableSelection() {
        final Observacion observacion = observationTable.getSelectionModel().getSelectedItem();
        detallesButton.setVisible(observacion != null);
        eliminarButton.setVisible(observacion != null);
    }

    public void onCrearClick(ActionEvent actionEvent) {
        if (loaderFunc == null) {
            log.error("Attempted to load node without node loader");
            return;
        }

        // Load the view
        final InputStream resource = getClass().getResourceAsStream("/sistemas/conservacion/observacion-form-view.fxml");
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
                log.error("Failed invoke load for FXML file for page: {}", "ObservacionController");
            }
        } catch (Exception e) {
            log.error("Failed to load FXML file for page: {}", "ObservacionController", e);
        }
    }

    public void onEliminarClick(ActionEvent actionEvent) {
        final Observacion observacion = observationTable.getSelectionModel().getSelectedItem();
        if (observacion == null) {
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

        if (!observacionStore.delete(observacion.getId())) {
            log.error("No se pudo eliminar el registro");
            return;
        }

        loadObservations();
    }

    public void onDetallesClick(ActionEvent actionEvent) {
        final Observacion observacion = observationTable.getSelectionModel().getSelectedItem();
        if (observacion == null) {
            log.error("No se selecciono un registro para ver detalles");
            return;
        }

        if (loaderFunc == null) {
            log.error("Attempted to load node without node loader");
            return;
        }

        // Load the view
        final InputStream resource = getClass().getResourceAsStream("/sistemas/conservacion/observacion-form-view.fxml");
        if (resource == null) {
            log.error("Failed to find FXML file for page: {}", "EstadoConservacion");
            return;
        }

        try {
            final FXMLLoader fxmlLoader = injector.getInstance(FXMLLoader.class);
            final Node pane = fxmlLoader.load(resource);

            // Pass data to the controller
            if (fxmlLoader.getController() instanceof ObservacionFormController formController) {
                formController.setLoaderFunc(loaderFunc);
                formController.setObservacion(observacion);
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
