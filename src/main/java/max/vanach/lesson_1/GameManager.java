package max.vanach.lesson_1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import max.vanach.lesson_1.utils.PlayerInput;

enum GameType {

    SINGLEPLAYER("SINGLEPLAYER"),
    MULTIPLAYER("MULTIPLAYER");

    private final String name;

    GameType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

enum GameMode {
    // Singleplayer modes
    PLAYER_VS_PC("PLAYER VS PC"),
    PC_VS_PLAYER("PC VS PLAYER"),
    PLAYER_VS_PC_MIXED("PLAYER VS PC MIXED"),
    // Multiplayer modes
    PLAYER_VS_PLAYER("PLAYER VS PLAYER"),
    PLAYERS_VS_PC("PLAYERS VS PC");

    private final String name;

    GameMode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

enum Difficulty {
    EASY,
    MEDIUM,
    HARD,
    CUSTOM;
}

public final class GameManager {
    private static GameManager instance = null;

    public static GameManager getInstance() {
        if (instance == null) {
            synchronized (GameManager.class) {
                if (instance == null) {
                    instance = new GameManager();
                }
            }
        }

        return instance;
    }

    private final int MAX_PLAYERS = 8;

    private static final String MAIN_MENU = "main";
    private static final String SINGLEPLAYER_MENU = "gameSingleplayerMenu";
    private static final String SP_GAME_MODES_MENU = "gameSPGameModesMenu";
    private static final String MULTIPLAYER_MENU = "gameMultiplayerMenu";
    private static final String MP_GAME_MODES_MENU = "gameMPGameModesMenu";
    private static final String SETTINGS_MENU = "settingsMenu";
    private static final String DIFICULTY_MENU = "dificultyMenu";

    private List<Player> players = new LinkedList<Player>();

    private GameType gameType;
    private GameMode gameMode;
    private Difficulty difficulty;

    private Ranking rankingInstance = Ranking.getInstance();

    private GameManager() {
        if (instance != null) {
            throw new RuntimeException("Not allowed. Please use getInstance() method");
        }

        initializeMenus();
    }

    /**
     * Creates a new player(s) and then enters the game loop.
     */
    public void run() {
        createNewPlayer();

        String title = "Create more players for multiplayer game puposes? (Y/N)";
        String prompt = ": ";
        String wrongInputMessage = "(Y)es or (N)o";

        String answear = PlayerInput
                .getInputString(title, prompt, wrongInputMessage, PlayerInput.YES_NO_REGEX_PATERN, false)
                .toLowerCase();

        if (answear.equals("y")) {
            createMultiplayerPlayers();
        }

        while (true) {
            Menu.getMenu(MAIN_MENU).show();
        }
    }

    /**
     * The function creates a new player by prompting for a nickname and adding it
     * to the list of
     * players.
     */
    private void createNewPlayer() {
        String title = "";
        String prompt = "Nickname: ";
        String wrongMessagePrompt = "Nickname must be 3-16 characters long and can have only alphanumeric and \"_\" characters!";

        if (players.size() > 0) {
            title = "Set nickname for Player" + (players.size() + 1) + ".";
            prompt = ": ";
        }

        String inputNickname = "";

        do {
            String tempInputNickname = PlayerInput.getInputString(title, prompt, wrongMessagePrompt,
                PlayerInput.PLAYER_NICKNAME_REGEX_PATERN, false);

            if (players.stream().filter(p -> p.getNickname().equals(tempInputNickname)).findFirst().isPresent()) {
                System.out.println("This nickname is already taken!");
                PlayerInput.pressEnterToContinue();
                continue;
            }

            inputNickname = tempInputNickname;
            break;
        } while (true);
        

        players.add(new Player(inputNickname));
    }

    /**
     * The function creates a specified number of multiplayer players by calling the
     * {@code createNewPlayer()}
     * function.
     */
    private void createMultiplayerPlayers() {
        String title = "How many players would you like to add? (Maximum " + (MAX_PLAYERS - players.size()) + ")";
        String inputPrompt = ": ";
        String wrongInputMessage = "Wrong number!";

        int numberOfPlayers = PlayerInput
                .getInputInt(title, inputPrompt, wrongInputMessage, 1, (MAX_PLAYERS - players.size()), false);

        for (int i = 0; i < numberOfPlayers; i++) {
            createNewPlayer();
        }
    }

