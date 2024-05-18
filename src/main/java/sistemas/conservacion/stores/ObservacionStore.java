package sistemas.conservacion.stores;

import sistemas.conservacion.models.Observacion;

import java.util.List;
import java.util.Optional;

public interface ObservacionStore {
    Optional<Observacion> get(String id);
    List<Observacion> getAll();
    boolean create(Observacion observacion);
    boolean update(Observacion observacion);
    boolean delete(String id);
}
