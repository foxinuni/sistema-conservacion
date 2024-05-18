package sistemas.conservacion.stores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sistemas.conservacion.models.Especie;

public class OracleEspecieStore implements EspecieStore {
    private static final Logger log = LogManager.getLogger(OracleEspecieStore.class);
    private final Connection connection;
    
    public OracleEspecieStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Especie> get(String id) {
        final String query = "SELECT id, nombre_cientifico, nombre_comun, caracteristicas_fisicas, habitad, id_estado_conservacion FROM especies WHERE id = ?";

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            final ResultSet result = statement.executeQuery();

            if (result.next()) {
                final Especie especie = new Especie(
                    result.getString("id"),
                    result.getString("nombre_cientifico"),
                    result.getString("nombre_comun"),
                    result.getString("caracteristicas_fisicas"),
                    result.getString("habitad"),
                    result.getString("id_estado_conservacion")
                );

                return Optional.of(especie);
            }
        } catch (SQLException e) {
            log.error("No se pudo obtener especie", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Especie> getAll() {
        final String query = "SELECT id, nombre_cientifico, nombre_comun, caracteristicas_fisicas, habitad, id_estado_conservacion FROM especies";
        final List<Especie> especies = new ArrayList<>();

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            final ResultSet result = statement.executeQuery();

            while (result.next()) {
                especies.add(new Especie(
                    result.getString("id"),
                    result.getString("nombre_cientifico"),
                    result.getString("nombre_comun"),
                    result.getString("caracteristicas_fisicas"),
                    result.getString("habitad"),
                    result.getString("id_estado_conservacion")
                ));
            }
        } catch (SQLException e) {
            log.error("No se pudieron obtener especies", e);
        }

        return especies;
    }

    @Override
    public boolean create(Especie especie) {
        final String query = "INSERT INTO especies (id, nombre_cientifico, nombre_comun, caracteristicas_fisicas, habitad, id_estado_conservacion) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, especie.id);
            statement.setString(2, especie.nombre_cientifico);
            statement.setString(3, especie.nombre_comun);
            statement.setString(4, especie.caracteristicas_fisicas);
            statement.setString(5, especie.habitad);
            statement.setString(6, especie.id_estado_conservacion);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error("No se pudo crear especie", e);
            return false;
        }
    }

    @Override
    public boolean update(Especie especie) {
        final String query = "UPDATE especies SET nombre_cientifico = ?, nombre_comun = ?, caracteristicas_fisicas = ?, habitad = ?, id_estado_conservacion = ? WHERE id = ?";

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, especie.nombre_cientifico);
            statement.setString(2, especie.nombre_comun);
            statement.setString(3, especie.caracteristicas_fisicas);
            statement.setString(4, especie.habitad);
            statement.setString(5, especie.id_estado_conservacion);
            statement.setString(6, especie.id);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error("No se pudo actualizar especie", e);
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        final String query = "DELETE FROM especies WHERE id = ?";

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error("No se pudo eliminar especie", e);
            return false;
        }
    }
}
