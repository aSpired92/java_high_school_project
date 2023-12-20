package max.vanach.lesson_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

public class Main {
    /**
     * Main game sequence. Attempts to guess the number
     * 
     * @param min Minimum number drawn
     * @param max Maximum number drawn
     * @return number of tries
     */
    public static int game_singleplayer(Integer min, Integer max) {
        Scanner scan = new Scanner(System.in);
        int tries = 0;
        int random = new Random().nextInt(max - min) + min;

        System.out.println(random);
        System.out.println(
                "I'm thinking of a certain number in the range of " + min + " to " + max + ". What number is it? ");

        while (true) {

            // Answer validation
            String answer = "";
            while (true) {
                answer = scan.nextLine();
                if (IsNumeric(answer)) {
                    break;
                }
                System.out.println("The answer must be a number between 0 and 100");
            }
            int parsedAnswer = Integer.parseInt(answer);
            tries++;

            if (parsedAnswer > max || parsedAnswer < min) {
                System.out.println("Number is out of range! (" + min + "-" + max + ") Try again...");
            } else if (parsedAnswer > random) {
                System.out.println("Too high! Try again...");
            } else if (parsedAnswer < random) {
                System.out.println("Too low! Try again...");
            } else {
                System.out.println("Correct! Congratulations!");
                return tries;
            }
        }
    }

    /**
     * Copies other players to temp file and appends current player with new result
     * 
     * @param resultsFilePath path of results file
     * @param player          player instance
     * @param newResult       new player result
     * @throws FileNotFoundException if results file not found
     */
    public static void updateResult(Path resultsFilePath, Player player, int newResult) throws FileNotFoundException {

        Path pathname = Paths.get("./temp.txt");
        File tempFile = new File(pathname.toString());
        File resultsFile = new File(resultsFilePath.toString());

        String line;
        try (PrintWriter fileWriter = new PrintWriter(tempFile)) {
            try (Scanner fileReader = new Scanner(resultsFile)) {
                while (fileReader.hasNextLine() && !((line = fileReader.nextLine()).isEmpty())) {
                    String[] arr = line.split(" ", 2);
                    String nickname = arr[0];
                    String result = arr[1];
                    if (!nickname.equals(player.nickname)) {
                        fileWriter.println(nickname + " " + result);
                    }
                }
                fileWriter.println(player.nickname + " " + newResult);
            }

        }

        if (!resultsFile.delete()) {
            System.err.println("Could not delete file");
        }

        if (!tempFile.renameTo(resultsFile)) {
            System.err.println("Could not rename file");
        }

    }

    public static void main(String[] args) throws IOException {

        GameManager gameManager = GameManager.getInstance();
        gameManager.run();

        // Scanner scan = new Scanner(System.in);
        // String inputNickname;
        // while (true) {
        // System.out.print("Nickname: ");
        // inputNickname = scan.nextLine();
        // if (!inputNickname.isEmpty()) {
        // break;
        // }
        // System.out.println("Nickname cannot be empty!");
        // }
        //
        // Player player = new Player(inputNickname);
        //
        //
        // System.out.println("Hello " + player.nickname + "!");
        //
        // while (true) {
        //
        //
        //
        // System.out.println("========== MENU ==========");
        // System.out.println("1. Play Singleplayer");
        // System.out.println("2. Play Singleplayer (Reversed)");
        // System.out.println("3. Play Multiplayer");
        // System.out.println("0. Exit");
        // System.out.println("==========================");
        //
        // String choice;
        // String[] choices_allowed = {"0","1","2","3"};
        // while (true) {
        // choice = scan.nextLine();
        // if (Arrays.asList(choices_allowed).contains(choice)) {
        // break;
        // }
        // System.out.println("Choice is out of range!");
        // }
        //
        //
        // switch (choice)
        // {
        // case "0":
        // {
        // return;
        // }
        //
        // case "1":
        // {
        // if (bestGlobalResult == bestPlayerResult && bestPlayerResult != 0) {
        // System.out.println("You're the best with result: " + bestGlobalResult);
        // } else {
        // if (bestGlobalResult != 0) {
        // System.out.print("Current best global result: " + bestGlobalResult + "by" +
        // bestPlayerNickname);
        // }
        //
        // if (bestPlayerResult != 0) {
        // System.out.print("Your best result: " + bestPlayerResult);
        // }
        // }
        //
        // int tries = game_singleplayer(DEFAULT_MIN, DEFAULT_MAX);
        //
        // if (bestPlayerResult != 0 && bestPlayerResult > tries) {
        // updateResult(pathname, player, tries);
        // } else {
        // try (PrintWriter fileWriter = new PrintWriter(file)) {
        // fileWriter.println(player.nickname + " " + tries);
        // }
        // }
        //
        // System.out.println("Your number of attempts is: " + tries);
        // }
        // break;
        //
        // case "2":
        // {
        // int tries = game_singleplayer(DEFAULT_MIN, DEFAULT_MAX);
        //
        // if (bestPlayerResult != 0 && bestPlayerResult > tries) {
        // updateResult(pathname, player, tries);
        // } else {
        // try (PrintWriter fileWriter = new PrintWriter(file)) {
        // fileWriter.println(player.nickname + " " + tries);
        // }
        // }
        //
        // System.out.println("Your number of attempts is: " + tries);
        // }
        // break;
        //
        // case "3":
        // {
        // if (bestGlobalResult == bestPlayerResult && bestPlayerResult != 0) {
        // System.out.println("You're the best with result: " + bestGlobalResult);
        // } else {
        // if (bestGlobalResult != 0) {
        // System.out.print("Current best global result: " + bestGlobalResult + "by" +
        // bestPlayerNickname);
        // }
        //
        // if (bestPlayerResult != 0) {
        // System.out.print("Your best result: " + bestPlayerResult);
        // }
        // }
        //
        // int tries = game_singleplayer(DEFAULT_MIN, DEFAULT_MAX);
        //
        // if (bestPlayerResult != 0 && bestPlayerResult > tries) {
        // updateResult(pathname, player, tries);
        // } else {
        // try (PrintWriter fileWriter = new PrintWriter(file)) {
        // fileWriter.println(player.nickname + " " + tries);
        // }
        // }
        //
        // System.out.println("Your number of attempts is: " + tries);
        // }
        // break;
        //
        // default:
        // {
        // System.err.println("Wrong MENU value!");
        // }
        // }
        //
        //
        //
        //
        // }

    }

    /**
     * The function checks if a given string can be parsed as a numeric value.
     * 
     * @param strNum The parameter "strNum" is a string that represents a number.
     * @return The method isNumeric is returning a boolean value.
     */
    public static boolean IsNumeric(String strNum) {
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * The function `ClearScreen()` clears the console screen in Java
     */
    public static void ClearScreen() {    
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec(new String[] { "clear" });
        } catch (IOException | InterruptedException ex) {
        }
    }
}
