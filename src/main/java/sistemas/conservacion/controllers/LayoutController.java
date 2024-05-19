package sistemas.conservacion.controllers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sistemas.conservacion.types.NodeLoader;
import sistemas.conservacion.types.Subview;

import java.io.InputStream;

public class LayoutController {
    private static final Logger log = LogManager.getLogger(LayoutController.class);
    private Subview currentSubview;

    @Inject
    private Injector injector;

    @FXML
    public BorderPane mainPane;

    @FXML
    public VBox leftMenu;

    public void initialize() {
        if (loadSubview(Subview.ESPECIE)) {
            log.info("Loaded page: {}", currentSubview.getTitle());
        }
    }

    public boolean loadSubview(Subview subview) {
        if (currentSubview == subview) {
            return true;
        }

        // Load the view
        final InputStream resource = LayoutController.class.getResourceAsStream(subview.getFxml());
        if (resource == null) {
            log.error("Failed to find FXML file for page: {}", subview.getTitle());
            return false;
        }

        try {
            // Load the FXML file
            final FXMLLoader fxmlLoader = injector.getInstance(FXMLLoader.class);
            final Node pane = fxmlLoader.load(resource);

            // Set the loader function
            if (fxmlLoader.getController() instanceof NodeLoader nodeLoader) {
                nodeLoader.setLoaderFunc(this::internalLoadPane);
            }

            // Set the current page
            mainPane.setCenter(pane);
            currentSubview = subview;

            // Set the selected nav item
            selectNavigation(subview.getId());
        } catch (Exception e) {
            log.error("Failed to load FXML file for page: {}", subview.getTitle(), e);
            return false;
        }

        return true;
    }

    public boolean internalLoadPane(Node pane) {
        if (pane == null) {
            log.error("Attempted to load a null pane");
            return false;
        }

        mainPane.setCenter(pane);
        return true;
    }

    public void selectNavigation(String id) {
        boolean found = false;

        for (Node node : leftMenu.getChildren()) {
            if (node instanceof Button button) {
                final String buttonId = button.getId();
                if (buttonId == null) {
                    continue;
                }

                if (buttonId.equals(id)) {
                    button.getStyleClass().add("selected");
                    found = true; // Found the nav item
                } else {
                    button.getStyleClass().remove("selected");
                }
            }
        }

        if (!found) {
            log.error("Failed to find navigation item with ID: {}", id);
        }
    }

    // Button event handlers
    public void onEstadoClick() {
        loadSubview(Subview.ESTADO);
    }

    public void onEspecieClick() {
        loadSubview(Subview.ESPECIE);
    }

    public void onObservacionClick() {
        loadSubview(Subview.OBSERVACION);
    }
}
