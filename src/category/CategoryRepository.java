package category;

interface CategoryRepository {
    Category create(String name);
    boolean existsCategory(String name);
}
