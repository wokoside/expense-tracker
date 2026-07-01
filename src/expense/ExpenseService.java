package expense;

import category.CategoryRepository;
import reader.InputException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

class ExpenseService {

    private static volatile ExpenseService instance;
    private final ExpenseRepository expenseDao;

    private final CategoryRepository categoryDao;

    private ExpenseService(ExpenseRepository expenseDao, CategoryRepository categoryRepository) {
        this.expenseDao = expenseDao;
        this.categoryDao = categoryRepository;
    }

    static ExpenseService getInstance(ExpenseRepository expenseDao, CategoryRepository categoryDao) {
        if (instance == null) {
            synchronized (ExpenseService.class) {
                if (instance == null) {
                    instance = new ExpenseService(expenseDao, categoryDao);
                }
            }
        }
        return instance;
    }

    void addExpense(String amount, String description, String categoryId, String expenseDate) {
        BigDecimal validAmount = validateAmount(amount);
        int validCategoryId = validateCategory(categoryId);
        LocalDate validExpenseDate = validateExpenseDate(expenseDate);
        expenseDao.create(validAmount, description, validCategoryId, validExpenseDate);
    }

    List<Expense> showAllExpenses() {
        return expenseDao.findAll();
    }

    List<Expense> showExpensesForPeriod(String start, String end) {
        LocalDate startDate = validateExpenseDate(start);
        LocalDate endDate = validateExpenseDate(end);
        return expenseDao.findBetweenDates(startDate, endDate);
    }

    List<Expense> showExpensesByCategory(String categoryId) {
        int validCategoryId = validateCategory(categoryId);
        return expenseDao.findByCategoryId(validCategoryId);
    }

    void updateExpense(String id, String amount, String description, String categoryId, String expenseDate) {
        int intId = validateId(id);
        BigDecimal validAmount = validateAmount(amount);
        int validCategoryId = validateCategory(categoryId);
        LocalDate validExpenseDate = validateExpenseDate(expenseDate);
        expenseDao.update(intId, validAmount, description, validCategoryId, validExpenseDate);
    }

    void deleteExpense(String id) {
        int intId = validateId(id);
        expenseDao.delete(intId);
    }

    BigDecimal sumExpenses() {
        return expenseDao.sumExpensesAmount();
    }

    Map<String,BigDecimal> sumAmountsByCategory() {
        Map<String, BigDecimal> map = expenseDao.sumAmountsByCategory();
        return map;
    }

    int validateId(String id) {
        if (id == null) throw new InputException("Введен пустой ID");
        String trimId = id.trim();
        if (trimId.isBlank()) throw new InputException("Введен пустой ID");
        try {
            int validId = Integer.valueOf(trimId);
            if (!expenseDao.isExpenseExistsById(validId)) throw new InputException("Указанный расход не существует");
            return validId;
        }
        catch (NumberFormatException e) {
            throw new InputException("Введена строка вместо числового значения");
        }
    }

    BigDecimal validateAmount(String amount) {
        if (amount == null) throw new InputException("Введена пустая стоимость");
        String trimAmount = amount.trim();
        if (trimAmount.isBlank()) throw new InputException("Введена пустая стоимость");
        String regex = "^\\d{1,10}([.,]\\d{1,})?$";
        if (trimAmount.matches(regex)) {
            String normalizedAmount = trimAmount.replace(",", ".");
            return new BigDecimal(normalizedAmount).setScale(2, RoundingMode.HALF_UP);
        } else throw new InputException("Указан неверный формат стоимости");
    }

    int validateCategory(String categoryId) {
        if (categoryId == null) throw new InputException("Введен пустой ID категории");
        String trimCategory = categoryId.trim();
        if (trimCategory.isBlank()) throw new InputException("Введен пустой ID категории");
        try {
            int id = Integer.valueOf(trimCategory);
            if (!categoryDao.isCategoryExistsById(id)) throw new InputException("Указанная категория не существует");
            return id;
        } catch (NumberFormatException e) {
            throw new InputException("Введена строка вместо числового значения");
        }
    }

    LocalDate validateExpenseDate(String expenseDate) {
        if (expenseDate == null) throw new InputException("Введена пустая дата");
        String trimDate = expenseDate.trim();
        if (trimDate.isBlank()) throw new InputException("Введена пустая дата");
        try {
            return LocalDate.parse(trimDate);
        } catch (DateTimeParseException e) {
            throw new InputException("Введенная дата(" + trimDate + ")не соответствует указанному формату");
        }
    }


}
