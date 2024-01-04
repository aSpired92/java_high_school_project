package max.vanach.lesson_1;

public class RankingEntry {
    private String nickname;
    private int numberOfTries;
    private int winCount;
    private boolean didWin;

    public RankingEntry(String nickname, int numberOfTries, int winCount) {
        this.nickname = nickname;
        this.numberOfTries = numberOfTries;
        this.winCount = winCount;
        this.didWin = false;
    }
    
    public RankingEntry(String nickname, int numberOfTries, boolean didWin) {
        this.nickname = nickname;
        this.numberOfTries = numberOfTries;
        this.winCount = (didWin ? 1 : 0);
        this.didWin = didWin;
    }

    public String getNickname() {
        return nickname;
    }

    public int getNumberOfTries() {
        return numberOfTries;
    }

    public int getWinCount() {
        return winCount;
    }

    public boolean getDidWin() {
        return didWin;
    }

    @Override
    public boolean equals(Object o) {
        return this.nickname.equals(((RankingEntry) o).nickname);
    }
}
