package sistemas.conservacion.stores;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sistemas.conservacion.models.Observacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OracleObservacionStore implements ObservacionStore {
    private static final Logger log = LogManager.getLogger(OracleObservacionStore.class);
    private final Connection connection;

    @Inject
    public OracleObservacionStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Observacion> get(String id) {
        final String query = "SELECT id, id_especie, cantidad_observada, ubicacion_latitud, ubicacion_longitud, fecha_hora, notas FROM observacion WHERE id = ?";

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            final ResultSet result = statement.executeQuery();

            if (result.next()) {
                final Observacion observacion = new Observacion(
                    result.getString("id"),
                    result.getString("id_especie"),
                    result.getTimestamp("fecha_hora"),
                    result.getInt("cantidad_observada"),
                    result.getFloat("ubicacion_latitud"),
                    result.getFloat("ubicacion_longitud"),
                    result.getString("notas")
                );

                return Optional.of(observacion);
            }
        } catch (SQLException e) {
            log.error("No se pudo obtener observacion", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Observacion> getAll() {
        final String query = "SELECT id, id_especie, cantidad_observada, ubicacion_latitud, ubicacion_longitud, fecha_hora, notas FROM observacion";
        final List<Observacion> observaciones = new ArrayList<>();

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            final ResultSet result = statement.executeQuery();

            while (result.next()) {
                final Observacion observacion = new Observacion(
                    result.getString("id"),
                    result.getString("id_especie"),
                    result.getTimestamp("fecha_hora"),
                    result.getInt("cantidad_observada"),
                    result.getFloat("ubicacion_latitud"),
                    result.getFloat("ubicacion_longitud"),
                    result.getString("notas")
                );

                observaciones.add(observacion);
            }
        } catch (SQLException e) {
            log.error("No se pudo obtener observaciones", e);
        }

        return observaciones;
    }

    @Override
    public boolean create(Observacion observacion) {
        final String query = "INSERT INTO observacion (id, id_especie, cantidad_observada, ubicacion_latitud, ubicacion_longitud, fecha_hora, notas) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, observacion.id);
            statement.setString(2, observacion.especieId);
            statement.setInt(3, observacion.cantidadObservada);
            statement.setFloat(4, observacion.latitud);
            statement.setFloat(5, observacion.longitud);
            statement.setTimestamp(6, observacion.fechaHora);
            statement.setString(7, observacion.nota);
            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            log.error("No se pudo crear observacion", e);
        }

        return false;
    }

    @Override
    public boolean update(Observacion observacion) {
        final String query = "UPDATE observacion SET id_especie = ?, cantidad_observada = ?, ubicacion_latitud = ?, ubicacion_longitud = ?, fecha_hora = ?, notas = ? WHERE id = ?";

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, observacion.especieId);
            statement.setInt(2, observacion.cantidadObservada);
            statement.setFloat(3, observacion.latitud);
            statement.setFloat(4, observacion.longitud);
            statement.setTimestamp(5, observacion.fechaHora);
            statement.setString(6, observacion.nota);
            statement.setString(7, observacion.id);
            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            log.error("No se pudo actualizar observacion", e);
        }

        return false;
    }

    @Override
    public boolean delete(String id) {
        final String query = "DELETE FROM observacion WHERE id = ?";

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            log.error("No se pudo eliminar observacion", e);
        }

        return false;
    }
}
