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
        System.out.println("\n\n========== " + title + " ==========");
        for (int i=0; i<options.size(); i++) {
            MenuOption option = options.get(i);
            System.out.println(i + ". " + option.label);
        }
        System.out.println("==========================");
        System.out.println("\nOption: ");

        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scan.nextLine());
                
                if (choice > 0 && choice <= options.size()) {
                    options.get(choice).process.run();
                    break;
                }   
            } catch(NumberFormatException e) { }
                
            
            System.out.println("Wrong option!");
        }
    }
}