    /**
     * The function initializes menus and their options for a game menu system.
     */
    private void initializeMenus() {
        Menu.createMenu(MAIN_MENU, "MAIN MENU");
        Menu.addOptionToMenu(MAIN_MENU, new MenuOption("Singleplayer", goToSingleplayerMenu));
        Menu.addOptionToMenu(MAIN_MENU, new MenuOption("Multiplayer", goToMultiplayerMenu));
        Menu.addOptionToMenu(MAIN_MENU, new MenuOption("Settings", goToSettings));
        Menu.addOptionToMenu(MAIN_MENU, new MenuOption("Exit", exit));

        Menu.createMenu(SINGLEPLAYER_MENU, "SINGLEPLAYER");
        Menu.addOptionToMenu(SINGLEPLAYER_MENU, new MenuOption("Play", goToSPGameModeSelection));
        Menu.addOptionToMenu(SINGLEPLAYER_MENU, new MenuOption("Ranking", goToSingleplayerRanking));
        Menu.addOptionToMenu(SINGLEPLAYER_MENU, new MenuOption("Back", goToMainMenu));

        Menu.createMenu(SP_GAME_MODES_MENU, "GAME MODE");
        Menu.addOptionToMenu(SP_GAME_MODES_MENU, new MenuOption("Player versus computer", selectPVCGamemode));
        Menu.addOptionToMenu(SP_GAME_MODES_MENU, new MenuOption("Computer versus player", selectCVPGamemode));
        Menu.addOptionToMenu(SP_GAME_MODES_MENU, new MenuOption("Player versus computer mixed", selectPVCMGamemode));
        Menu.addOptionToMenu(SP_GAME_MODES_MENU, new MenuOption("Back", goToSingleplayerMenu));

        Menu.createMenu(MULTIPLAYER_MENU, "MULTIPLAYER");
        Menu.addOptionToMenu(MULTIPLAYER_MENU, new MenuOption("Play", goToMPGameModeSelection));
        Menu.addOptionToMenu(MULTIPLAYER_MENU, new MenuOption("Ranking", goToMultiplayerRanking));
        Menu.addOptionToMenu(MULTIPLAYER_MENU, new MenuOption("Back", goToMainMenu));

        Menu.createMenu(MP_GAME_MODES_MENU, "GAME MODE");
        Menu.addOptionToMenu(MP_GAME_MODES_MENU, new MenuOption("Player versus Player", selectPVPGamemode));
        Menu.addOptionToMenu(MP_GAME_MODES_MENU, new MenuOption("Players versus Computer", selectPsVCGamemode));
        Menu.addOptionToMenu(MP_GAME_MODES_MENU, new MenuOption("Back", goToMultiplayerMenu));

        Menu.createMenu(SETTINGS_MENU, "SETTINGS");
        Menu.addOptionToMenu(SETTINGS_MENU, new MenuOption("Change number of players", changeNumberOfPlayers));
        Menu.addOptionToMenu(SETTINGS_MENU, new MenuOption("Change name of player", changePlayerName));
        Menu.addOptionToMenu(SETTINGS_MENU, new MenuOption("Back", goToGameMenu));

        Menu.createMenu(DIFICULTY_MENU, "DIFFICULTY");
        Menu.addOptionToMenu(DIFICULTY_MENU, new MenuOption("Easy", playEasy));
        Menu.addOptionToMenu(DIFICULTY_MENU, new MenuOption("Medium", playMedium));
        Menu.addOptionToMenu(DIFICULTY_MENU, new MenuOption("Hard", playHard));
        Menu.addOptionToMenu(DIFICULTY_MENU, new MenuOption("Custom", playCustom));
        Menu.addOptionToMenu(DIFICULTY_MENU, new MenuOption("Back", goToGameMenu));
    }

