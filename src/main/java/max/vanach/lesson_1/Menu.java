package max.vanach.lesson_1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    public List<MenuOption> options;
    public String title;

    private final Scanner scan = new Scanner(System.in);

    public Menu(String title) {
        options = new ArrayList<>();
        this.title = title;
    }

    public void add_option(MenuOption option) {
        options.add(option);
    }

    /**
     * The function displays a menu with options and prompts the user to choose an option, then
     * executes the corresponding action.
     */
    public void show() {
        // Display menu
        Main.ClearScreen();
        System.out.println("\n\n========== " + title + " ==========");
        for (int i = 0; i < options.size(); i++) {
            MenuOption option = options.get(i);
            System.out.println(i+1 + ". " + option.label);
        }
        System.out.println("==========================");

        // Take user's input
        int choice;
        while (true) {
            System.out.println("\nOption: ");
            String strChoice = scan.nextLine();
            if (Main.IsNumeric(strChoice)) {
                choice = Integer.parseInt(strChoice);

                if (choice > 0 || choice <= options.size()) {
                    options.get(choice - 1).process.run();
                    break;
                }
            }

            System.out.println("Wrong option!");
        }
    }
}
