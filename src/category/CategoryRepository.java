package category;

interface CategoryRepository {
    void create(String name);
    boolean existsCategory(String name);
}
