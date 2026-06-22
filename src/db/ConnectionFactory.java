package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionFactory {
    private static final DatabaseConfig CFG = DatabaseConfig.load();

    private ConnectionFactory() {
    }

    public static Connection openConnection() {
        try {
            Connection connection = DriverManager.getConnection(CFG.getUrl(), CFG.getUsername(), CFG.getPassword());
            return connection;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to database : ", e);
        }
    }
}

