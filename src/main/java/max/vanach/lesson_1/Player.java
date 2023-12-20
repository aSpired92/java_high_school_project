package max.vanach.lesson_1;

import java.util.Scanner;

public class Player {

    protected String nickname;

    protected int number;

    private Scanner scan = new Scanner(System.in);

    public Player(String nickname) {
        this.nickname = nickname;
    }

    /**
     * The function prompts the user to enter a number within a specified range and keeps asking until
     * a valid number is entered.
     * 
     * @param min The minimum value of the range from which the user can choose a number.
     * @param max The maximum number in the range.
     */
    public void SetNumber(int min, int max) {
        

        while (true) {
            System.out.println("Choose number from range" + min + " - " + max);
            System.out.println("\nNumber: ");

            try {
                String strChoice = scan.nextLine();
                if (strChoice == null || strChoice.isEmpty()) {
                    throw new NumberFormatException();
                }

                int choice = Integer.parseInt(strChoice);

                if (choice >= min && choice <= max) {
                    number = choice;
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Wrong number!");
            }
        }
    }

    
    /**
     * The function returns the difference between the input number and a stored number,
     * clamped between -1 and 1.
     * 
     * @param number The parameter "number" is an integer value that is passed into the method.
     * @return The method is returning: 
     *      -1 if input number is smaller, 
     *      0 when numbers are equal, 
     *      1 when input number is greater.
     */
    public int AskForNumber(int number) {
        return Math.clamp(number - this.number, -1, 1);
    }
}
