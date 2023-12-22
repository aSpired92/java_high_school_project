package max.vanach.lesson_1;

import java.util.regex.Pattern;

import max.vanach.lesson_1.utils.PlayerInput;

public class Player {

    protected String nickname;

    protected int number;

    public Player(String nickname) {
        this.nickname = nickname;
    }

    /**
     * The function prompts the user to enter a number within a specified range
     * 
     * @param min The minimum value of the range from which the user can choose a
     *            number.
     * @param max The maximum number in the range.
     * @param hideInput Determines whether the guessed number should be hidden or not.
     */
    public void setNumber(int min, int max, boolean hideInput) {
        String title = "Choose number from range(" + min + "-" + max + ")";
        String wrongInputMessage = "Wrong number!";

        number = PlayerInput.getInputInt(title, ": ", wrongInputMessage, min, max, hideInput);
    }

    /**
     * Guess {@code askedPlayer}'s number
     * 
     * @param askedPlayer The player who is being asked if guessed number is number player think of.
     * @param min The minimum value of the range from which the user can choose a number.
     * @param max The maximum number in the range.
     * @param hideGuess Determines whether the guessed number should be hidden or not.
     * @return Returns  guessed number if parameter {@code hideGuess} is true. Otherwise, it returns the result of comparing the guessed number
     * with the number chosen by the {@code askedPlayer}.
     */
    public int guessNumber(Player askedPlayer, int min, int max, boolean hideGuess) {
        String title = "I'm thinking of a certain number in the range of " + min + " to " + max + ". What number is it? ";
        String wrongInputMessage = "Number is out of range! (" + min + "-" + max + ") Try again...";

        int number = PlayerInput.getInputInt(title, ": ", wrongInputMessage, min, max, hideGuess);

        if (hideGuess) {
            return number;
        } else {
            return askedPlayer.compareNumber(number);
        }
    }

    /**
     * Returns the difference between the input number and a number have in mind
     * 
     * @param number Value that is comapred to the player number.
     * @return {@code -1} if input number is smaller,
     *         {@code 0} when numbers are equal,
     *         {@code 1} when input number is greater.
     */
    public int compareNumber(int number) {
        String title = "Is " + number + " the number you thought of?";
        String prompt = "Your guess is ( too (H)igh, (E)qual or too (S)mall ): ";
        String wrongInputMessage = "Wrong option!";
        Pattern p = Pattern.compile("[hes]", Pattern.CASE_INSENSITIVE);

        String answear = PlayerInput.getInputString(title, prompt, wrongInputMessage, p, false).toLowerCase();

        switch (answear) {
            case "h":
                return 1;
            case "s":
                return -1;
            default:
                return 0;
        }
    }
}
