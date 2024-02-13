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

    private int minRange;
    private int maxRange;

    private List<Integer> helpersAvaible;

    // Prevent from using default constructor
    private Lobby() { }

    public Lobby(Player playerChoosingNumber, Player playerGuessingNumber, int minRange, int maxRange) {
        this.playerChoosingNumber = playerChoosingNumber;
        
        this.minRange = minRange;
        this.maxRange = maxRange;

        this.guessingPlayers = new ArrayList<>(1);
        this.guessingPlayers.add(playerGuessingNumber);

        this.helpersAvaible = new ArrayList<>(1);
        this.helpersAvaible.add(0);
    }

    public Lobby(Player playerChoosingNumber, Player playerGuessingNumber, int minRange, int maxRange, int helpersAvaible) {
        this.playerChoosingNumber = playerChoosingNumber;
        
        this.minRange = minRange;
        this.maxRange = maxRange;

        this.guessingPlayers = new ArrayList<>(1);
        this.guessingPlayers.add(playerGuessingNumber);

        this.helpersAvaible = new ArrayList<>(1);
        this.helpersAvaible.add(helpersAvaible);
    }

    public Lobby(Player playerChoosingNumber, List<Player> guessingPlayers, int minRange, int maxRange, List<Integer> helpersAvaible) {
        this.playerChoosingNumber = playerChoosingNumber;

        this.minRange = minRange;
        this.maxRange = maxRange;

        this.guessingPlayers = new ArrayList<>(guessingPlayers);

        this.helpersAvaible = new ArrayList<>(helpersAvaible);
    }

    /**
     * The function "play" allows players to play a guessing game, either in singleplayer or
     * multiplayer mode, and returns the number of tries each player took to guess the correct number.
     * 
     * @param gameType Type of game being played.
     * @return The method is returning an ArrayList of integers, which represents the number of tries
     * made by each player in the game.
     */
    public ArrayList<Integer> play(GameType gameType) {
        ArrayList<Integer> tries = new ArrayList<>(Collections.nCopies(guessingPlayers.size(), 0));

        boolean isTournament = gameType == GameType.TOURNAMENT;

        // Singleplayer
        if (gameType == GameType.SINGLEPLAYER || isTournament) {
            playerChoosingNumber.setNumber(minRange, maxRange, isTournament);
            Player guessingPlayer = guessingPlayers.get(0);

            int result = -3;
            do {
                tries.set(0, tries.get(0) + 1);

                boolean isHelperAvaible = helpersAvaible.get(0) > 0;

                result = guessingPlayer.guessNumber(playerChoosingNumber, minRange, maxRange, isTournament, isHelperAvaible, isTournament);
                if (guessingPlayer.didUseHelper()) {
                    helpersAvaible.set(0, 0);
                }

                if (result == -2) {
                    System.out.println(playerChoosingNumber.getNickname() + " cheated! Game is invalidated!");
                    PlayerInput.pressEnterToContinue();
                    return null;
                }

                if (isTournament) {
                    result = playerChoosingNumber.compareNumber(result, true);
                }

                if (guessingPlayer instanceof Player) {
                    if (result == 1) {
                        System.out.println("Too high! Try again...\n");
                        PlayerInput.pressEnterToContinue();
                    } else if (result == -1) {
                        System.out.println("Too low! Try again...\n");
                        PlayerInput.pressEnterToContinue();
                    }
                }
            } while (result != 0);

            System.out.println("Correct! Congratulations!\n");
            PlayerInput.pressEnterToContinue();

        } else  if (gameType == GameType.MULTIPLAYER) { 
            int round = 1;
            ArrayList<Integer> numbers = new ArrayList<>(Collections.nCopies(guessingPlayers.size(), 0));
            ArrayList<Boolean> doesPlayerGuessedList = new ArrayList<>(Collections.nCopies(guessingPlayers.size(), false));
            
            playerChoosingNumber.setNumber(minRange, maxRange, true);
            
            while (!doesPlayerGuessedList.stream().allMatch(p -> p==true)) {
                String message = "Results of " + round++ + " round:\n";

                for (int i=0; i<guessingPlayers.size(); i++) {
                    if (doesPlayerGuessedList.get(i)) {
                        continue;
                    }

                    boolean isHelperAvaible = helpersAvaible.get(i) > 0 && Ranking.isPlayerMaster(guessingPlayers.get(i).getNickname());
                    if (guessingPlayers.get(0).didUseHelper()) {
                        helpersAvaible.set(i, 0);
                    }

                    numbers.set(
                            i, 
                            guessingPlayers.get(i).guessNumber(
                                    null, 
                                    minRange, 
                                    maxRange, 
                                    true, 
                                    isHelperAvaible, 
                                    false));

                    tries.set(i, tries.get(i) + 1);
                }
                
                for (int i = 0; i < numbers.size(); i++) {
                    if (doesPlayerGuessedList.get(i)) {
                        continue;
                    }
                    
                    int result = playerChoosingNumber.compareNumber(numbers.get(i), true);
                    message += guessingPlayers.get(i).getNickname() + ": ";
                    if (result == 1) {
                        message += "It's too high!\n"; 
                    } else if (result == -1) {
                        message += "It's too low!\n"; 
                    } else {
                        message += "Correct! Congratulations!\n";
                        doesPlayerGuessedList.set(i, true);
                    }
                }

                PlayerInput.clearScreen();
                System.out.println(message);
                PlayerInput.pressEnterToContinue();
            }
        }

        return tries;
    }
}
