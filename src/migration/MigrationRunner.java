package migration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class MigrationRunner {
    private static final String MIGRATION_PATH = "migrations/";

    public static void run(Connection connection) {
        Throwable originalE = null;
        boolean autoCommit = true;
        try {
            autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            initMigration(connection, "categories.sql");
            initMigration(connection, "expenses.sql");
            connection.commit();
        } catch (Exception e) {
            originalE = e;
            try {
                connection.rollback();
            } catch (SQLException ex) {
                originalE.addSuppressed(ex);
            }
            throw new RuntimeException("Failed migration", originalE);
        } finally {
            try {
                connection.setAutoCommit(autoCommit);
            } catch (SQLException e) {
                if (originalE != null) {
                    originalE.addSuppressed(e);
                }
            }
        }
    }

    private static void initMigration(Connection connection, String sqlName) throws SQLException {
        InputStream is = MigrationRunner.class.getClassLoader().getResourceAsStream(MIGRATION_PATH + sqlName);
        if (is == null) throw new IllegalStateException("Migration file not found: " + sqlName);
        try (is; BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)); Statement statement = connection.createStatement()) {
            String sql = br.lines().collect(Collectors.joining("\n"));
            statement.execute(sql);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read migration file: " + sqlName, e);
        }
    }

}

