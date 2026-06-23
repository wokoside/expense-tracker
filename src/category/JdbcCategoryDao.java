package category;

import db.ConnectionFactory;
import db.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class JdbcCategoryDao implements CategoryRepository {
    private static final JdbcCategoryDao INSTANCE = new JdbcCategoryDao();

    private JdbcCategoryDao() {
    }

    static JdbcCategoryDao getInstance() {
        return INSTANCE;
    }

    @Override
    public void create(String name) {
        String sql = """
                insert into categories (name)
                values (?)
                """;
        try (Connection connection = ConnectionFactory.openConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
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
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    Category mapCategoryRow(ResultSet rs) throws SQLException {
        return new Category(
                rs.getInt("id"),
                rs.getString("name"));

    }
}
