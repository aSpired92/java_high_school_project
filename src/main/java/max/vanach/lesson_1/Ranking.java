package max.vanach.lesson_1;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;

import max.vanach.lesson_1.utils.PlayerInput;

public class Ranking {

    private static Ranking instance = null;

    public static final String SINGLEPLAYER_RANKING_NAME = "singleplayer";
    public static final String MULTIPLAYER_RANKING_NAME = "multiplayer";
    public static final String TOURNAMENT_RANKING_NAME = "tournament";

    private static final Path RANKING_FILE_PATH = Paths.get("./ranking.txt");
    private static final Path TEMP_FILE_NAME = Paths.get("./temp.txt");

    private Map<String, List<RankingEntry>> rankings = new HashMap<>() {
        {
            put(SINGLEPLAYER_RANKING_NAME, new LinkedList<RankingEntry>());
            put(MULTIPLAYER_RANKING_NAME, new LinkedList<RankingEntry>());
            put(TOURNAMENT_RANKING_NAME, new LinkedList<RankingEntry>());
        }
    };

    private Ranking() {
        try {
            readRankingFromFile();
        } catch (IOException e) {
        }
    }

    public static Ranking getInstance() {
        if (instance == null) {
            instance = new Ranking();
        }

        return instance;
    }

    /**
     * Updates a ranking entry in a rankings map, either by replacing it with a new
     * entry or adding it if it doesn't exist.
     * 
     * @param ranking String that represents the name of the ranking to be updated.
     * @param entry   Ranking entry with which ranking will be updated.
     */
    public void updateEntry(String ranking, RankingEntry entry) {
        if (rankings.containsKey(ranking)) {
            List<RankingEntry> l = rankings.get(ranking);
            int index = l.indexOf(entry);

            if (index != -1) {
                RankingEntry oldEntry = l.get(index);
                RankingEntry e;

                int n = (oldEntry.getNumberOfTries() < entry.getNumberOfTries()) ? oldEntry.getNumberOfTries() : entry.getNumberOfTries();

                e = new RankingEntry(
                        entry.getNickname(),
                        n,
                        oldEntry.getWinCount() + (entry.getDidWin() ? 1 : 0),
                        entry.isMaster());
                

                l.set(index, e);
            } else {
                l.add(entry);
            }
        }
    }

    /**
     * Saves a ranking to a file, deleting the old rankings file if it exists.
     */
    public void saveRankingToFile() throws IOException, SecurityException {
        File tempFile = new File(TEMP_FILE_NAME.toString());
        File resultsFile = new File(RANKING_FILE_PATH.toString());

        if (tempFile.exists()) {
            if (!tempFile.delete()) {
                System.err.println("Could not delete old temp file");
                PlayerInput.pressEnterToContinue();
                return;
            }
        }

        if (!tempFile.createNewFile()) {
            System.err.println("Could not create temp file");
            PlayerInput.pressEnterToContinue();
            return;
        }

        try (PrintWriter fileWriter = new PrintWriter(tempFile)) {
            fileWriter.write(serializeRankings());
        }

        if (resultsFile.exists() && !resultsFile.delete()) {
            System.err.println("Could not delete old rankings file");
            PlayerInput.pressEnterToContinue();
            return;
        }

        if (!tempFile.renameTo(resultsFile)) {
            System.err.println("Could not rename temp file to rankings file");
            PlayerInput.pressEnterToContinue();
            return;
        }
    }

    /**
     * The function reads a ranking from a file and adds the entries to the
     * corresponding ranking key.
     */
    public void readRankingFromFile() throws IOException {
        File file = new File(RANKING_FILE_PATH.toString());

        if (file.exists()) {
            try (Scanner fileReader = new Scanner(file)) {
                String line;
                String key = "";
                while (fileReader.hasNextLine() && !((line = fileReader.nextLine()).isEmpty())) {
                    if (line.startsWith("[") && line.endsWith("]")) {
                        key = line.substring(1, line.length() - 1);
                        continue;
                    }

                    if (!key.equals("")) {
                        if (!key.equals(SINGLEPLAYER_RANKING_NAME) && 
                                !key.equals(MULTIPLAYER_RANKING_NAME) && 
                                !key.equals(TOURNAMENT_RANKING_NAME)) {
                            key = "";
                            continue;
                        }

                        String[] values = line.split("=", 2);
                        if (values.length < 2) {
                            continue;
                        }

                        String nickname = values[0];

                        values = values[1].split(",", 3);
                        if (values.length < 3) {
                            continue;
                        }

                        String recordString = values[0];
                        String winCountString = values[1];
                        String isMasterString = values[2];

                        Matcher m = PlayerInput.PLAYER_NICKNAME_REGEX_PATERN.matcher(nickname);
                        if (!m.matches() || 
                                !PlayerInput.isNumeric(recordString) || 
                                !PlayerInput.isNumeric(winCountString) ||
                                !(isMasterString.equals("true") || isMasterString.equals("false"))) {
                            continue;
                        }

                        int record = Integer.parseInt(values[0]);
                        int winCount = Integer.parseInt(values[1]);
                        boolean isMaster = Boolean.parseBoolean(values[2]);

                        RankingEntry entry = new RankingEntry(nickname, record, winCount, isMaster);
                        this.updateEntry(key, entry);
                    }
                }
            }
        }
    }

