package expense;

import reader.InputReader;

public class ExpenseView {

    private static volatile ExpenseView instance;
    private final InputReader inputReader;

    private ExpenseView(InputReader inputReader) {
        this.inputReader = inputReader;
    }

    static ExpenseView getInstance() {
        if (instance == null) {
            synchronized (ExpenseView.class) {
                if (instance == null) {
                    instance = new ExpenseView(InputReader.getInstance());
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
