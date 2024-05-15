module sistemas.conservacion {
    requires javafx.controls;
    requires javafx.fxml;


    opens sistemas.conservacion to javafx.fxml;
    exports sistemas.conservacion;
}