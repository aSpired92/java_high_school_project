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

    private GameType gameType;
    private GameMode gameMode;
    private Difficulty difficulty;

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
                .getInputString("Create more players for multiplayer game puposes? (Y/N)", ": ", "(Y)es or (N)o", p, false)
                .toLowerCase();

        if (answear.equals("y")) {
            createMultiplayerPlayers();
        }

        Menu.getMenu("main").show();
    }

    /**
     * The function creates a new player by prompting for a nickname and adding it
     * to the list of
     * players.
     */
    private void createNewPlayer() {
        String title = "";
        String prompt = "Nickname: ";

        if (players.size() > 0) {
            title = "Set nickname for Player" + (players.size() + 1) + ".";
            prompt = ": ";
        }

        String inputNickname = PlayerInput.getInputString(title, prompt, "Nickname can't be empty!", null, false);

        players.add(new Player(inputNickname));
    }

    /**
     * The function creates a specified number of multiplayer players by calling the
     * {@code createNewPlayer()}
     * function.
     */
    private void createMultiplayerPlayers() {
        int numberOfPlayers = PlayerInput
                .getInputInt("How many players would you like to add? (Maximum " + MAX_PLAYERS + ")" , ": ", "Wrong number!",
                        1, MAX_PLAYERS, false);

        for (int i = 0; i < numberOfPlayers; i++) {
            createNewPlayer();
        }
    }

    /**
     * The function initializes menus and their options for a game menu system.
     */
    private void initializeMenus() {
        Menu.createMenu("main", "MAIN MENU");
        Menu.addOptionToMenu("main", new MenuOption("Singleplayer", setSinglePlayer));
        Menu.addOptionToMenu("main", new MenuOption("Multiplayer", setMultiPlayer));
        Menu.addOptionToMenu("main", new MenuOption("Exit", exit));

        Menu.createMenu("gameSingleplayerMenu", "SINGLEPLAYER");
        Menu.addOptionToMenu("gameSingleplayerMenu", new MenuOption("Play", goToSPGameModeSelection));
        Menu.addOptionToMenu("gameSingleplayerMenu", new MenuOption("Ranking", goToRanking));
        Menu.addOptionToMenu("gameSingleplayerMenu", new MenuOption("Back", goToMainMenu));

        Menu.createMenu("gameSPGameModesMenu", "GAME MODE");
        Menu.addOptionToMenu("gameSPGameModesMenu", new MenuOption("Player versus computer", selectPVCGamemode));
        Menu.addOptionToMenu("gameSPGameModesMenu", new MenuOption("Computer versus player", selectCVPGamemode));
        Menu.addOptionToMenu("gameSPGameModesMenu", new MenuOption("Player versus computer mixed", selectPVCMGamemode));

        Menu.createMenu("gameMultiplayerMenu", "MULTIPLAYER");
        Menu.addOptionToMenu("gameMultiplayerMenu", new MenuOption("Play", goToMPGameModeSelection));
        Menu.addOptionToMenu("gameMultiplayerMenu", new MenuOption("Ranking", goToRanking));
        Menu.addOptionToMenu("gameMultiplayerMenu", new MenuOption("Settings", goToMultiplayerSettings));
        Menu.addOptionToMenu("gameMultiplayerMenu", new MenuOption("Back", goToMainMenu));

        Menu.createMenu("gameMPGameModesMenu", "GAME MODE");
        Menu.addOptionToMenu("gameMPGameModesMenu", new MenuOption("Player versus Player", selectPVPGamemode));
        Menu.addOptionToMenu("gameMPGameModesMenu", new MenuOption("Players versus Computer", selectPsVCGamemode));

        Menu.createMenu("multiplayerSettingsMenu", "MULTIPLAYER SETTINGS");
        Menu.addOptionToMenu("multiplayerSettingsMenu", new MenuOption("Change number of players", changeNumberOfPlayers));
        Menu.addOptionToMenu("multiplayerSettingsMenu", new MenuOption("Change name of player", changePlayerName));
        Menu.addOptionToMenu("multiplayerSettingsMenu", new MenuOption("Back", goToGameMenu));

        Menu.createMenu("dificultyMenu", "DIFFICULTY");
        Menu.addOptionToMenu("dificultyMenu", new MenuOption("EASY", playEasy));
        Menu.addOptionToMenu("dificultyMenu", new MenuOption("MEDIUM", playMedium));
        Menu.addOptionToMenu("dificultyMenu", new MenuOption("HARD", playHard));
        Menu.addOptionToMenu("dificultyMenu", new MenuOption("Back", goToGameMenu));
    }

    private void play() {
        if (gameType == GameType.SINGLEPLAYER) {
            Computer computer = new Computer();

            switch (gameMode) {
                case PLAYER_VS_PC: {
                    Lobby lobby = new Lobby(computer, players.get(0), difficulty);

                    lobby.play();

                    break;
                }
                case PC_VS_PLAYER: {
                    
                    Lobby lobby = new Lobby(players.get(0), computer, difficulty);

                    lobby.play();

                    break;
                }
                case PLAYER_VS_PC_MIXED:
                    break;
                default:
                    break;
            }
        } else if (gameType == GameType.MULTIPLAYER) {
            switch (gameMode) {
                case PLAYER_VS_PLAYER:

                    break;
                case PLAYERS_VS_PC:

                    break;
                default:
                    break;
            }
        }
    }

    private void showRanking() {

    }

    private void changeNameOfPlayer() {

    }

    private void numberOfPlayers() {

    }

    private final Thread setSinglePlayer = new Thread(() -> {
        gameType = GameType.SINGLEPLAYER;
        Menu.getMenu("gameSingleplayerMenu").show();
    });

    private final Thread goToSPGameModeSelection = new Thread(() -> {
        Menu.getMenu("gameSPGameModesMenu").show();
    });

    private final Thread selectPVCGamemode = new Thread(() -> {
        gameMode = GameMode.PLAYER_VS_PC;
        Menu.getMenu("dificultyMenu").show();
        
    });

    private final Thread selectCVPGamemode = new Thread(() -> {
        gameMode = GameMode.PC_VS_PLAYER;
        Menu.getMenu("dificultyMenu").show();
    });

    private final Thread selectPVCMGamemode = new Thread(() -> {
        gameMode = GameMode.PLAYER_VS_PC_MIXED;
        Menu.getMenu("dificultyMenu").show();
    });

    private final Thread setMultiPlayer = new Thread(() -> {
        gameType = GameType.MULTIPLAYER;
        Menu.getMenu("gameMultiplayerMenu").show();
    });

    private final Thread goToMPGameModeSelection = new Thread(() -> {
        Menu.getMenu("gameMPGameModesMenu").show();
    });

    private final Thread selectPVPGamemode = new Thread(() -> {
        gameMode = GameMode.PLAYER_VS_PLAYER;
        Menu.getMenu("dificultyMenu").show();
    });

    private final Thread selectPsVCGamemode = new Thread(() -> {
        gameMode = GameMode.PLAYERS_VS_PC;
        Menu.getMenu("dificultyMenu").show();
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

    private final Thread goToMultiplayerSettings = new Thread(() -> {
        Menu.getMenu("multiplayerSettingsMenu").show();
    });

    private final Thread changePlayerName = new Thread(() -> {
        changeNameOfPlayer();
    });

    private final Thread changeNumberOfPlayers = new Thread(() -> {
        numberOfPlayers();
    });

    private final Thread goToRanking = new Thread(() -> {
        showRanking();
    });

    private final Thread goToGameMenu = new Thread(() -> {
        if (gameType == GameType.SINGLEPLAYER) {
            Menu.getMenu("gameSingleplayerMenu").show();
        } else if (gameType == GameType.MULTIPLAYER) {
            Menu.getMenu("gameMultiplayerMenu").show();
        }
    });

    private final Thread goToMainMenu = new Thread(() -> {
        Menu.getMenu("menu").show();
    });

    private final Thread exit = new Thread(() -> {
        System.exit(0);
    });

}
