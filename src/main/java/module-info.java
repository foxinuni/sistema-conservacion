module sistemas.conservacion {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.oracle.database.jdbc;
    requires org.apache.logging.log4j;


    opens sistemas.conservacion to javafx.fxml;
    exports sistemas.conservacion;
}