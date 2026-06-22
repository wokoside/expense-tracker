package reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class InputReader {

    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

    public static String readLineWithMessage (String message) {
        System.out.println(message);
        try {
            String line = reader.readLine();
            return line != null ? line.trim() : "";
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input", e);
        }
    }
}
