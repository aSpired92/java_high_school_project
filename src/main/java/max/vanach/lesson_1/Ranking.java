package max.vanach.lesson_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ranking {

    public List<RankingEntry> entries;

    public Ranking() {
        entries = new ArrayList<>();
    }

    private void add_entry(RankingEntry entry) {
        entries.add(entry);
    }

    public void read_ranking_from_file(String path) throws IOException {

        Path pathname = Paths.get(path);
        File file = new File(pathname.toString());

        if (file.exists()) {
            try (Scanner fileReader = new Scanner(file)) {
                String line;
                while (fileReader.hasNextLine() && !((line = fileReader.nextLine()).isEmpty())) {
                    String[] arr = line.split(" ", 2);
                    String nickname = arr[0];
                    int result = Integer.parseInt(arr[1]);

                    RankingEntry entry = new RankingEntry(nickname, result);
                    this.add_entry(entry);
                }
            }
        } else {
            Files.createFile(pathname);
        }
    }
}
