package category;

import reader.InputException;
import reader.InputReader;

public class CategoryController {
    private static volatile CategoryController instance;
    private final CategoryView categoryView = CategoryView.getInstance();
    private final CategoryService categoryService = CategoryService.getInstance(JdbcCategoryDao.getInstance());

    private CategoryController() {
    }

    public static CategoryController getInstance() {
        if (instance == null) {
            synchronized (CategoryController.class) {
                if (instance == null) {
                    instance = new CategoryController();
                }
            }
        }
        return instance;
    }

    public void addCategory() {
        while (true) {
            categoryView.showMessage("Введите название категории (должно быть уникальным, ограничение - 100 символов): ");
            String name = categoryView.readLine();
            try {
                categoryService.addCategory(name);
                categoryView.showMessage("Категория создана. Введите '1', чтобы создать категорию / любой символ, чтобы вернуться в меню");
                String choice = categoryView.readLine();
                if (!choice.equals("1")) return;
            } catch (InputException e) {
                categoryView.showMessage(e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

}
