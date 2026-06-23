package category;

import reader.InputReader;

class CategoryView {
    private static final CategoryView INSTANCE = new CategoryView();

    private CategoryView() {
    }

    static CategoryView getInstance() {
        return INSTANCE;
    }

    String readLine(String message) {
        return InputReader.readLineWithMessage(message);
    }

    void showMessage(String message) {
        System.out.println(message);
    }
}
