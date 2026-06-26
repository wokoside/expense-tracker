package expense;

import java.math.BigDecimal;
import java.time.LocalDate;

class Expense {
    private int id;
    private BigDecimal amount;
    private String description;
    private int categoryId;
    private LocalDate expenseDate;

    public int getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public Expense(int id, BigDecimal amount, String description, int categoryId, LocalDate expenseDate) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.categoryId = categoryId;
        this.expenseDate = expenseDate;
    }

    public Expense(BigDecimal amount, String description, int categoryId, LocalDate expenseDate) {
        this.amount = amount;
        this.description = description;
        this.categoryId = categoryId;
        this.expenseDate = expenseDate;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", categoryId=" + categoryId +
                ", expenseDate=" + expenseDate +
                '}';
    }
}
