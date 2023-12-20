package max.vanach.lesson_1;

import java.util.Scanner;
import java.util.List;
import java.util.LinkedList;

//        int tries = 0;
//        int random = new Random().nextInt(max - min) + min;
//
//        System.out.println(random);
//        System.out.println("I'm thinking of a certain number in the range of " + min + " to " + max + ". What number is it? ");
//
//        while (true) {
//
////            Answer validation
//            String answer = "";
//            while (true) {
//                answer = scan.nextLine();
//                if (isNumeric(answer)) {
//                    break;
//                }
//                System.out.println("The answer must be a number between 0 and 100");
//            }
//            int parsedAnswer = Integer.parseInt(answer);
//            tries++;
//
//            if (parsedAnswer > max || parsedAnswer < min) {
//                System.out.println("Number is out of range! (" + min + "-" + max + ") Try again...");
//            } else if (parsedAnswer > random) {
//                System.out.println("Too high! Try again...");
//            } else if (parsedAnswer < random) {
//                System.out.println("Too low! Try again...");
//            } else {
//                System.out.println("Correct! Congratulations!");
//                return tries;
//            }
//        }

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

    PLAYER_VS_PC("PLAYER VS PC"),
    PC_VS_PLAYER("PC VS PLAYER"),
    PLAYER_VS_PC_MIXED("PLAYER VS PC MIXED"),
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

    private final Scanner scan = new Scanner(System.in);
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

        InitializeMenus();
    }

    public void run() {
        CreateNewPlayer();

        while (true) {
            System.out.println("Create more players for multiplayer game puposes? (Y/N)");
            System.out.print(": ");

            String answear = scan.nextLine().toLowerCase();
            if (!answear.isEmpty() && (answear.equals("y") || answear.equals("n"))) {
                if (answear.equals("y")) {
                    while (true) {
                        System.out.println("How many players would you like to add? (Maximum is " + MAX_PLAYERS + ")");
                        System.out.print(": ");

                        String numberString = scan.nextLine().toLowerCase();
                        if (!numberString.isEmpty() && Main.IsNumeric(numberString)) {
                            int number = Integer.parseInt(numberString);

                            for (int i=0; i<number; i++) {
                                CreateNewPlayer();
                            }

                            break;
                        }
                    }
                }

                break;
            }
        }

        menu.show();
    }

    private void CreateNewPlayer() {
        String inputNickname;
        while (true) {
            Main.ClearScreen();
            if (players.size() > 0) {
                System.out.println("Set nickname for Player" + (players.size() + 1) + ".");
            }

            System.out.print("Nickname: ");
            inputNickname = scan.nextLine();
            if (!inputNickname.isEmpty()) {
                break;
            }
            System.out.println("Nickname cannot be empty!");
        }

        players.add(new Player(inputNickname));
    }

    private void InitializeMenus() {
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

    private static void Play() {

    }

    private static void ShowRanking() {

    }

    private static void ChangeNameOfPlayer() {

    }

    private static void NumberOfPlayers() {

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
        Play();
    });

    private static final Thread playMedium = new Thread(() -> {
        difficulty = Difficulty.MEDIUM;
        Play();
    });

    private static final Thread playHard = new Thread(() -> {
        difficulty = Difficulty.HARD;
        Play();
    });

    private static final Thread goToMultiplayerSettings = new Thread(() -> {
        multiplayerSettingsMenu.show();
    });

    private static final Thread changePlayerName = new Thread(() -> {
        ChangeNameOfPlayer();
    });

    private static final Thread changeNumberOfPlayers = new Thread(() -> {
        NumberOfPlayers();
    });

    private static final Thread goToRanking = new Thread(() -> {
        ShowRanking();
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
