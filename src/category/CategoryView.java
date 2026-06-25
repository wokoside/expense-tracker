package category;

import reader.InputReader;

import java.io.InputStream;

class CategoryView {
    private static volatile CategoryView instance;
    private final InputReader inputReader;

    private CategoryView(InputReader inputReader) {
        this.inputReader = inputReader;
    }

    static CategoryView getInstance(InputReader inputReader) {
        if (instance == null) {
            synchronized (CategoryView.class) {
                if (instance == null) {
                    instance = new CategoryView(inputReader);
                }
            }
        }
        return instance;
    }

    String readLine() {
        return inputReader.readLine();
    }

    void showMessage(String message) {
        System.out.println(message);
    }
}
