package category;

import reader.InputReader;

class CategoryView {
    private static volatile CategoryView instance;
    private final InputReader inputReader;

    private CategoryView(InputReader inputReader) {
        this.inputReader = inputReader;
    }

    static CategoryView getInstance() {
        if (instance == null) {
            synchronized (CategoryView.class) {
                if (instance == null) {
                    instance = new CategoryView(InputReader.getInstance());
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
