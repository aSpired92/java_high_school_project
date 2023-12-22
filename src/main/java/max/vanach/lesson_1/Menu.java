package max.vanach.lesson_1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import max.vanach.lesson_1.utils.PlayerInput;

public class Menu {
    public List<MenuOption> options;
    public String title;

    private static Map<String, Menu> menusMap = new HashMap<String, Menu>();

    public Menu(String title) {
        options = new ArrayList<>();
        this.title = title;
    }

    /**
     * Creates a menu with a given name and title if it doesn't already exist.
     * 
     * @param name Name of the menu.
     * @param title Title of the menu.
     */
    public static void createMenu(String name, String title) {
        if (menusMap.get(title) == null) {
            menusMap.put(name, new Menu(title));
        }
    }

    /**
     * Adds a {@code MenuOption} to a specified menu, if exists.
     * 
     * @param menuName Name of the menu to which the option will be added.
     * @param option Option that will be added to a menu.
     */
    public static void addOptionToMenu(String menuName, MenuOption option) {
        Menu m = menusMap.get(menuName);
        if (m != null) {
            m.addOption(option);
        }
    }

    /**
     * The function returns a Menu object based on the given name.
     * 
     * @param name Name of the menu that you want to retrieve.
     * @return {@code Menu} object.
     */
    public static Menu getMenu(String name) {
        return menusMap.get(name);
    }
    
    public void addOption(MenuOption option) {
        options.add(option);
    }

    /**
     * The function displays a menu with options and prompts the user to choose an
     * option, then executes the corresponding action.
     */
    public void show() {
        String menuString = "========== " + title + " ==========\n";
        for (int i = 0; i < options.size(); i++) {
            MenuOption option = options.get(i);
            menuString += (i + 1) + ". " + option.label + "\n";
        }
        menuString += "==========================\n\n";

        int choice = PlayerInput.getInputInt(menuString, "Option: ", "Wrong option!", 1, options.size(), false);

        options.get(choice - 1).process.run();
    }
}
