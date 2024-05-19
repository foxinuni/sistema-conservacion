package sistemas.conservacion.providers;

import sistemas.conservacion.types.Database;
import jakarta.inject.Provider;
import java.sql.Connection;

public class ConnectionProvider implements Provider<Connection> {
    @Override
    public Connection get() {
        return Database.connect()
                .orElseThrow(() -> new RuntimeException("No se pudo conectar a la base de datos"));
    }
}
