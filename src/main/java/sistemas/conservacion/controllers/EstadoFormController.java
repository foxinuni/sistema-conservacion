package sistemas.conservacion.controllers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sistemas.conservacion.models.EstadoConservacion;
import sistemas.conservacion.stores.EstadoConservacionStore;
import sistemas.conservacion.types.NodeLoader;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

public class EstadoFormController implements NodeLoader {
    private static final Logger log = LogManager.getLogger(EstadoController.class);
    private Function<Node, Boolean> loaderFunc;
    private EstadoConservacion estadoConservacion;

    @Inject private EstadoConservacionStore estadoStore;
    @Inject private Injector injector;

    @FXML public HBox errorHBox;
    @FXML public Label errorText;
    @FXML public VBox formVBox;
    @FXML public TextField formId;
    @FXML public TextField formEstado;
    @FXML public TextArea formDetalles;

    @Override
    public void setLoaderFunc(Function<Node, Boolean> loaderFunc) {
        this.loaderFunc = loaderFunc;
    }

    public void setEstadoConservacion(EstadoConservacion estadoConservacion) {
        this.estadoConservacion = estadoConservacion;

        if (estadoConservacion != null) {
            formId.setText(estadoConservacion.getId());
            formEstado.setText(estadoConservacion.getEstadoConservacion());
            formDetalles.setText(estadoConservacion.getDefinicionEstado());

            formId.setDisable(true);
        }
    }

    public void initialize() {
        formVBox.getChildren().remove(errorHBox);
    }

    public void onGuardarClick() {
        final String id = formId.getText();
        final String estado = formEstado.getText();
        final String detalles = formDetalles.getText();

        // Check if any fields are empty
        if (id.isEmpty() || estado.isEmpty() || detalles.isEmpty()) {
            displayError("Todos los campos son requeridos!");
            return;
        }

        // Check if the ID already exists
        final boolean exists = estadoStore.get(id).isPresent();
        if (estadoConservacion == null) {
            if (exists) {
                displayError("Ya existe un registro con ese ID!");
                return;
            }

            EstadoConservacion nuevoEstado = new EstadoConservacion(id, estado, detalles);
            if (!estadoStore.create(nuevoEstado)) {
                displayError("Ocurrio un error al intentar guardar el registro!");
                return;
            }
        } else {
            if (!exists) {
                displayError("No se encontro el registro a actualizar!");
                return;
            }

            estadoConservacion.setId(id);
            estadoConservacion.setEstadoConservacion(estado);
            estadoConservacion.setDefinicionEstado(detalles);

            if (!estadoStore.update(estadoConservacion)) {
                displayError("Ocurrio un error al intentar actualizar el registro!");
                return;
            }
        }

        onDescartarClick();
    }

    public void onDescartarClick() {
        if (loaderFunc == null) {
            log.error("Attempted to load node without node loader");
            return;
        }

        final InputStream resource = getClass().getResourceAsStream("/sistemas/conservacion/estado-view.fxml");
        if (resource == null) {
            log.error("Failed to find FXML file for page: Estado");
            return;
        }

        try {
            final FXMLLoader fxmlLoader = injector.getInstance(FXMLLoader.class);
            final Node pane = fxmlLoader.load(resource);

            if (fxmlLoader.getController() instanceof NodeLoader nodeLoader) {
                nodeLoader.setLoaderFunc(loaderFunc);
            }

            if (!loaderFunc.apply(pane)) {
                log.error("Failed invoke load for FXML file for page: {}", "EstadoFormController");
            }
        } catch (Exception e) {
            log.error("Failed to load FXML file for page: {}", "EstadoFormController", e);
        }

    }

    public void onIdTyped() {
        final String text = formId.getText();
        if (text.length() > 6) {
            formId.setText(text.substring(0, 6));
        }
    }

    public void displayError(String message) {
        errorText.setText(message);

        // Add the error box to the top of the form
        final List<Node> children = formVBox.getChildren();
        if (!children.contains(errorHBox)) {
            children.add(0, errorHBox);
        }
    }
}
