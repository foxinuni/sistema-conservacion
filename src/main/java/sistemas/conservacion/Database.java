package sistemas.conservacion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;

public class Database {
    private static final String DATABASE_URL = System.getenv("DATABASE_URL");
    private static final String DATABASE_USER = System.getenv("DATABASE_USER");
    private static final String DATABASE_PASSWORD = System.getenv("DATABASE_PASSWORD");

    public static Optional<Connection> connect() {
        // Try Oracle DB JDBC driver
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC Driver is not found");
            e.printStackTrace();
            return Optional.empty();
        }

        // Connect to Oracle DB
        try {
            final Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            return Optional.of(connection);
        } catch (Exception e) {
            System.out.println("Connection failed");
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
