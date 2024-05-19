package sistemas.conservacion;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public class Bootstrap extends Application {
    private static final Logger log = LogManager.getLogger(Bootstrap.class);
    private static final Injector injector = Guice.createInjector(new Module());

    @Override
    public void start(Stage stage) {
        // Load the main FXML file
        FXMLLoader fxmlLoader = injector.getInstance(FXMLLoader.class);

        try (InputStream fxmlStream = Bootstrap.class.getResourceAsStream("layout-view.fxml")) {
            Parent parent = fxmlLoader.load(fxmlStream);

            // Setup scene and show stage
            Scene scene = new Scene(parent, 800, 600);
            stage.setTitle("Sistemas de Conservación");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            log.error("No se pudo cargar el archivo FXML", e);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}