package sistemas.conservacion.controllers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sistemas.conservacion.models.Especie;
import sistemas.conservacion.models.Observacion;
import sistemas.conservacion.stores.EspecieStore;
import sistemas.conservacion.stores.ObservacionStore;
import sistemas.conservacion.types.NodeLoader;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.function.Function;

public class ObservacionFormController implements NodeLoader {
    private static final Logger log = LogManager.getLogger(ObservacionFormController.class);
    private Function<Node, Boolean> loaderFunc;
    private Observacion observacion;

    @Inject private EspecieStore especieStore;
    @Inject private ObservacionStore observacionStore;
    @Inject private Injector injector;

    @FXML public HBox errorHBox;
    @FXML public Label errorText;
    @FXML public VBox formVBox;
    @FXML public TextField formId;
    @FXML public TextField formCantidad;
    @FXML public TextField formLatitud;
    @FXML public TextField formLongitud;
    @FXML public TextArea formNotas;
    @FXML public DatePicker formFecha;
    @FXML public ComboBox<Especie> formEspecie;

    @Override
    public void setLoaderFunc(Function<Node, Boolean> loaderFunc) {
        this.loaderFunc = loaderFunc;
    }

    public void setObservacion(Observacion observacion) {
        this.observacion = observacion;

        if (observacion != null) {
            formId.setText(observacion.getId());
            formCantidad.setText(String.valueOf(observacion.getCantidadObservada()));
            formLatitud.setText(String.format("%.6f", observacion.getLatitud()));
            formLongitud.setText(String.format("%.6f", observacion.getLongitud()));
            formNotas.setText(observacion.getNota());
            formFecha.setValue(observacion.getFechaHora().toLocalDateTime().toLocalDate());
            especieStore.get(observacion.getEspecieId())
                    .ifPresent(especie -> formEspecie.setValue(especie));

            formId.setDisable(true);
        }
    }

    public void initialize() {
        formVBox.getChildren().remove(errorHBox);
        formEspecie.setConverter(new Especie.EspecieStringConverter());
        updateEspecies();
    }

    public void updateEspecies() {
        formEspecie.getItems().clear();
        formEspecie.getItems().addAll(especieStore.getAll());
    }

    public void onGuardarClick() {
        try {
            final String id = formId.getText();
            final int cantidad = Integer.parseInt(formCantidad.getText());
            final float latitud = Float.parseFloat(formLatitud.getText());
            final float longitud = Float.parseFloat(formLongitud.getText());
            final String notas = formNotas.getText();
            final Especie especie = formEspecie.getValue();
            final Timestamp fecha = Timestamp.valueOf(formFecha.getValue().atStartOfDay());

            // Check if any fields are empty
            if (id.isEmpty() || notas.isEmpty() || especie == null) {
                displayError("Todos los campos son requeridos!");
                return;
            }

            // Check if the ID already exists
            final boolean exists = observacionStore.get(id).isPresent();
            if (observacion == null) {
                if (exists) {
                    displayError("Ya existe un registro con ese ID!");
                    return;
                }

                Observacion nuevaObservacion = new Observacion(id, especie.getId(), fecha, cantidad, latitud, longitud, notas);
                if (!observacionStore.create(nuevaObservacion)) {
                    displayError("Ocurrio un error al intentar guardar el registro!");
                    return;
                }
            } else {
                if (!exists) {
                    displayError("No se encontro el registro a actualizar!");
                    return;
                }

                observacion.setId(id);
                observacion.setEspecieId(especie.getId());
                observacion.setFechaHora(fecha);
                observacion.setCantidadObservada(cantidad);
                observacion.setLatitud(latitud);
                observacion.setLongitud(longitud);
                observacion.setNota(notas);

                if (!observacionStore.update(observacion)) {
                    displayError("Ocurrio un error al intentar actualizar el registro!");
                    return;
                }
            }
        } catch (Exception e) {
            displayError("Existen campos que tienen datos invalidos!");
            return;
        }

        onDescartarClick();
    }

    public void onDescartarClick() {
        if (loaderFunc == null) {
            log.error("Attempted to load node without node loader");
            return;
        }

        final InputStream resource = getClass().getResourceAsStream("/sistemas/conservacion/observacion-view.fxml");
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