    /**
     * Handles the logic for playing the game, including singleplayer and
     * multiplayer modes, choosing players, updating rankings, and saving the ranking to a file.
     */
    private void play() {
        ArrayList<Integer> tries = new ArrayList<>();

        int[] range = getGuessingRange();

        if (gameType == GameType.SINGLEPLAYER) {
            Computer computer = new Computer();
            Lobby lobby;

            if (gameMode == GameMode.PLAYER_VS_PC || gameMode == GameMode.PC_VS_PLAYER) {
                Player numberChoosingPlayer = (gameMode == GameMode.PLAYER_VS_PC ? computer : players.get(0));
                Player numberGuessingPlayer = (gameMode == GameMode.PLAYER_VS_PC ? players.get(0) : computer);

                lobby = new Lobby(numberChoosingPlayer, numberGuessingPlayer, range[0], range[1]);

                tries = lobby.play(gameType);

                rankingInstance.updateEntry(Ranking.SINGLEPLAYER_RANKING_NAME,
                        new RankingEntry(numberGuessingPlayer.getNickname(), tries.get(0), true));
            } else {
                Random r = new Random();

                // 0 - Player, 1 - Computer
                int whoFirstGuessing = r.nextInt(2);

                Player player1 = players.get(0);
                Player player2 = computer;

                lobby = new Lobby((whoFirstGuessing == 0 ? player2 : player1),
                        (whoFirstGuessing == 0 ? player1 : player2), range[0], range[1]);

                tries = lobby.play(gameType);

                PlayerInput.clearScreen();
                System.out.println("Prepare for next round.");
                PlayerInput.pressEnterToContinue();

                lobby = new Lobby((whoFirstGuessing == 0 ? player1 : player2),
                        (whoFirstGuessing == 0 ? player2 : player1), range[0], range[1]);

                tries.addAll(lobby.play(gameType));

                PlayerInput.clearScreen();

                int p1Tries = tries.get((whoFirstGuessing == 0 ? 0 : 1));
                int p2Tries = tries.get((whoFirstGuessing == 0 ? 1 : 0));

                rankingInstance.updateEntry(Ranking.SINGLEPLAYER_RANKING_NAME,
                        new RankingEntry(player1.getNickname(), p1Tries, p2Tries > p1Tries));

                rankingInstance.updateEntry(Ranking.SINGLEPLAYER_RANKING_NAME,
                        new RankingEntry(player2.getNickname(), p2Tries, p1Tries > p2Tries));

                if (p1Tries != p2Tries) {
                    String winner = (p2Tries > p1Tries ? player1.getNickname() : player2.getNickname());
                    System.out.println(winner + " wins.");
                } else {
                    System.out.println("It's a draw.");
                }

                PlayerInput.pressEnterToContinue();
            }
        } else if (gameType == GameType.MULTIPLAYER) {
            tries = new ArrayList<>(Collections.nCopies(players.size() - 1, 0));

            switch (gameMode) {
                case PLAYER_VS_PLAYER: {
                    String title = "Who wants to choose a number?\n";
                    for (int i = 0; i < players.size(); i++) {
                        title += (i + 1) + ". " + players.get(i).getNickname() + "\n";
                    }
                    String inputPrompt = ": ";
                    String wrongInputMessage = "Wrong number!";

                    int n = PlayerInput
                            .getInputInt(title, inputPrompt, wrongInputMessage, 1, players.size(),
                                    false) - 1;

                    Player playerChoosingNumber = players.get(n);

                    ArrayList<Player> guessingPlayers = new ArrayList<>(
                            players.stream().filter(p -> !p.getNickname().equals(playerChoosingNumber.getNickname()))
                                    .collect(Collectors.toList()));

                    Lobby lobby = new Lobby(playerChoosingNumber, guessingPlayers, range[0], range[1]);

                    tries = lobby.play(gameType);
                    int best = tries.stream().min(Integer::compare).get();

                    String message = "Game results :\n";

                    for (int i = 0; i < tries.size(); i++) {
                        String nickname = guessingPlayers.get(i).getNickname();
                        int t = tries.get(i);

                        message += nickname + ": " + t + (t != 1 ? " tries\n" : " try\n");
                        rankingInstance.updateEntry(Ranking.MULTIPLAYER_RANKING_NAME,
                                new RankingEntry(nickname, t, t == best));
                    }

                    PlayerInput.clearScreen();
                    System.out.println(message);
                    PlayerInput.pressEnterToContinue();

                    break;
                }
                case PLAYERS_VS_PC: {
                    Computer computer = new Computer();

                    Lobby lobby = new Lobby(computer, players, range[0], range[1]);

                    tries = lobby.play(gameType);
                    int best = tries.stream().min(Integer::compare).get();

                    String message = "Game results :\n";

                    for (int i = 0; i < tries.size(); i++) {
                        String nickname = players.get(i).getNickname();
                        int t = tries.get(i);

                        message += nickname + ": " + t + (t != 1 ? " tries\n" : " try\n");
                        rankingInstance.updateEntry(Ranking.MULTIPLAYER_RANKING_NAME,
                                new RankingEntry(nickname, t, t == best));
                    }

                    PlayerInput.clearScreen();
                    System.out.println(message);
                    PlayerInput.pressEnterToContinue();
                    break;
                }
                default:
                    break;
            }
        }

        // Save tries to ranking
        if (tries.size() > 0) {
            try {
                rankingInstance.saveRankingToFile();
            } catch (Exception e) {
                System.err.println("Can't save ranking to file");
                PlayerInput.pressEnterToContinue();
            }
        }
    }

