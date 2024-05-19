package sistemas.conservacion.controllers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sistemas.conservacion.models.Especie;
import sistemas.conservacion.models.EstadoConservacion;
import sistemas.conservacion.stores.EspecieStore;
import sistemas.conservacion.stores.EstadoConservacionStore;
import sistemas.conservacion.types.NodeLoader;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

public class EspecieFormController implements NodeLoader {
    private static final Logger log = LogManager.getLogger(EspecieController.class);
    private Function<Node, Boolean> loaderFunc;
    private Especie especie;

    @Inject private EspecieStore especieStore;
    @Inject private EstadoConservacionStore estadoStore;
    @Inject private Injector injector;

    @FXML public HBox errorHBox;
    @FXML public Label errorText;
    @FXML public VBox formVBox;
    @FXML public TextField formId;
    @FXML public TextField formComun;
    @FXML public TextField formCientifico;
    @FXML public TextField formHabitad;
    @FXML public TextArea formCaracteristicas;
    @FXML public ComboBox<EstadoConservacion> formEstado;

    @Override
    public void setLoaderFunc(Function<Node, Boolean> loaderFunc) {
        this.loaderFunc = loaderFunc;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;

        if (especie != null) {
            formId.setText(especie.getId());
            formComun.setText(especie.getNombreComun());
            formCientifico.setText(especie.getNombreCientifico());
            formHabitad.setText(especie.getHabitad());
            formCaracteristicas.setText(especie.getCaracteristicasFisicas());
            estadoStore.get(especie.getEstadoConservacionId())
                    .ifPresent(estado -> formEstado.setValue(estado));

            formId.setDisable(true);
        }
    }

    public void initialize() {
        formVBox.getChildren().remove(errorHBox);
        formEstado.setConverter(new EstadoConservacion.EstadoStringConverter());
        updateEstadosConservacion();
    }

    public void updateEstadosConservacion() {
        formEstado.getItems().clear();
        formEstado.getItems().addAll(estadoStore.getAll());
    }

    public void onGuardarClick() {
        final String id = formId.getText();
        final String comun = formComun.getText();
        final String cientifico = formCientifico.getText();
        final String habitad = formHabitad.getText();
        final String caracteristicas = formCaracteristicas.getText();
        final EstadoConservacion estado = formEstado.getValue();

        // Check if any fields are empty
        if (id.isEmpty() || comun.isEmpty() || cientifico.isEmpty() || habitad.isEmpty() || caracteristicas.isEmpty() || estado == null) {
            displayError("Todos los campos son requeridos!");
            return;
        }

        // Check if the ID already exists
        final boolean exists = especieStore.get(id).isPresent();
        if (especie == null) {
            if (exists) {
                displayError("Ya existe un registro con ese ID!");
                return;
            }

            Especie nuevaEspecie = new Especie(id, comun, cientifico, caracteristicas, habitad, estado.getId());
            if (!especieStore.create(nuevaEspecie)) {
                displayError("Ocurrio un error al intentar guardar el registro!");
                return;
            }
        } else {
            if (!exists) {
                displayError("No se encontro el registro a actualizar!");
                return;
            }

            especie.setId(id);
            especie.setNombreComun(comun);
            especie.setNombreCientifico(cientifico);
            especie.setCaracteristicasFisicas(caracteristicas);
            especie.setHabitad(habitad);
            especie.setEstadoConservacionId(estado.getId());

            if (!especieStore.update(especie)) {
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

        final InputStream resource = getClass().getResourceAsStream("/sistemas/conservacion/especie-view.fxml");
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