    /**
     * The function serializes a map of rankings into a string format.
     * 
     * @return The method is returning a string representation of the rankings.
     */
    private String serializeRankings() {
        String result = "";

        for (Map.Entry<String, List<RankingEntry>> ranking : rankings.entrySet()) {
            String key = ranking.getKey();
            List<RankingEntry> value = ranking.getValue();

            result += "[" + key + "]\n";

            for (RankingEntry entry : value) {
                result += entry.getNickname() + "=" + 
                    entry.getNumberOfTries() + "," + 
                    entry.getWinCount() + "," + 
                    String.valueOf(entry.isMaster()) + "\n";
            }
        }

        return result;
    }

    /**
     * Displays a ranking of players sorted by their best number of tries in either
     * the singleplayer or multiplayer mode.
     * 
     * @param rankingName String that represents the name of the ranking to be
     *                    displayed.
     */
    public void displayRanking(String rankingName) {
        ArrayList<RankingEntry> entries;
        String title = "";

        if (rankingName.equals(SINGLEPLAYER_RANKING_NAME) && rankings.containsKey(SINGLEPLAYER_RANKING_NAME)) {
            entries = new ArrayList<>(rankings.get(SINGLEPLAYER_RANKING_NAME));
            title = "Singleplayer ranking";
        } else if (rankingName.equals(MULTIPLAYER_RANKING_NAME) && rankings.containsKey(MULTIPLAYER_RANKING_NAME)) {
            entries = new ArrayList<>(rankings.get(MULTIPLAYER_RANKING_NAME));
            title = "Multiplayer ranking";
        }else if (rankingName.equals(TOURNAMENT_RANKING_NAME) && rankings.containsKey(TOURNAMENT_RANKING_NAME)) {
            entries = new ArrayList<>(rankings.get(TOURNAMENT_RANKING_NAME));
            title = "Tournament ranking";
        } else {
            return;
        }

        Collections.sort(entries, new Comparator<RankingEntry>() {
            public int compare(RankingEntry e1, RankingEntry e2) {
                return Integer.compare(e1.getNumberOfTries(), e2.getNumberOfTries());
            }
        });

        int maxIntLength = (Integer.toString(Integer.MAX_VALUE)).length() + 1;

        PlayerInput.clearScreen();

        System.out.println(title);

        for (int i = 0; i < entries.size(); i++) {
            RankingEntry e = entries.get(i);

            String out = "";

            String space1 = new String(new char[17 - e.getNickname().length()]).replace('\0', ' ');
            String space2 = new String(new char[maxIntLength - Integer.toString(e.getNumberOfTries()).length()])
                    .replace('\0', ' ');
            

            System.out.print((i + 1) + ". " + e.getNickname() + space1 + "| Best number of tries: "
                    + e.getNumberOfTries() + space2 + "| Number of wins: " + e.getWinCount());

            out += (i + 1) + ". " + e.getNickname() + space1;

            if (e.getNumberOfTries() > 0) {
                out += "| Best number of tries: " + e.getNumberOfTries() + space2;
            }

            out += "| Number of wins: " + e.getWinCount();
            
            if (e.isMaster()) {
                String space3 = new String(new char[maxIntLength - Integer.toString(e.getWinCount()).length()])
                        .replace('\0', ' ');
                out += space3;
                out += " | Master";
            }

            System.out.println(out);
        }

        System.out.println();
        PlayerInput.pressEnterToContinue();
    }

    public static String getRankingNameFromGameType(GameType gameType) {
        switch (gameType) {
            case SINGLEPLAYER:
                return SINGLEPLAYER_RANKING_NAME;
        
            case MULTIPLAYER:
                return MULTIPLAYER_RANKING_NAME;
        
            case TOURNAMENT:
                return TOURNAMENT_RANKING_NAME;
        
            default:
                return "";
        }
    }

    public static boolean isPlayerMaster(String nickname) {
        return instance.rankings.get(TOURNAMENT_RANKING_NAME).stream().anyMatch(e -> (e.getNickname().equals(nickname) && e.isMaster()));
    }
}
