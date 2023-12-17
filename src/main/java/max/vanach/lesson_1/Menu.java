package max.vanach.lesson_1;

import java.util.ArrayList;
import java.util.Arrays;
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

    public void show() {
        List<Integer> allowed = new ArrayList<>();

        System.out.println("\n\n========== " + title + " ==========");
        for (MenuOption option : options) {
            System.out.println(option.value + ". " + option.label);
            allowed.add(option.value);
        }
        System.out.println("==========================");
        System.out.println("\nOption: ");

        int choice;
        while (true) {
            choice = Integer.parseInt(scan.nextLine());
            if (allowed.contains(choice)) {
                break;
            }
            System.out.println("Wrong option!");
        }

        for (MenuOption option : options) {
            if (option.value == choice) {
                option.process.run();
                break;
            }
        }
    }

    private void get_allowed_options() {


    }
}
