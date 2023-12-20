package max.vanach.lesson_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
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
    }
}
