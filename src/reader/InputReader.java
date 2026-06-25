package reader;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class InputReader {

    private final BufferedReader reader;

    public InputReader(InputStream is) {
        reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
    }

    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input", e);
        }
    }
}
