package sistemas.conservacion;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;

public class Application extends javafx.application.Application {
    private static final Logger log = LogManager.getLogger(Application.class);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        final Connection connection = Database.connect()
                .orElseThrow(() -> new RuntimeException("Database connection failed"));

        log.info("Database connection successful");

        launch();
    }
}