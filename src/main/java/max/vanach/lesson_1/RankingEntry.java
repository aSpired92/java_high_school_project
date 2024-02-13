package max.vanach.lesson_1;

public class RankingEntry {
    private String nickname;
    private int numberOfTries;
    private int winCount;
    private boolean didWin;
    private boolean master;

    public RankingEntry(String nickname, int numberOfTries, int winCount, boolean isMaster) {
        this.nickname = nickname;
        this.numberOfTries = numberOfTries;
        this.winCount = winCount;
        this.didWin = false;
        this.master = isMaster;
    }
    
    public RankingEntry(String nickname, int numberOfTries, boolean didWin, boolean isMaster) {
        this.nickname = nickname;
        this.numberOfTries = numberOfTries;
        this.winCount = (didWin ? 1 : 0);
        this.didWin = didWin;
        this.master = isMaster;
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

    public boolean isMaster() {
        return master;
    }

    @Override
    public boolean equals(Object o) {
        return this.nickname.equals(((RankingEntry) o).nickname);
    }
}
