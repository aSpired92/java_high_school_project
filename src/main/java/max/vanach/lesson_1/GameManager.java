package max.vanach.lesson_1;

import java.util.List;
import java.util.LinkedList;
import java.util.regex.Pattern;

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
    HARD;
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

    private List<Player> players = new LinkedList<Player>();

    private static GameType gameType;
    private static GameMode gameMode;
    private static Difficulty difficulty;

    private static Menu menu;
    private static Menu gameSingleplayerMenu;
    private static Menu dificultyMenu;
    private static Menu gameMultiplayerMenu;
    private static Menu multiplayerSettingsMenu;

    private GameManager() {
        if (instance != null) {
            throw new RuntimeException("Not allowed. Please use getInstance() method");
        }

        initializeMenus();
    }

    public void run() {
        createNewPlayer();

        Pattern p = Pattern.compile("[yn]", Pattern.CASE_INSENSITIVE);
        String answear = PlayerInput
                .getInputString("Create more players for multiplayer game puposes? (Y/N)", ": ", "(Y)es or (N)o", p)
                .toLowerCase();

        if (answear.equals("y")) {
            createMultiplayerPlayers();
        }

        menu.show();
    }

    /**
     * The function creates a new player by prompting for a nickname and adding it to the list of
     * players.
     */
    private void createNewPlayer() {
        String title = "";
        String prompt = "Nickname: ";

        if (players.size() > 0) {
            title = "Set nickname for Player" + (players.size() + 1) + ".";
            prompt = ": ";
        }

        String inputNickname = PlayerInput.getInputString(title, prompt, "Nickname can't be empty!", null);

        players.add(new Player(inputNickname));
    }

    /**
     * The function creates a specified number of multiplayer players by calling the {@code createNewPlayer()}
     * function.
     */
    private void createMultiplayerPlayers() {
        int numberOfPlayers = PlayerInput
                .getInputInt("How many players would you like to add?", ": ", "Wrong number!",
                        1, MAX_PLAYERS, false, true);

        for (int i = 0; i < numberOfPlayers; i++) {
            createNewPlayer();
        }
    }

    /**
     * The function initializes menus and their options for a game menu system.
     */
    private void initializeMenus() {
        menu = new Menu("MAIN MENU");
        menu.add_option(new MenuOption("Singleplayer", setSinglePlayer));
        menu.add_option(new MenuOption("Multiplayer", setMultiPlayer));
        menu.add_option(new MenuOption("Exit", exit));

        gameSingleplayerMenu = new Menu("SINGLEPLAYER");
        gameSingleplayerMenu.add_option(new MenuOption("Play", goToDifficultyMenu));
        gameSingleplayerMenu.add_option(new MenuOption("Ranking", goToRanking));
        gameSingleplayerMenu.add_option(new MenuOption("Back", goToMainMenu));

        dificultyMenu = new Menu("DIFFICULTY");
        dificultyMenu.add_option(new MenuOption("EASY", playEasy));
        dificultyMenu.add_option(new MenuOption("MEDIUM", playMedium));
        dificultyMenu.add_option(new MenuOption("HARD", playHard));
        dificultyMenu.add_option(new MenuOption("Back", goToGameMenu));

        gameMultiplayerMenu = new Menu("MULTIPLAYER");
        gameMultiplayerMenu.add_option(new MenuOption("Play", goToDifficultyMenu));
        gameMultiplayerMenu.add_option(new MenuOption("Ranking", goToRanking));
        gameMultiplayerMenu.add_option(new MenuOption("Settings", goToMultiplayerSettings));
        gameMultiplayerMenu.add_option(new MenuOption("Back", goToMainMenu));

        multiplayerSettingsMenu = new Menu("MULTIPLAYER SETTINGS");
        multiplayerSettingsMenu.add_option(new MenuOption("Change number of players", goToDifficultyMenu));
        multiplayerSettingsMenu.add_option(new MenuOption("Change name of player", goToDifficultyMenu));
        multiplayerSettingsMenu.add_option(new MenuOption("Back", goToGameMenu));
    }

    private static void play() {

    }

    private static void showRanking() {

    }

    private static void changeNameOfPlayer() {

    }

    private static void numberOfPlayers() {

    }

    private static final Thread setSinglePlayer = new Thread(() -> {
        gameType = GameType.SINGLEPLAYER;
        gameSingleplayerMenu.show();
    });

    private static final Thread setMultiPlayer = new Thread(() -> {
        gameType = GameType.MULTIPLAYER;
        gameMultiplayerMenu.show();
    });

    private static final Thread goToDifficultyMenu = new Thread(() -> {
        dificultyMenu.show();
    });

    private static final Thread playEasy = new Thread(() -> {
        difficulty = Difficulty.EASY;
        play();
    });

    private static final Thread playMedium = new Thread(() -> {
        difficulty = Difficulty.MEDIUM;
        play();
    });

    private static final Thread playHard = new Thread(() -> {
        difficulty = Difficulty.HARD;
        play();
    });

    private static final Thread goToMultiplayerSettings = new Thread(() -> {
        multiplayerSettingsMenu.show();
    });

    private static final Thread changePlayerName = new Thread(() -> {
        changeNameOfPlayer();
    });

    private static final Thread changeNumberOfPlayers = new Thread(() -> {
        numberOfPlayers();
    });

    private static final Thread goToRanking = new Thread(() -> {
        showRanking();
    });

    private static final Thread goToGameMenu = new Thread(() -> {
        if (gameType == GameType.SINGLEPLAYER) {
            gameSingleplayerMenu.show();
        } else if (gameType == GameType.SINGLEPLAYER) {
            gameMultiplayerMenu.show();
        }
    });

    private static final Thread goToMainMenu = new Thread(() -> {
        menu.show();
    });

    private static final Thread exit = new Thread(() -> {
        System.exit(0);
    });

}
