module sistemas.conservacion.sistemaconservacion {
    requires javafx.controls;
    requires javafx.fxml;


    opens sistemas.conservacion.sistemaconservacion to javafx.fxml;
    exports sistemas.conservacion.sistemaconservacion;
}