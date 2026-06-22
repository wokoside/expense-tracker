package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class ConnectionFactory {

    private static final String USERNAME = "db.username";
    private static final String PASSWORD = "db.password";
    private static final String URL = "db.url";
    private static final String POOL_SIZE = "db.pool.size";
    private static BlockingQueue<Connection> pool;
    private static final List<Connection> allConnections = new ArrayList<>();

    static {
        initPool();
    }

    private ConnectionFactory() {
    }

    private static Connection connect() {
        try {
            Connection connection = DriverManager.getConnection(PropertiesDB.get(URL), PropertiesDB.get(USERNAME), PropertiesDB.get(PASSWORD));
            allConnections.add(connection);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        try {
            Connection connection = pool.take();
            return new ConnectionWrapper(connection, pool);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private static void initPool() {
        String poolSize = PropertiesDB.get(POOL_SIZE);
        int size = Integer.valueOf(poolSize);
        pool = new ArrayBlockingQueue<>(size);
        for (int i = 0; i < size; i++) {
            pool.add(connect());
        }
    }

    public static void closePool() {
        RuntimeException rE = null;
        for (Connection connection : allConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                if (rE != null) rE.addSuppressed(e);
                else {
                    rE = new RuntimeException("Failed to close connection");
                }
            }
        }
        if (rE != null) {
            throw rE;
        }
    }
}

