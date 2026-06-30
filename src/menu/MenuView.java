package menu;

import reader.InputReader;

class MenuView {
    private static volatile MenuView instance;
    private static InputReader inputReader;

    private MenuView(InputReader inputReader) {
        this.inputReader = inputReader;
    }

    static MenuView getInstance() {
        if (instance == null) {
            synchronized (MenuView.class) {
                if (instance == null) {
                    instance = new MenuView(InputReader.getInstance());
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
