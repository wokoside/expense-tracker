package category;

public interface CategoryRepository {
    Category create(String name);

    boolean isCategoryExistsByName(String name);

    boolean isCategoryExistsById(int id);

    static CategoryRepository getInstance() {
        return JdbcCategoryDao.getInstance();
    }
}
