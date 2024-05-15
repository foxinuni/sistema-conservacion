package sistemas.conservacion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;

public class Database {
    private static final String DATABASE_URL = System.getenv("DATABASE_URL");
    private static final String DATABASE_USER = System.getenv("DATABASE_USER");
    private static final String DATABASE_PASSWORD = System.getenv("DATABASE_PASSWORD");
    private static final Logger log = LogManager.getLogger(Database.class);

    public static Optional<Connection> connect() {
        // Try Oracle DB JDBC driver
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            log.error("Oracle JDBC Driver is not found", e);
            return Optional.empty();
        }

        // Connect to Oracle DB
        try {
            DriverManager.setLoginTimeout(10); // Fail after 10 seconds if connection can't be established
            final Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            return Optional.of(connection);
        } catch (Exception e) {
            log.error("Database connection failed", e);
            return Optional.empty();
        }
    }
}
