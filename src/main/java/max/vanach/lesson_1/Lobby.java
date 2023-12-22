package max.vanach.lesson_1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import max.vanach.lesson_1.utils.PlayerInput;

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

    public Lobby(Player playerChoosingNumber, Player playerGuessingNumber, Difficulty difficulty) {
        this.playerChoosingNumber = playerChoosingNumber;
        this.difficulty = difficulty;

        this.guessingPlayers = new ArrayList<>(1);
        this.guessingPlayers.add(playerGuessingNumber);
    }

    public Lobby(Player playerChoosingNumber, List<Player> guessingPlayers, Difficulty difficulty) {
        this.playerChoosingNumber = playerChoosingNumber;
        this.difficulty = difficulty;

        this.guessingPlayers = new ArrayList<>(guessingPlayers);
    }

    public ArrayList<Integer> play() {
        int[] guessingRange = getGuessingRange();
        ArrayList<Integer> tries = new ArrayList<>(Collections.nCopies(guessingPlayers.size(), 0));

        // Singleplayer
        if (guessingPlayers.size() == 1
                && (playerChoosingNumber instanceof Computer || guessingPlayers.get(0) instanceof Computer)) {
            Player guessingPlayer = guessingPlayers.get(0);
            playerChoosingNumber.setNumber(guessingRange[0], guessingRange[1], false);

            int result = -3;
            while (result != 0) {
                tries.set(0,tries.get(0)+1);
                
                result = guessingPlayer.guessNumber(playerChoosingNumber, guessingRange[0], guessingRange[1], false);
                
                if (result == -2) {
                    System.out.println(playerChoosingNumber.nickname + " cheated! Game is invalidated!");
                    PlayerInput.pressEnterToContinue();
                    return null;
                }

                if(!(playerChoosingNumber instanceof Player)) {
                    if (result == 1) {
                        System.out.println("Too high! Try again...");
                        PlayerInput.pressEnterToContinue();
                    } else if (result == -1) {
                        System.out.println("Too low! Try again...");
                        PlayerInput.pressEnterToContinue();
                    }
                }
            }

            System.out.println("Correct! Congratulations!");
            PlayerInput.pressEnterToContinue();
        } else { // Multiplayer

        }

        return tries;
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
