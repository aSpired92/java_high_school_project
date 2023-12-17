package max.vanach.lesson_1;

import java.util.Scanner;


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

public final class GameManager {
    private static volatile GameManager instance = null;

    private GameManager() {
        if(instance != null) {
            throw new RuntimeException("Not allowed. Please use getInstance() method");
        }
    }

    public static GameManager getInstance() {

        if(instance == null) {
            synchronized(GameManager.class) {
                if(instance == null) {
                    instance = new GameManager();
                }
            }
        }

        return instance;
    }

    public void run(){
        InitializeMenus();
        AskForPlayer();
        menu.show();
    }

    private enum GameMode {

        SINGLEPLAYER("SINGLEPLAYER"),
        REVERSED("REVERSED SINGLEPLAYER"),
        MULTIPLAYER("MULTIPLAYER");

        private final String name;

        GameMode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private enum Difficulty {

        EASY,
        MEDIUM,
        HARD;
    }


    private final Scanner scan = new Scanner(System.in);
    private Player player;
    private static Menu menu;
    private static GameMode gameMode;
    private static Difficulty difficulty;
    private static Menu gameMenu;
    private static Menu dificultyMenu;

    private void AskForPlayer() {
        String inputNickname;
        while (true) {
            System.out.print("Nickname: ");
            inputNickname = scan.nextLine();
            if (!inputNickname.isEmpty()) {
                break;
            }
            System.out.println("Nickname cannot be empty!");
        }

        player = new Player(inputNickname);
    }

    private void InitializeMenus() {
        menu = new Menu("MAIN MENU");
        menu.add_option(new MenuOption("Singleplayer", setSinglePlayer));
        menu.add_option(new MenuOption("Reversed Singleplayer", setReversedSinglePlayer));
        menu.add_option(new MenuOption("Multiplayer", setMultiPlayer));
        menu.add_option(new MenuOption("Exit", exit));

        gameMenu = new Menu("GAME MENU");
        gameMenu.add_option(new MenuOption("Play", goToDifficultyMenu));
        gameMenu.add_option(new MenuOption("Ranking", goToRanking));
        gameMenu.add_option(new MenuOption("Back", goToMainMenu));

        dificultyMenu = new Menu("DIFFICULTY");
        dificultyMenu.add_option(new MenuOption("EASY", playEasy));
        dificultyMenu.add_option(new MenuOption("MEDIUM", playMedium));
        dificultyMenu.add_option(new MenuOption("HARD", playHard));
        dificultyMenu.add_option(new MenuOption("Back", goToGameMenu));
    }

    private static void SetGameMode(GameMode mode) {
        gameMode = mode;
        gameMenu.title = mode.toString();
    }

    private static void Play() {

    }

    private static void ShowRanking() {

    }

    private static final Thread goToRanking = new Thread(() -> {
        ShowRanking();
    });

    private static final Thread setSinglePlayer = new Thread(() -> {
        SetGameMode(GameMode.SINGLEPLAYER);
        gameMenu.show();
    });

    private static final Thread setMultiPlayer = new Thread(() -> {
        SetGameMode(GameMode.MULTIPLAYER);
        gameMenu.show();
    });

    private static final Thread setReversedSinglePlayer = new Thread(() -> {
        SetGameMode(GameMode.REVERSED);
        gameMenu.show();
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

    private static final Thread goToMainMenu = new Thread(() -> {
        menu.show();
    });

    private static final Thread goToGameMenu = new Thread(() -> {
        gameMenu.show();
    });

    private static final Thread goToDifficultyMenu = new Thread(() -> {
        dificultyMenu.show();
    });

    private static final Thread exit = new Thread(() -> {
        System.out.println("Bye :D");
    });


}
