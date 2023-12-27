package max.vanach.lesson_1;

public class RankingEntry {
    private String nickname;
    private int numberOfTries;

    public RankingEntry(String nickname, int numberOfTries) {
        this.nickname = nickname;
        this.numberOfTries = numberOfTries;
    }

    public String getNickname() {
        return nickname;
    }

    public int getNumberOfTries() {
        return numberOfTries;
    }

    @Override
    public boolean equals(Object o) {
        return this.nickname.equals(((RankingEntry) o).nickname);
    }
}
