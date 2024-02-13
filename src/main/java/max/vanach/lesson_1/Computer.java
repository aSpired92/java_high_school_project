package max.vanach.lesson_1;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Computer extends Player {

    private int realMin = Integer.MIN_VALUE;
    private int realMax = Integer.MAX_VALUE;

    private List<Integer> numbersThoughtOf;

    private Random rand;

    public Computer() {
        super("Computer");
        
        rand = new Random();
        rand.setSeed(rand.nextLong());
        numbersThoughtOf = new ArrayList<>();
    }

    /**
     * Set pseudorandom number for computer.
     * 
     * @param min The minimum value of the range from which the computer can
     *            'choose' a number.
     * @param max The maximum number in the range.
     */
    @Override
    public void setNumber(int min, int max, boolean hideInput) {
        number = rand.nextInt(min, max + 1);
    }

    /**
     * Guess {@code askedPlayer}'s number
     * 
     * @param askedPlayer The player who is being asked if guessed number is number
     *                    player think of.
     * @param min         The minimum value of the range from which the user can
     *                    choose a number.
     * @param max         The maximum number in the range.
     * @param hideGuess   Unused.
     * @return Returns the result of comparing the guessed number with the number
     *         chosen by the {@code askedPlayer}.
     */
    @Override
    public int guessNumber(Player askedPlayer, int min, int max, boolean hideGuess) {
        realMin = (realMin == Integer.MIN_VALUE ? min : realMin);
        realMax = (realMax == Integer.MAX_VALUE ? max : realMax);

        int number = Integer.MIN_VALUE;
        number = rand.nextInt(realMin, realMax + 1);

        while (numbersThoughtOf.contains(number)) {
            number = rand.nextInt(realMin, realMax + 1);
        }

        numbersThoughtOf.add(number);

        int comparison = askedPlayer.compareNumber(number, false);

        numbersThoughtOf.add(number);

        switch (comparison) {
            case 1:
                realMax = number - 1;
                break;
            case -1:
                realMin = number + 1;
                break;
            default:
                break;
        }

        if (realMin > realMax) {
            return -2; // WHAT DO YOU MEAN MAN
        }

        return comparison;
    }
}
