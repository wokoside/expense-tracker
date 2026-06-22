package migration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class MigrationRunner {

    private static final Path MIGRATION_PATH = Path.of("resources", "migrations");

    private MigrationRunner() {
    }

    public static void run(Connection connection) {
        Objects.requireNonNull(connection, "connection must not be null");
        List<Path> migrationFiles = findMigrationFiles();
        boolean previousAutoCommit = getAutoCommit(connection);
        Exception failure = null;
        try {
            connection.setAutoCommit(false);
            for (Path migrationFile : migrationFiles) {
                executeMigration(connection, migrationFile);
            }
            connection.commit();
        } catch (Exception e) {
            failure = e;
            rollback(connection, e);
            throw new MigrationException("Failed to execute migrations", e);
        } finally {
            restoreAutoCommit(connection, previousAutoCommit, failure);
        }
    }

    private static List<Path> findMigrationFiles() {
        if (!Files.isDirectory(MIGRATION_PATH)) {
            throw new MigrationException("Migration directory not found: " + MIGRATION_PATH.toAbsolutePath());
        }
        try (Stream<Path> files = Files.list(MIGRATION_PATH)) {
            return files
                    .filter(Files::isRegularFile)
                    .filter(MigrationRunner::isSqlFile)
                    .sorted(Comparator.comparing(
                            path -> path.getFileName().toString()
                    ))
                    .toList();
        } catch (IOException e) {
            throw new MigrationException("Failed to read migration directory: " + MIGRATION_PATH.toAbsolutePath(), e);
        }
    }

    private static boolean isSqlFile(Path path) {
        return path.getFileName()
                .toString()
                .toLowerCase()
                .endsWith(".sql");
    }

    private static void executeMigration(Connection connection, Path migrationFile) throws IOException, SQLException {
        String sql = Files.readString(migrationFile, StandardCharsets.UTF_8);
        if (sql.isBlank()) {
            return;
        }
        System.out.println("Executing migration: " + migrationFile.getFileName());
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private static boolean getAutoCommit(Connection connection) {
        try {
            return connection.getAutoCommit();
        } catch (SQLException e) {
            throw new MigrationException("Failed to read connection state", e);
        }
    }

    private static void rollback(Connection connection, Exception originalException) {
        try {
            connection.rollback();
        } catch (SQLException rollbackException) {
            originalException.addSuppressed(rollbackException);
        }
    }

    private static void restoreAutoCommit(Connection connection, boolean previousAutoCommit, Exception originalException) {
        try {
            connection.setAutoCommit(previousAutoCommit);
        } catch (SQLException restoreException) {
            if (originalException != null) {
                originalException.addSuppressed(restoreException);
            } else {
                throw new MigrationException("Failed to restore connection state", restoreException);
            }
        }
    }
}