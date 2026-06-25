package category;

import reader.InputException;
import reader.InputReader;

public class CategoryController {
    private final InputReader inputReader = new InputReader(System.in);
    private final CategoryView categoryView = CategoryView.getInstance(inputReader);
    private final CategoryService categoryService = CategoryService.getInstance();

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
                categoryView.showMessage(e.getMessage());
            }
        }
    }

}
