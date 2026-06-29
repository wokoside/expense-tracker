package expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

interface ExpenseRepository {

    Expense create(BigDecimal amount, String description, int categoryId, LocalDate expenseDate);

    List<Expense> findAll();

    List<Expense> findBetweenDates(LocalDate start, LocalDate end);

    List<Expense> findByCategoryId(int categoryId);

    void update(int id, BigDecimal amount, String description, int categoryId, LocalDate expenseDate);

    void delete(int id);

    BigDecimal sumExpensesAmount();

    Map<String, BigDecimal> sumAmountsByCategory();

    boolean isExpenseExistsById(int id);
}
