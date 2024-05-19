package sistemas.conservacion.types;

import javafx.scene.Node;

import java.util.function.Function;

public interface NodeLoader {
    void setLoaderFunc(Function<Node, Boolean> loaderFunc);
}
