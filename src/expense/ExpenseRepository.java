package expense;

import java.time.LocalDate;
import java.util.List;

interface ExpenseRepository {

    Expense create(long amount, String description, int categoryId, LocalDate expenseDate);

    List<Expense> findAll();

    List<Expense> findBetweenDates(LocalDate start, LocalDate end);

    List<Expense> findByCategoryId(int categoryId);

    void update(int id, long amount, String description, int categoryId, LocalDate expenseDate);

    void delete(int id);

    long sumExpensesAmount();

    StringBuilder sumAmountsByCategory();

    boolean isCategoryExists(int id);
}
