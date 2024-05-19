package sistemas.conservacion.providers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import jakarta.inject.Provider;
import javafx.fxml.FXMLLoader;

public class FXMLLoaderProvider implements Provider<FXMLLoader> {
    @Inject
    private Injector injector;

    @Override
    public FXMLLoader get() {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(injector::getInstance);
        loader.setLocation(getClass().getResource("/sistemas/conservacion"));
        return loader;
    }
}
