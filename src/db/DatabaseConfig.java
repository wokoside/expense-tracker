package db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

final class DatabaseConfig {

    private static final String RESOURCE_NAME = "application.properties";

    private final String url;
    private final String username;
    private final String password;

    private DatabaseConfig (String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    static DatabaseConfig load() {
        Properties properties = new Properties();

        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream(RESOURCE_NAME)) {
            if (input == null) {
                throw new DatabaseException ("Resource not found: " + RESOURCE_NAME);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new DatabaseException ("Failed to read " + RESOURCE_NAME, e);
        }
        return new DatabaseConfig(
                getRequired(properties, "db.url"),
                getRequired(properties, "db.username"),
                getRequired(properties, "db.password")
        );
    }

    String getUrl() {
        return url;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    private static String getRequired (Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new DatabaseException ("Required property is missing or blank: " + key);
        }
        return value.trim();
    }
}