    private void showRanking(String rankingName) {
        rankingInstance.displayRanking(rankingName);
    }

    private void changeNameOfPlayer() {
        String title = "Which nickname should be changed?\n";
        for (int i = 0; i < players.size(); i++) {
            title += (i + 1) + ". " + players.get(i).getNickname() + "\n";
        }

        String inputPrompt = ": ";
        String wrongInputMessage = "Wrong number!";

        int n = PlayerInput.getInputInt(title, inputPrompt, wrongInputMessage, 1, players.size(), false) - 1;

        do {
            title = "";
            inputPrompt = "New nickname: ";
            wrongInputMessage = "Nickname must be 3-16 characters long and can have only alphanumeric and \"_\" characters!";

            String inputNickname = PlayerInput.getInputString(title, inputPrompt, wrongInputMessage,
                    PlayerInput.PLAYER_NICKNAME_REGEX_PATERN, false);
            
            if (players.stream().filter(p -> (p.getNickname().equals(inputNickname))).findFirst().isPresent()) {
                System.out.println("This nickname is already taken!");
                PlayerInput.pressEnterToContinue();
                continue;
            }


            players.get(n).setNickname(inputNickname);
            break;
        } while (true);
    }

    /**
     * The function allows the user to change the number of players in a game by either kicking a
     * player or adding a new player.
     */
    private void changeNumberOfPlayers() {
        do {
            String title = "1. Kick player.\n2. Add player.\n3. Back";
            String inputPrompt = ": ";
            String wrongInputMessage = "Wrong number!";

            int n = PlayerInput.getInputInt(title, inputPrompt, wrongInputMessage, 1, 2, false);

            if (n == 3) {
                return;
            }

            PlayerInput.clearScreen();

            if (n == 1) {
                if (players.size() == 1) {
                    System.out.println("You cannot kick if there is only one player.");
                    PlayerInput.pressEnterToContinue();
                    continue;
                }

                title = "Which player should be kicked?\nNOTE: If you kick player1, player2 will be new host for singleplayer games.";

                for (int i = 0; i < players.size(); i++) {
                    title += "\n" + (i + 1) + ". " + players.get(i).getNickname();
                }

                inputPrompt = ": ";
                wrongInputMessage = "Wrong number!";

                int playerId = PlayerInput.getInputInt(title, inputPrompt, wrongInputMessage, 1, players.size(), false) - 1;

                players.remove(playerId);
            } else if (n == 2) {
                if (players.size() == MAX_PLAYERS) {
                    System.out.println("List of players is full.");
                    PlayerInput.pressEnterToContinue();
                }
                
                createNewPlayer();
            }
            
            break;
        } while (true);
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
            default: {
                int[] range = { 0, 100 };

                String title = "What is your desired minimal range to guess? (Minimum 0, Maximum " + (Integer.MAX_VALUE-1) + ")";
                String inputPrompt = ": ";
                String wrongInputMessage = "Wrong number!";

                int minRange = PlayerInput
                        .getInputInt(title, inputPrompt, wrongInputMessage, 0, (Integer.MAX_VALUE-1), false);

                title = "What is your desired maximal range to guess? (Minimum " + (minRange + 1) + ", Maximum " + Integer.MAX_VALUE + ")";
                inputPrompt = ": ";
                wrongInputMessage = "Wrong number!";

                int maxRange = PlayerInput
                        .getInputInt(title, inputPrompt, wrongInputMessage, (minRange + 1), Integer.MAX_VALUE, false);

                range = new int[] {minRange, maxRange};

                return range;
            }
        }
    }

