package max.vanach.lesson_1;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    // Player who will be choosing the number in the game.
    private Player playerChoosingNumber;
    // List of players who will be guessing the number in the game.
    private List<Player> guessingPlayers;

    // This variable is used to store the difficulty(guessing range) of the game.
    private Difficulty difficulty;

    // Prevent from using default constructor
    private Lobby() {
    }

    public Lobby(Player playerChoosingNumber, List<Player> guessingPlayers, Difficulty difficulty) {
        this.playerChoosingNumber = playerChoosingNumber;
        this.difficulty = difficulty;

        this.guessingPlayers = new ArrayList<>(guessingPlayers);
    }

    public void play() {

    }

    /**
     * Returns an array representing the range of numbers to guess from
     * based on the difficulty level.
     * 
     * @return An array of two integers representing the guessing range. The first
     *         element of the array is the lower bound of the range, and the second
     *         element is the upper bound of the range.
     */
    private int[] getGuessingRange() {
        switch (difficulty) {
            case EASY:
                return new int[] { 0, 100 };
            case MEDIUM:
                return new int[] { 0, 1000 };
            case HARD:
                return new int[] { 0, 10000 };
        }

        return new int[] { 0, 100 };
    }
}
