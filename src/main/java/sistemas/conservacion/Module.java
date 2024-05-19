package sistemas.conservacion;

import com.google.inject.AbstractModule;
import javafx.fxml.FXMLLoader;
import sistemas.conservacion.providers.ConnectionProvider;
import sistemas.conservacion.providers.FXMLLoaderProvider;
import sistemas.conservacion.stores.*;

import java.sql.Connection;

public class Module extends AbstractModule {
    @Override
    protected void configure() {
        // Providers
        bind(Connection.class).toProvider(ConnectionProvider.class);
        bind(FXMLLoader.class).toProvider(FXMLLoaderProvider.class);

        // Stores
        bind(EspecieStore.class).to(OracleEspecieStore.class);
        bind(ObservacionStore.class).to(OracleObservacionStore.class);
        bind(EstadoConservacionStore.class).to(OracleEstadoConservacionStore.class);
    }
}
