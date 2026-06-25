package expense;

import db.ConnectionFactory;
import db.DatabaseException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class JdbcExpenseDao implements ExpenseRepository {
    private static volatile JdbcExpenseDao instance;

    private JdbcExpenseDao() {
    }

    static JdbcExpenseDao getInstance() {
        if (instance == null) {
            synchronized (JdbcExpenseDao.class) {
                if (instance == null) {
                    instance = new JdbcExpenseDao();
                }
            }
        }
        return instance;
    }

    @Override
    public Expense create(long amount, String description, int categoryId, LocalDate expenseDate) {
        String sql = """
                insert into expenses(amount, description, category_id, expense_date)
                values (?, ?, ?, ?)
                returning id, amount, description, category_id, expense_date
                """;
        try (Connection connection = ConnectionFactory.openConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, amount);
            ps.setString(2, description);
            ps.setInt(3, categoryId);
            ps.setDate(4, Date.valueOf(expenseDate));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapExpenseRow(rs);
            }
            throw new DatabaseException("Не удалось создать расход");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<Expense> findAll() {
        String sql = """
                select id, amount, description, category_id, expense_date from expenses
                """;
        try (Connection connection = ConnectionFactory.openConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<Expense> expenses = new ArrayList<>();
            while (rs.next()) {
                expenses.add(mapExpenseRow(rs));
            }
            return expenses;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<Expense> findBetweenDates(LocalDate start, LocalDate end) {
        String sql = """
                select id, amount, description, category_id, expense_date from expenses
                where expense_date between ? and ?
                """;
        try (Connection connection = ConnectionFactory.openConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            ResultSet rs = ps.executeQuery();
            List<Expense> expenses = new ArrayList<>();
            while (rs.next()) {
                expenses.add(mapExpenseRow(rs));
            }
            return expenses;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<Expense> findByCategoryId(int categoryId) {
        String sql = """
                select id, amount, description, category_id, expense_date from expenses
                where category_id = ?
                """;
        try (Connection connection = ConnectionFactory.openConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            List<Expense> expenses = new ArrayList<>();
            while (rs.next()) {
                expenses.add(mapExpenseRow(rs));
            }
            return expenses;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void update(int id, long amount, String description, int categoryId, LocalDate expenseDate) {
        String sql = """
                update expenses
                set amount = ?, description = ?, categoryId = ?, expenseDate = ?
                where id = ?
                """;
        try (Connection connection = ConnectionFactory.openConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, amount);
            ps.setString(2, description);
            ps.setInt(3, categoryId);
            ps.setDate(4, Date.valueOf(expenseDate));
            ps.setInt(5, id);
            int count = ps.executeUpdate();
            if (count == 0) throw new DatabaseException("Не удалось обновить запись");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = """
                delete from expenses
                where id = ?
                """;
        try (Connection connection = ConnectionFactory.openConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int count = ps.executeUpdate();
            if (count == 0) throw new DatabaseException("Не удалось удалить запись");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean isCategoryExists(int id) {
        String sql = """
                select 1 from categories
                where id = ?
                """;
        try (Connection connection = ConnectionFactory.openConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public long sumExpensesAmount() {
        String sql = """
                select sum(amount) as sum from expenses
                """;
        try (Connection connection = ConnectionFactory.openConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) rs.getLong("sum");
            }
            throw new DatabaseException("Не удалось подсчитать сумму расходов");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public StringBuilder sumAmountsByCategory() {
        String sql = """
                select categories.name as category, sum(amount) as sum from expenses, categories
                where expenses.category_id = categories.id
                group by categories.name
                """;
        try (Connection connection = ConnectionFactory.openConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                String category = rs.getString("category");
                long sum = rs.getLong("sum");
                sb.append(category + ": " + sum);
            }
            return sb;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private Expense mapExpenseRow(ResultSet rs) throws SQLException {
        return new Expense(
                rs.getInt("id"),
                rs.getLong("amount"),
                rs.getString("description"),
                rs.getInt("category_id"),
                rs.getDate("expense_date").toLocalDate()
        );
    }


}
