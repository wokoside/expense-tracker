package expense;

import reader.InputException;

import java.util.List;

public class ExpenseController {

    private final ExpenseView expenseView = ExpenseView.getInstance();
    private final ExpenseService expenseService = ExpenseService.getInstance(JdbcExpenseDao.getInstance());

    private static volatile ExpenseController instance;

    private ExpenseController() {
    }

    public static ExpenseController getInstance() {
        if (instance == null) {
            synchronized (ExpenseController.class) {
                if (instance == null) {
                    instance = new ExpenseController();
                }
            }
        }
        return instance;
    }

    public void addExpense() {
        while (true) {
            expenseView.showMessage("Введите стоимость расхода. Ограничение: 10 символов до дробной части, в дробной - 2): ");
            String amount = expenseView.readLine();
            expenseView.showMessage("Введите описание расхода: ");
            String description = expenseView.readLine();
            expenseView.showMessage("Введите ID категории: ");
            String categoryId = expenseView.readLine();
            expenseView.showMessage("Введите корректную дату расхода в формате 'yyyy-mm-dd'");
            String expenseDate = expenseView.readLine();
            try {
                expenseService.addExpense(amount, description, categoryId, expenseDate);
                expenseView.showMessage("Расход успешно добавлен. Введите '1', чтобы создать расход / любой символ, чтобы вернуться в меню");
                String choice = expenseView.readLine();
                if (!choice.equals("1")) return;
            } catch (InputException e) {
                expenseView.showMessage(e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    public void showAllExpenses() {
        List<Expense> expenses = expenseService.showAllExpenses();
        if (expenses.isEmpty()) {
            expenseView.showMessage("Расходы отсутствуют.");
        } else {
            for (Expense expense : expenses) {
                expenseView.showMessage(expense.toString());
            }
        }
    }

    public void showExpensesForPeriod() {
        while (true) {
            expenseView.showMessage("Введите начальную дату: ");
            String start = expenseView.readLine();
            expenseView.showMessage("Введите конечную дату: ");
            String end = expenseView.readLine();
            try {
                List<Expense> expenses = expenseService.showExpensesForPeriod(start, end);
                if (expenses.isEmpty()) {
                    expenseView.showMessage("Отсутствуют расходы за указанный период");
                }
                else {
                    for (Expense expense : expenses) {
                        expenseView.showMessage(expense.toString());
                    }
                }
                return;
            } catch (InputException e) {
                expenseView.showMessage(e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    public void showExpensesByCategory() {
        while (true) {
            expenseView.showMessage("Введите ID категории: ");
            String categoryId = expenseView.readLine();
            try {
                List<Expense> expenses = expenseService.showExpensesByCategory(categoryId);
                if (expenses.isEmpty()) {
                    expenseView.showMessage("Расходы по указанной категории отсутствуют.");
                } else {
                    for (Expense expense : expenses) {
                        expenseView.showMessage(expense.toString());
                    }
                }
                return;
            } catch (InputException e) {
                expenseView.showMessage(e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    public void updateExpense() {
        while (true) {
            expenseView.showMessage("Введите ID расхода");
            String id = expenseView.readLine();
            expenseView.showMessage("Введите стоимость расхода. Ограничение: 10 символов до дробной части, в дробной - 2: ");
            String amount = expenseView.readLine();
            expenseView.showMessage("Введите описание расхода: ");
            String description = expenseView.readLine();
            expenseView.showMessage("Введите ID категории: ");
            String categoryId = expenseView.readLine();
            expenseView.showMessage("Введите корректную дату расхода в формате 'yyyy-mm-dd'");
            String expenseDate = expenseView.readLine();
            try {
                expenseService.updateExpense(id, amount, description, categoryId, expenseDate);
                expenseView.showMessage("Расход успешно обновлен.");
                return;
            } catch (InputException e) {
                expenseView.showMessage(e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    public void deleteExpense() {
        while (true) {
            expenseView.showMessage("Введите ID расхода: ");
            String id = expenseView.readLine();
            try {
                expenseService.deleteExpense(id);
            } catch (InputException e) {
                expenseView.showMessage(e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    public void calculateTotalExpenses() {
        expenseView.showMessage("Общая сумма расходов: " + expenseService.sumExpenses());
    }

    public void showExpenseAmountByCategory() {
        StringBuilder result = expenseService.sumAmountsByCategory();
        if (result.isEmpty()) expenseView.showMessage("Отсутствуют расходы по категориям.");
        else expenseView.showMessage(result.toString());
    }
}
