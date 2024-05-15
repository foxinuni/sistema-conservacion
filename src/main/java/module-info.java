module sistemas.conservacion {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.oracle.database.jdbc;


    opens sistemas.conservacion to javafx.fxml;
    exports sistemas.conservacion;
}