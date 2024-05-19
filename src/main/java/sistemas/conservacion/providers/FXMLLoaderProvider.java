package sistemas.conservacion.providers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import jakarta.inject.Provider;
import javafx.fxml.FXMLLoader;

import java.nio.charset.StandardCharsets;

public class FXMLLoaderProvider implements Provider<FXMLLoader> {
    @Inject
    private Injector injector;

    @Override
    public FXMLLoader get() {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(injector::getInstance);
        loader.setLocation(getClass().getResource("/sistemas/conservacion"));
        loader.setCharset(StandardCharsets.UTF_8);
        return loader;
    }
}
