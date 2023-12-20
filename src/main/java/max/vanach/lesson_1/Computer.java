package max.vanach.lesson_1;

import java.util.Random;

public class Computer extends Player {
    public Computer() {
        super("Max");
    }

    @Override
    public void SetNumber(int min, int max) {
        Random rnd = new Random();
        number = rnd.nextInt(min, max + 1);
    }
}
