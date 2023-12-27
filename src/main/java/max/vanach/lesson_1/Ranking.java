package max.vanach.lesson_1;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;

import max.vanach.lesson_1.utils.PlayerInput;

public class Ranking {

    private static Ranking instance = null;

    private static final Path RANKING_FILE_PATH = Paths.get("./ranking.txt");
    private static final Path TEMP_FILE_NAME = Paths.get("./temp.txt");

    public static final String SINGLEPLAYER_RANKING_NAME = "singleplayer";
    public static final String MULTIPLAYER_PVP_RANKING_NAME = "multiplayerPVP";
    public static final String MULTIPLAYER_PVC_RANKING_NAME = "multiplayerPVC";

    private Map<String, List<RankingEntry>> rankings = new HashMap<>() {
        {
            put(SINGLEPLAYER_RANKING_NAME, new LinkedList<RankingEntry>());
            put(MULTIPLAYER_PVP_RANKING_NAME, new LinkedList<RankingEntry>());
            put(MULTIPLAYER_PVC_RANKING_NAME, new LinkedList<RankingEntry>());
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

    public void addEntry(String ranking, RankingEntry entry) {
        if (rankings.containsKey(ranking)) {
            List<RankingEntry> l = rankings.get(ranking);
            int index = l.indexOf(entry);

            if (index != -1) {
                if (l.get(index).getNumberOfTries() > entry.getNumberOfTries()) {
                    l.set(index, entry);
                }
            } else {
                l.add(entry);
            }
        }
    }

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
                        if (!key.equals(SINGLEPLAYER_RANKING_NAME) && !key.equals(MULTIPLAYER_PVP_RANKING_NAME)
                                && !key.equals(MULTIPLAYER_PVC_RANKING_NAME)) {
                            key = "";
                            continue;
                        }

                        if (!line.contains("=")) {
                            continue;
                        }

                        String[] arr = line.split("=", 2);
                        String nickname = arr[0];
                        String recordString = arr[1];

                        Matcher m = PlayerInput.PLAYER_NICKNAME_REGEX_PATERN.matcher(nickname);
                        if (!m.matches() || !PlayerInput.isNumeric(recordString)) {
                            continue;
                        }

                        int result = Integer.parseInt(arr[1]);

                        RankingEntry entry = new RankingEntry(nickname, result);
                        this.addEntry(key, entry);
                    }
                }
            }
        }
    }

    private String serializeRankings() {
        String result = "";

        for (Map.Entry<String, List<RankingEntry>> ranking : rankings.entrySet()) {
            String key = ranking.getKey();
            List<RankingEntry> value = ranking.getValue();

            result += "[" + key + "]\n";

            for (RankingEntry entry : value) {
                result += entry.getNickname() + "=" + entry.getNumberOfTries() + "\n";
            }
        }

        return result;
    }
}
