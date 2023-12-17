package max.vanach.lesson_1;

public class MenuOption {
    String label;
    int value;
    Thread process;

    public MenuOption(String label, int value, Thread process) {
        this.label = label;
        this.value = value;
        this.process = process;
    }
}
