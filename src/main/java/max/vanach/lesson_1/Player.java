package max.vanach.lesson_1;

import java.util.ArrayList;
import java.util.Random;
import max.vanach.lesson_1.utils.PlayerInput;

public class Player {

    protected String nickname;

    protected int number;

    private boolean usedHelper = false;

    public Player(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String value) {
        nickname = value;
    }

    public boolean didUseHelper() {
        if (usedHelper) {
            usedHelper = false;
            return true;
        }

        return false;
    }

    /**
     * The function prompts the user to enter a number within a specified range
     * 
     * @param min       The minimum value of the range from which the user can
     *                  choose a
     *                  number.
     * @param max       The maximum number in the range.
     * @param hideInput Determines whether the guessed number should be hidden or
     *                  not.
     */
    public void setNumber(int min, int max, boolean hideInput) {
        String title = "Choose number from range(" + min + "-" + max + ")";
        String wrongInputMessage = "Wrong number!";

        number = PlayerInput.getInputInt(
                title, 
                ": ", 
                wrongInputMessage, 
                min, 
                max, 
                hideInput);
    }

    /**
     * Guess {@code askedPlayer}'s number
     * 
     * @param askedPlayer  The player who is being asked if guessed number is number
     *                     player think of.
     * @param min          The minimum value of the range from which the user can
     *                     choose a number.
     * @param max          The maximum number in the range.
     * @param hideGuess    Determines whether the guessed number should be hidden or
     *                     not.
     * @param isTournament Is this part of tournament
     * @return Returns guessed number if parameter {@code hideGuess} is true.
     *         Otherwise, it returns the result of comparing the guessed number
     *         with the number chosen by the {@code askedPlayer}.
     */
    public int guessNumber(Player askedPlayer, int min, int max, boolean hideGuess, boolean canUseHelpers, boolean isTournament) {
        String title =  nickname + ", I'm thinking of a certain number in the range of " + min + " to " + max
                + ". What number is it? ";
        String wrongInputMessage = "Number is out of range! (" + min + "-" + max + ") Try again...";

        int number = 0;

        if (canUseHelpers) {
            title += "\nYou have avaible helpers. You can use one of them once or guess number.\n";
            if (isTournament) {
                title += "a. Narrow down guessing range\n";
            } else {
                title += "a. Guess number from 4 numbers\n";
            }

            String nString = PlayerInput.getInputIntOrString(
                    title,
                    ": ",
                    wrongInputMessage,
                    min,
                    max,
                    (isTournament ? PlayerInput.TOURNAMENT_HELP_PATTERN : PlayerInput.NORMAL_HELP_PATTERN),
                    hideGuess);

            if (PlayerInput.isNumeric(nString)) {
                number = Integer.parseInt(nString);
            } else {
                usedHelper = true;

                Random rnd = new Random();
                rnd.setSeed(rnd.nextLong());

                if (isTournament) {
                    int n = askedPlayer.number;

                    int newMin = min + rnd.nextInt(n - min);
                    int newMax = n + rnd.nextInt(max - n);
                    return guessNumber(askedPlayer, newMin, newMax, hideGuess, false, isTournament);
                } else {
                    boolean correctPlaced = false;
                    int correctNumber = askedPlayer.number;
                    ArrayList<Integer> numbers = new ArrayList<>();
                    char c = 'a';
                    
                    title = nickname + ", I'm thinking of a certain number from ones below. What number is it?\n";
                    wrongInputMessage = "It's not correct answear. Try again...";

                    for (int i=0; i<4; i++) {
                        if (i > 0) {
                            title += "\t";
                        }

                        if (!correctPlaced) {
                            boolean place = rnd.nextInt(2) == 0;
                            correctPlaced = place;

                            if (place || 4-i == 1) {
                                numbers.add(correctNumber);
                                title += c++ + ". " + correctNumber;
                            }

                        } else {
                            int n = rnd.nextInt(min, max);
                            while (n == correctNumber) {
                                n = rnd.nextInt(min, max);
                            }
                            
                            numbers.add(n);
                            title += c++ + ". " + n;
                        }
                    }

                    PlayerInput.clearScreen();

                    String strAnswear = PlayerInput.getInputString(
                            title,
                            ": ",
                            wrongInputMessage,
                            PlayerInput.QUIZ_PATTERN,
                            hideGuess);

                    int answear = strAnswear.charAt(0) - 'a';

                    number = numbers.get(answear);
                }
            }
        } else {
            number = PlayerInput.getInputInt(
                    title,
                    ": ",
                    wrongInputMessage,
                    min,
                    max,
                    hideGuess);
        }

        if (hideGuess) {
            return number;
        } else {
            return askedPlayer.compareNumber(number, askedPlayer instanceof Computer);
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
    public int compareNumber(int number, boolean autoResolve) {
        if (autoResolve) {
            return Math.clamp(number - this.number, -1, 1);
        }
        
        String title = "Is " + number + " the number you thought of?";
        String prompt = "Your guess is ( too (H)igh, (E)qual or too (S)mall ): ";
        String wrongInputMessage = "Wrong option!";

        String answear = PlayerInput
                .getInputString(
                        title, 
                        prompt, 
                        wrongInputMessage, 
                        PlayerInput.NUMBER_COMPARE_INPUT_REGEX_PATERN, 
                        false)
                .toLowerCase();

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
