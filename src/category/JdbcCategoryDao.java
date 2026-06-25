package category;

import db.ConnectionFactory;
import db.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class JdbcCategoryDao implements CategoryRepository {
    private static volatile JdbcCategoryDao instance;

    private JdbcCategoryDao() {
    }

    static JdbcCategoryDao getInstance() {
        if (instance == null) {
            synchronized (JdbcCategoryDao.class) {
                if (instance == null) {
                    instance = new JdbcCategoryDao();
                }
            }
        }
        return instance;
    }

    @Override
    public Category create(String name) {
        String sql = """
                insert into categories (name)
                values (?)
                returning id
                """;
        try (Connection connection = ConnectionFactory.openConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Category(rs.getInt("id"), name);
                }
                throw new DatabaseException("Не удалось создать категорию");
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean existsCategory(String name) {
        String sql = """
                select 1 from categories
                where name = ?
                """;
        try (Connection connection = ConnectionFactory.openConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

}