    /*
     * Threads for menus
     */

    private final Thread goToSingleplayerMenu = new Thread(() -> {
        gameType = GameType.SINGLEPLAYER;
        Menu.getMenu(SINGLEPLAYER_MENU).show();
    });

    private final Thread goToSPGameModeSelection = new Thread(() -> {
        Menu.getMenu(SP_GAME_MODES_MENU).show();
    });

    private final Thread selectPVCGamemode = new Thread(() -> {
        gameMode = GameMode.PLAYER_VS_PC;
        Menu.getMenu(DIFICULTY_MENU).show();

    });

    private final Thread selectCVPGamemode = new Thread(() -> {
        gameMode = GameMode.PC_VS_PLAYER;
        Menu.getMenu(DIFICULTY_MENU).show();
    });

    private final Thread selectPVCMGamemode = new Thread(() -> {
        gameMode = GameMode.PLAYER_VS_PC_MIXED;
        Menu.getMenu(DIFICULTY_MENU).show();
    });

    private final Thread goToMultiplayerMenu = new Thread(() -> {
        gameType = GameType.MULTIPLAYER;
        Menu.getMenu(MULTIPLAYER_MENU).show();
    });

    private final Thread goToMPGameModeSelection = new Thread(() -> {
        Menu.getMenu(MP_GAME_MODES_MENU).show();
    });

    private final Thread selectPVPGamemode = new Thread(() -> {
        gameMode = GameMode.PLAYER_VS_PLAYER;
        Menu.getMenu(DIFICULTY_MENU).show();
    });

    private final Thread selectPsVCGamemode = new Thread(() -> {
        gameMode = GameMode.PLAYERS_VS_PC;
        Menu.getMenu(DIFICULTY_MENU).show();
    });

    private final Thread playEasy = new Thread(() -> {
        difficulty = Difficulty.EASY;
        play();
    });

    private final Thread playMedium = new Thread(() -> {
        difficulty = Difficulty.MEDIUM;
        play();
    });

    private final Thread playHard = new Thread(() -> {
        difficulty = Difficulty.HARD;
        play();
    });

    private final Thread playCustom = new Thread(() -> {
        difficulty = Difficulty.CUSTOM;
        play();
    });

    private final Thread goToSettings = new Thread(() -> {
        changeNumberOfPlayers();

        Menu.getMenu(SETTINGS_MENU).show();
    });

    private final Thread changePlayerName = new Thread(() -> {
        changeNameOfPlayer();
    });

    private final Thread changeNumberOfPlayers = new Thread(() -> {
        changeNumberOfPlayers();
    });

    private final Thread goToSingleplayerRanking = new Thread(() -> {
        showRanking(Ranking.SINGLEPLAYER_RANKING_NAME);
    });

    private final Thread goToMultiplayerRanking = new Thread(() -> {
        showRanking(Ranking.MULTIPLAYER_RANKING_NAME);
    });

    private final Thread goToGameMenu = new Thread(() -> {
        if (gameType == GameType.SINGLEPLAYER) {
            Menu.getMenu(SINGLEPLAYER_MENU).show();
        } else if (gameType == GameType.MULTIPLAYER) {
            Menu.getMenu(MULTIPLAYER_MENU).show();
        }
    });

    private final Thread goToMainMenu = new Thread(() -> {
        Menu.getMenu(MAIN_MENU).show();
    });

    private final Thread exit = new Thread(() -> {
        System.exit(0);
    });

}
