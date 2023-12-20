package max.vanach.lesson_1;

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
     */
    public void setNumber(int min, int max) {
        number = PlayerInput.getInputInt("Choose number from range", ": ", "Wrong number!", min, max, true, true);
    }

    /**
     * Returns the difference between the input number and a stored number
     * 
     * @param number Value that is comapred to the player number.
     * @return {@code -1} if input number is smaller,
     *         {@code 0} when numbers are equal,
     *         {@code 1} when input number is greater.
     */
    public int AskForNumber(int number) {
        return Math.clamp(number - this.number, -1, 1);
    }
}
