package max.vanach.lesson_1;

public class MenuOption {
    String label;
    Thread process;

    public MenuOption(String label, Thread process) {
        this.label = label;
        this.process = process;
    }
}
