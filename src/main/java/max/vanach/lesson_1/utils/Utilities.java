package max.vanach.lesson_1.utils;

public class Utilities {
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
