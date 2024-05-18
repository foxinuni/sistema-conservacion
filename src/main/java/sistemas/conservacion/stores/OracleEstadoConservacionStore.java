package sistemas.conservacion.stores;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sistemas.conservacion.models.EstadoConservacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OracleEstadoConservacionStore implements EstadoConservacionStore {
    private static final Logger log = LogManager.getLogger(OracleEstadoConservacionStore.class);
    private final Connection connection;

    public OracleEstadoConservacionStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<EstadoConservacion> get(String id) {
        final String query = "SELECT id, estado_conservacion, definicion_estado FROM estado_conservacion WHERE id = ?";

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            final ResultSet result = statement.executeQuery();

            if (result.next()) {
                final EstadoConservacion estadoConservacion = new EstadoConservacion(
                    result.getString("id"),
                    result.getString("estado_conservacion"),
                    result.getString("definicion_estado")
                );

                return Optional.of(estadoConservacion);
            }
        } catch (SQLException e) {
            log.error("No se pudo obtener estado de conservacion", e);
        }

        return Optional.empty();
    }

    @Override
    public List<EstadoConservacion> getAll() {
        final String query = "SELECT id, estado_conservacion, definicion_estado FROM estado_conservacion";
        final List<EstadoConservacion> estadosConservacion = new ArrayList<>();

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            final ResultSet result = statement.executeQuery();

            while (result.next()) {
                final EstadoConservacion estadoConservacion = new EstadoConservacion(
                    result.getString("id"),
                    result.getString("estado_conservacion"),
                    result.getString("definicion_estado")
                );

                estadosConservacion.add(estadoConservacion);
            }
        } catch (SQLException e) {
            log.error("No se pudo obtener estados de conservacion", e);
        }

        return estadosConservacion;
    }

    @Override
    public boolean create(EstadoConservacion estadoConservacion) {
        final String query = "INSERT INTO estado_conservacion (id, estado_conservacion, definicion_estado) VALUES (?, ?, ?)";

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, estadoConservacion.id);
            statement.setString(2, estadoConservacion.estado_conservacion);
            statement.setString(3, estadoConservacion.definicion_estado);
            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            log.error("No se pudo crear estado de conservacion", e);
        }

        return false;
    }

    @Override
    public boolean update(EstadoConservacion estadoConservacion) {
        final String query = "UPDATE estado_conservacion SET estado_conservacion = ?, definicion_estado = ? WHERE id = ?";

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, estadoConservacion.estado_conservacion);
            statement.setString(2, estadoConservacion.definicion_estado);
            statement.setString(3, estadoConservacion.id);
            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            log.error("No se pudo actualizar estado de conservacion", e);
        }

        return false;
    }

    @Override
    public boolean delete(String id) {
        final String query = "DELETE FROM estado_conservacion WHERE id = ?";

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            log.error("No se pudo eliminar estado de conservacion", e);
        }

        return false;
    }
}
