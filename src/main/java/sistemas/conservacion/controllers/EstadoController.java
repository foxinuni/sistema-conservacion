package sistemas.conservacion.controllers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sistemas.conservacion.models.EstadoConservacion;
import sistemas.conservacion.stores.EstadoConservacionStore;
import sistemas.conservacion.types.NodeLoader;

import java.io.InputStream;
import java.util.function.Function;

public class EstadoController implements NodeLoader {
    private static final Logger log = LogManager.getLogger(EstadoController.class);
    private Function<Node, Boolean> loaderFunc;

    @Inject private Injector injector;
    @Inject private EstadoConservacionStore estadoStore;

    @FXML public TableView<EstadoConservacion> estadoTable;
    @FXML public TableColumn<EstadoConservacion, String> estadoId;
    @FXML public TableColumn<EstadoConservacion, String> estadoContenido;
    @FXML public TableColumn<EstadoConservacion, String> estadoDetalles;

    @Override
    public void setLoaderFunc(Function<Node, Boolean> loaderFunc) {
        this.loaderFunc = loaderFunc;
    }

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

    // buttons
    public void onCrearClick() {
        if (loaderFunc == null) {
            log.error("Attempted to load node without node loader");
            return;
        }

        // Load the view
        final InputStream resource = getClass().getResourceAsStream("/sistemas/conservacion/estado-form-view.fxml");
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
}
