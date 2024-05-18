package sistemas.conservacion.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sistemas.conservacion.Page;

import java.net.URL;

public class LayoutController {
    private static final Logger log = LogManager.getLogger(LayoutController.class);
    private Page currentPage = Page.OBSERVACION;

    @FXML
    public BorderPane mainPane;

    @FXML
    public VBox leftMenu;

    public void initialize() {
        if (loadPage(currentPage)) {
            log.info("Loaded page: {}", currentPage.getTitle());
        }
    }

    public boolean loadPage(Page page) {
        // Load the view
        final URL resource = LayoutController.class.getResource(page.getFxml());
        if (resource == null) {
            log.error("Failed to find FXML file for page: {}", page.getTitle());
            return false;
        }

        try {
            // Load the FXML file
            final Node pane = FXMLLoader.load(resource);

            // Set the current page
            mainPane.setCenter(pane);
            currentPage = page;

            // Set the selected nav item
            selectNavigation(page.getId());
        } catch (Exception e) {
            log.error("Failed to load FXML file for page: {}", page.getTitle(), e);
            return false;
        }

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
}
