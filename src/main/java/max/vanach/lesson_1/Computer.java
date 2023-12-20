package max.vanach.lesson_1;

import java.util.Random;

public class Computer extends Player {
    public Computer() {
        super("Max");
    }

    @Override
    /**
     * Set pseudorandom number for computer.
     * 
     * @param min The minimum value of the range from which the computer can
     *            'choose' a number.
     * @param max The maximum number in the range.
     */
    public void setNumber(int min, int max) {
        Random rnd = new Random();
        number = rnd.nextInt(min, max + 1);
    }
}
