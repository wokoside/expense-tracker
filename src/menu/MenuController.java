package menu;

import category.CategoryController;
import db.ConnectionFactory;
import expense.ExpenseController;
import migration.MigrationException;
import migration.MigrationRunner;
import reader.InputException;

import java.sql.Connection;
import java.sql.SQLException;

public class MenuController {
    private final CategoryController categoryController = CategoryController.getInstance();
    private final ExpenseController expenseController = ExpenseController.getInstance();

    private final MenuView menuView = MenuView.getInstance();
    private final MenuService menuService = MenuService.getInstance();

    private static volatile MenuController instance;

    private MenuController() {
    }

    public static MenuController getInstance() {
        if (instance == null) {
            synchronized (MenuController.class) {
                if (instance == null) {
                    instance = new MenuController();
                }
            }
        }
        return instance;
    }

    public void execute() {
        try (Connection connection = ConnectionFactory.openConnection()) {
            MigrationRunner.run(connection);
        } catch (SQLException e) {
            menuView.showMessage("Не удалось установить соединение с базой данных: " + e.getMessage());
            return;
        } catch (MigrationException e) {
            menuView.showMessage("Ошибка файлов миграции: " + e.getMessage());
            return;
        }
        while (true) {
            menuView.showMessage("1. Добавить категорию");
            menuView.showMessage("2. Добавить расход");
            menuView.showMessage("3. Показать все расходы");
            menuView.showMessage("4. Найти расходы за период");
            menuView.showMessage("5. Показать расходы по категории");
            menuView.showMessage("6. Изменить расход");
            menuView.showMessage("7. Удалить расход");
            menuView.showMessage("8. Подсчёт общей суммы расходов");
            menuView.showMessage("9. Вывод сумм расходов по категориям");
            menuView.showMessage("0. Выход");
            String input = menuView.readLine();
            try {
                int choice = menuService.validateChoice(input);
                switch (choice) {
                    case 1 -> categoryController.addCategory();
                    case 2 -> expenseController.addExpense();
                    case 3 -> expenseController.showAllExpenses();
                    case 4 -> expenseController.showExpensesForPeriod();
                    case 5 -> expenseController.showExpensesByCategory();
                    case 6 -> expenseController.updateExpense();
                    case 7 -> expenseController.deleteExpense();
                    case 8 -> expenseController.calculateTotalExpenses();
                    case 9 -> expenseController.showExpenseAmountByCategory();
                    case 0 -> {
                        return;
                    }
                }
            } catch (InputException e) {
                menuView.showMessage(e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }


}
