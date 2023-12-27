package max.vanach.lesson_1.utils;

import java.io.Console;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerInput {
    private final static Console console = System.console();

    public final static Pattern PLAYER_NICKNAME_REGEX_PATERN = Pattern.compile("[a-z0-9_]{3,16}",
            Pattern.CASE_INSENSITIVE);
    public final static Pattern YES_NO_REGEX_PATERN = Pattern.compile("[yn]", Pattern.CASE_INSENSITIVE);
    public final static Pattern NUMBER_COMPARE_INPUT_REGEX_PATERN = Pattern.compile("[hes]", Pattern.CASE_INSENSITIVE);;

    /**
     * Takes user input for an integer within a specified range and displays error
     * messages accordingly.
     * 
     * @param title             Title for the input. It is displayed before the
     *                          input prompt.
     * @param inputPrompt       Message that will be displayed to the user before
     *                          user input.
     * @param wrongInputMessage Message that will be displayed when the user enters
     *                          an invalid input.
     * @param min               The minimum value that the user can input.
     * @param max               The maximum value that the user can input.
     * @param showMin           Value indicating whether to show the minimum value
     *                          in the input prompt.
     * @param showMax           Value indicating whether to show the maximum value
     *                          in the input prompt.
     * 
     * @return Valid user's input.
     */
    public static int getInputInt(String title, String inputPrompt, String wrongInputMessage, int min, int max,
            boolean hideInput) {
        int result = 0;

        clearScreen();

        if (title != null && !title.isEmpty()) {
            System.out.println(title);
        }

        while (true) {
            if (inputPrompt != null && !inputPrompt.isEmpty()) {
                System.out.print(inputPrompt);
            }

            String numberString = (hideInput ? new String(console.readPassword()) : console.readLine());
            if (PlayerInput.isNumeric(numberString)) {
                result = Integer.parseInt(numberString);

                if (min <= result && result <= max) {
                    return result;
                }
            }

            if (wrongInputMessage != null && !wrongInputMessage.isEmpty()) {
                System.out.println(wrongInputMessage);
            } else {
                clearScreen();
            }
        }
    }

    /**
     * Prompts the user for input until the input matches the specified pattern if
     * specified.
     * 
     * @param title             The title is a string that represents the title. It
     *                          is displayed before the input prompt.
     * @param inputPrompt       Message that will be displayed to the user before
     *                          the user input.
     * @param wrongInputMessage Message that will be displayed when the user enters
     *                          an invalid input.
     * @param pattern           A regular expression pattern that the user's input
     *                          must match in order for it to
     *                          be considered valid.
     * @return The method is returning a String value.
     */
    public static String getInputString(String title, String inputPrompt, String wrongInputMessage, Pattern pattern,
            boolean hideInput) {
        String result = "";

        clearScreen();

        if (title != null && !title.isEmpty()) {
            System.out.println(title);
        }

        while (true) {
            if (inputPrompt != null && !inputPrompt.isEmpty()) {
                System.out.print(inputPrompt);
            }

            result = (hideInput ? new String(console.readPassword()) : console.readLine());

            if (result != null && !result.isEmpty()) {
                if (pattern != null && !pattern.pattern().isEmpty()) {
                    Matcher m = pattern.matcher(result);

                    if (m.matches()) {
                        return result;
                    }
                } else {
                    return result;
                }
            }

            if (wrongInputMessage != null && !wrongInputMessage.isEmpty()) {
                System.out.println(wrongInputMessage);
            } else {
                clearScreen();
            }
        }
    }

    /**
     * The function prompts the user to press enter to continue.
     */
    public static void pressEnterToContinue() {
        console.readLine("Press enter to contiune.");
    }

    /**
     * Clears the console screen in Java
     */
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec(new String[] { "clear" });
        } catch (IOException | InterruptedException ex) {
        }
    }

    /**
     * Checks if a given string can be parsed as a numeric value.
     * 
     * @param strNum {@code String} representing number.
     * @return The method isNumeric is returning a boolean value.
     */
    public static boolean isNumeric(String strNum) {
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
