package sistemas.conservacion.stores;

import sistemas.conservacion.models.Especie;

import java.util.List;
import java.util.Optional;

public interface EspecieStore {
    Optional<Especie> get(String id);
    List<Especie> getAll();
    boolean create(Especie especie);
    boolean update(Especie especie);
    boolean delete(String id);
}
