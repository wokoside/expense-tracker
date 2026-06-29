package category;

import reader.InputException;

class CategoryService {
    private final CategoryRepository categoryDao;
    private static volatile CategoryService instance;

    private CategoryService(CategoryRepository categoryDao) {
        this.categoryDao = categoryDao;
    }

    static CategoryService getInstance(CategoryRepository categoryDao) {
        if (instance == null) {
            synchronized (CategoryService.class) {
                if (instance == null) {
                    instance = new CategoryService(categoryDao);
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
        if (name == null) throw new InputException("Введена пустая строка");
        String trimName = name.trim();
        if (trimName.isBlank()) throw new InputException("Введена пустая строка");
        if (trimName.length() > 100) throw new InputException("Превышен лимит символов (>100)");
        if (categoryDao.isCategoryExistsByName(trimName))
            throw new InputException("Введено неуникальное имя категории");
        return trimName;
    }

    boolean isCategoryExistsById(int id) {
        return categoryDao.isCategoryExistsById(id);
    }
}
