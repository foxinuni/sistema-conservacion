module sistemas.conservacion {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.oracle.database.jdbc;
    requires org.apache.logging.log4j;
    requires com.google.guice;
    requires jakarta.inject;

    opens sistemas.conservacion to javafx.fxml;
    opens sistemas.conservacion.models to javafx.fxml;
    opens sistemas.conservacion.providers to com.google.guice;
    opens sistemas.conservacion.controllers to com.google.guice;

    exports sistemas.conservacion;
    exports sistemas.conservacion.stores;
    exports sistemas.conservacion.models;
    exports sistemas.conservacion.controllers;
    exports sistemas.conservacion.types;
    opens sistemas.conservacion.types to com.google.guice, javafx.fxml;
}