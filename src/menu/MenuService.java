package menu;

import reader.InputException;

class MenuService {
    private static volatile MenuService instance;

    private MenuService() {
    }

    static MenuService getInstance() {
        if (instance == null) {
            synchronized (MenuService.class) {
                if (instance == null) {
                    instance = new MenuService();
                }
            }
        }
        return instance;
    }

    int validateChoice(String input) {
        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new InputException("Введена строка вместо числового значения");
        }
        if (choice < 0 || choice > 9) {
            throw new InputException("Выбранного пункта не существует");
        }
        return choice;
    }

}