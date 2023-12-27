package max.vanach.lesson_1;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GameManager gameManager = GameManager.getInstance();
        gameManager.run();
    }
}
