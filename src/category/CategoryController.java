package category;

import reader.InputException;

public class CategoryController {
    private final CategoryView categoryView = CategoryView.getInstance();
    private final CategoryService categoryService = CategoryService.getInstance();

    public void addCategory() {
        while (true) {
            String name = categoryView.readLine("Введите название категории (должно быть уникальным, ограничение - 100 символов): ");
            try {
                categoryService.addCategory(name);
                String choice = categoryView.readLine("Категория создана. Введите '1', чтобы создать категорию / любой символ, чтобы вернуться в меню");
                if (!choice.equals("1")) return;
            }
            catch (InputException e) {
                categoryView.showMessage(e.getMessage());
            }
        }
    }

}
