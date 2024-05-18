package sistemas.conservacion;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sistemas.conservacion.stores.*;

import java.io.IOException;
import java.sql.Connection;

public class Application extends javafx.application.Application {
    private static final Logger log = LogManager.getLogger(Application.class);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("layout-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Sistemas de Conservación");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        final Connection connection = Database.connect()
                .orElseThrow(() -> new RuntimeException("Database connection failed"));

        log.info("Database connection successful");

        // Stores
        final EstadoConservacionStore estadoConservacionStore = new OracleEstadoConservacionStore(connection);
        final EspecieStore especieStore = new OracleEspecieStore(connection);
        final ObservacionStore observacionStore = new OracleObservacionStore(connection);

        launch();
    }
}