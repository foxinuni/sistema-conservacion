package sistemas.conservacion.stores;

import sistemas.conservacion.models.EstadoConservacion;

import java.util.List;
import java.util.Optional;

public interface EstadoConservacionStore {
    Optional<EstadoConservacion> get(String id);
    List<EstadoConservacion> getAll();
    boolean create(EstadoConservacion estadoConservacion);
    boolean update(EstadoConservacion estadoConservacion);
    boolean delete(String id);
}
