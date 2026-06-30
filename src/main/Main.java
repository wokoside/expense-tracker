package main;

import menu.MenuController;

public class Main {
    public static void main(String[] args) {
        MenuController menuController = MenuController.getInstance();
        menuController.execute();
    }
}
