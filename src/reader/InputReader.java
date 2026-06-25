package reader;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class InputReader {
    private static volatile InputReader instance;
    private final BufferedReader reader;

    private InputReader(InputStream is) {
        reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
    }

    public static InputReader getInstance() {
        if (instance == null) {
            synchronized (InputReader.class) {
                if (instance == null) {
                    instance = new InputReader(System.in);
                }
            }
        }
        return instance;
    }

    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input", e);
        }
    }
}
