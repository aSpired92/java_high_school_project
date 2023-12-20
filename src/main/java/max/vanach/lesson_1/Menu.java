package max.vanach.lesson_1;

import java.util.ArrayList;
import java.util.List;

import max.vanach.lesson_1.utils.PlayerInput;

public class Menu {
    public List<MenuOption> options;
    public String title;

    public Menu(String title) {
        options = new ArrayList<>();
        this.title = title;
    }

    public void add_option(MenuOption option) {
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

        int choice = PlayerInput.getInputInt(menuString, "Option: ", "Wrong option!", 1, options.size(), false, false);

        options.get(choice - 1).process.run();
    }
}
