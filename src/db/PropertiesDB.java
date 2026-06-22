package db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesDB {

    private static final Properties PROPERTIES = new Properties();
    private static final String RESOURCE = "application.properties";

    static {
        loadProperties();
    }

    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null) throw new IllegalStateException("Missing property");
        return value;
    }

    private static void loadProperties() {
        try (InputStream inputStream = PropertiesDB.class.getClassLoader().getResourceAsStream(RESOURCE)) {
            if (inputStream == null) {
                throw new IllegalStateException("File " + RESOURCE + " not found");
            }
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed load properties");
        }
    }
}

