package category;

import reader.InputException;

class CategoryService {
    private final CategoryRepository categoryDao;
    private static volatile CategoryService instance;

    private CategoryService(CategoryRepository categoryDao) {
        this.categoryDao = categoryDao;
    }

    static CategoryService getInstance() {
        if (instance == null) {
            synchronized (CategoryService.class) {
                if (instance == null) {
                    instance = new CategoryService(JdbcCategoryDao.getInstance());
                }
            }
        }
        return instance;
    }

    void addCategory(String name) {
        name = validateName(name);
        categoryDao.create(name);
    }

    String validateName(String name) {
        String trimName = name.trim();
        if (name.isBlank() || name == null) throw new InputException("Введена пустая строка");
        if (trimName.length() > 100) throw new InputException("Превышен лимит символов (>100)");
        if (categoryDao.existsCategory(trimName)) throw new InputException("Введено неуникальное имя категории");
        return trimName;
    }


}
