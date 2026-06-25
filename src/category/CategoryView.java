package category;

import reader.InputReader;

class CategoryView {
    private static volatile CategoryView instance;

    private CategoryView() {
    }

    static CategoryView getInstance() {
        if (instance == null) {
            synchronized (CategoryView.class) {
                if (instance == null) {
                    instance = new CategoryView();
                }
            }
        }
        return instance;
    }

    String readLine(String message) {
        return InputReader.readLineWithMessage(message);
    }

    void showMessage(String message) {
        System.out.println(message);
    }
}
