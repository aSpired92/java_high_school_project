package max.vanach.lesson_1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import max.vanach.lesson_1.utils.PlayerInput;

public class Bracket {
    private ArrayList<ArrayList<Player>> players;

    private int minRange;
    private int maxRange;

    private int nRounds;
    private int nPlayers;

    private int bestOf;

    public Bracket(ArrayList<Player> players, int minRange, int maxRange, int bestOf) {
        initializeBracket(players);

        this.minRange = minRange;
        this.maxRange = maxRange;

        this.bestOf = bestOf;
    }
    
    public Player play() {
        int currentRound = 0;
        Random rnd = new Random();
        rnd.setSeed(rnd.nextLong());

        Player winner = null;
        Iterator<Player> it = players.get(currentRound).iterator();

        while(currentRound < nRounds) {
            if (!it.hasNext()) {
                it = players.get(++currentRound).iterator();
                
                int last = players.get(currentRound).indexOf(null);

                if (last == -1) {
                    last = players.get(currentRound).size()-1;
                }

                if (players.get(currentRound).get(last-1) instanceof Computer) {
                    players.get(currentRound).set(last-1, null);
                }

                if (players.get(currentRound).stream().filter(p -> p != null).count() % 2 == 1) {
                    players.get(currentRound).set(last, new Computer());
                }

                displayBracket();
                PlayerInput.pressEnterToContinue();
                
                continue;
            }

            Player p1 = it.next();
            Player p2 = it.next();

            ArrayList<Integer> score = playBestOfN(p1, p2);

            if (score.get(0) > score.get(1)) {
                winner = p1;
            } else if (score.get(1) > score.get(0)) {
                winner = p2;
            }

            if (nRounds - currentRound != 1) {
                int n = players.get(currentRound+1).indexOf(null);
                players.get(currentRound + 1).set(n, winner);
            } else {
                break;
            }

        }

        return winner;
    }
    
    /**
     * The function plays a game between two players and returns the number of tries each player took to win.
     * 
     * @param player1 The first player participating in the game.
     * @param player2 Second player in the game.
     * @return {@code ArrayList} of tries each player had in game.
     */
    private ArrayList<Integer> playBestOf1(Player player1, Player player2) {
        ArrayList<Integer> tries = new ArrayList<>();
        Lobby lobby;
        Random r = new Random();

        int whoFirstGuessing = r.nextInt(2);

        Player p1 = (whoFirstGuessing == 0 ? player1 : player2);
        Player p2 = (whoFirstGuessing == 0 ? player2 : player1);

        lobby = new Lobby(p1, p2, minRange, maxRange);

        tries.add(lobby.play(GameType.TOURNAMENT).get(0));

        PlayerInput.clearScreen();
        System.out.println("Prepare for next round.");
        PlayerInput.pressEnterToContinue();

        lobby = new Lobby(p2, p1, minRange, maxRange);

        tries.add(lobby.play(GameType.TOURNAMENT).get(0));

        PlayerInput.clearScreen();

        int p1Tries = tries.get(0);
        int p2Tries = tries.get(1);

        if (p1Tries != p2Tries) {
            String winner = (p2Tries > p1Tries ? player1.getNickname() : player2.getNickname());
            System.out.println(winner + " wins.");
        } else {
            System.out.println("It's a draw.");
        }

        return tries;
    }

    /**
     * Plays a game between two players and returns the score of the game.
     * 
     * @param player1 The first player participating in the game.
     * @param player2 The second player in the game.
     * @return Final score of the game played between player1 and player2.
     */
    private ArrayList<Integer> playBestOfN(Player player1, Player player2) {
        ArrayList<Integer> score = new ArrayList<>(Collections.nCopies(2, 0));

        int s1, s2;

        while ((s1 = score.get(0)) + (s2 = score.get(1)) != bestOf && !(s1 > bestOf / 2 || s2 > bestOf / 2)) {
            ArrayList<Integer> tries = playBestOf1(player1, player2);

            if (tries.get(0) == tries.get(1)) { // Draw
                System.out.println("Round will be repeated.");
                PlayerInput.pressEnterToContinue();
                continue;
            }

            score.set(0, s1 + (tries.get(0) > tries.get(1) ? 0 : 1));
            score.set(1, s2 + (tries.get(0) > tries.get(1) ? 1 : 0));
        }

        return score;
    }

    /**
     * Generates and displays a bracket for a tournament.
     */
    public void displayBracket() {
        ArrayList<Iterator<Player>> playersIterator = new ArrayList<>();

        for (ArrayList<Player> ps : players) {
            playersIterator.add(ps.iterator());
        }

        String versus = "       vs       ";
        String hLineL = " *****     ";
        String hLineR = "     ***** ";
        String vLine = "     *     ";
        String emptySpace = new String(new char[16]).replace('\0', ' ');
        String emptySpaceShort = new String(new char[hLineL.length()]).replace('\0', ' ');
        String emptyPlace = new String(new char[16]).replace('\0', 'x');
        
        String out = "";
        
        for (int i=0; i<nRounds; i++) {
            if (i != 0) {
                out += emptySpaceShort;
            }
            
            out += roundString(i+1);
        }
        
        // Generating bracket as described in bracket.txt file

        out += "\n\n";
        // Line 1
        out += getNicknameOrEmpty(playersIterator.get(0), emptyPlace, emptySpace);
        // Line 2
        out += "\n";
        out += versus;
        
        if (nRounds > 1) {
            out += hLineL;
            // Line 3
            out += "\n";
            out += getNicknameOrEmpty(playersIterator.get(0), emptyPlace, emptySpace);
            out += vLine;
            out += getNicknameOrEmpty(playersIterator.get(1), emptyPlace, emptySpace);

            

            // Line 4
            out += "\n";
            out += emptySpace + hLineR + versus;
            
            if (nRounds > 2) {
                out += hLineL;
            } 
            // Line 5
            out += "\n";
            out += getNicknameOrEmpty(playersIterator.get(0), emptyPlace, emptySpace);
            out += vLine; 
            out += getNicknameOrEmpty(playersIterator.get(1), emptyPlace, emptySpace);
            
            if (nRounds > 2) {
                out += vLine;
            }
            // Line 6
            out += "\n";
            out += versus + hLineL;
            
            if (nRounds > 2) {
                out += emptySpace +  vLine;
            }
            // Line 7
            out += "\n";
            out += getNicknameOrEmpty(playersIterator.get(0), emptyPlace, emptySpace);
            
            if (nRounds > 2) {
                out += emptySpaceShort + emptySpace + vLine;
                out += getNicknameOrEmpty(playersIterator.get(2), emptyPlace, emptySpace);
            }
            // Line 8
            out += "\n";
            
            if (nRounds > 2) {
                out += emptySpace + emptySpaceShort + emptySpace + hLineR + versus;
                
                if (nRounds > 3) {
                    out += hLineL;
                }
                // Line 9
                out += "\n";
                out += getNicknameOrEmpty(playersIterator.get(0), emptyPlace, emptySpace);
                out += emptySpaceShort + emptySpace + vLine;
                out += getNicknameOrEmpty(playersIterator.get(2), emptyPlace, emptySpace);
                
                if (nRounds > 3) {
                    out += vLine;
                }
                // Line 10
                out += "\n";
                out += versus + hLineL + emptySpace + vLine;
                
                if (nRounds > 3) {
                    out += emptySpace + vLine;
                }
                // Line 11
                out += "\n";
                out += getNicknameOrEmpty(playersIterator.get(0), emptyPlace, emptySpace);
                out += vLine;
                out += getNicknameOrEmpty(playersIterator.get(1), emptyPlace, emptySpace);
                out += vLine;
                
                if (nRounds > 3) {
                    out += emptySpace + vLine;
                }
                // Line 12
                out += "\n";
                out += emptySpace + hLineR + versus + hLineL;
                
                if (nRounds > 3) {
                    out += emptySpace + vLine;
                }
                // Line 13
                out += "\n";
                out += getNicknameOrEmpty(playersIterator.get(0), emptyPlace, emptySpace);
                out += (nPlayers < 7) ? emptySpaceShort : vLine;
                out += getNicknameOrEmpty(playersIterator.get(1), emptyPlace, emptySpace);
                
                if (nRounds > 3) {
                    out += emptySpaceShort + emptySpace + vLine;
                }
                // Line 14
                out += "\n";
                out += (nPlayers < 7) ? (emptySpace + emptySpaceShort) : (versus + hLineL);
                
                if (nRounds > 3) {
                    out += emptySpace + emptySpaceShort + emptySpace + vLine;
                }
                // Line 15
                out += "\n";
                out += getNicknameOrEmpty(playersIterator.get(0), emptyPlace, emptySpace);
                
                if (nRounds > 3) {
                    out += emptySpaceShort + emptySpace + emptySpaceShort + emptySpace + vLine;
                    out += getNicknameOrEmpty(playersIterator.get(3), emptyPlace, emptySpace);
                    // Line 16
                    out += "\n";
                    out += emptySpace + emptySpaceShort + emptySpace + emptySpaceShort + emptySpace + hLineR + versus;
                    // Line 17
                    out += "\n";
                    out += getNicknameOrEmpty(playersIterator.get(0), emptyPlace, emptySpace);
                    out += emptySpaceShort + emptySpace + emptySpaceShort + emptySpace + vLine;
                    out += getNicknameOrEmpty(playersIterator.get(3), emptyPlace, emptySpace);
                    // Line 18
                    out += "\n";
                    out += versus + hLineL + emptySpace + emptySpaceShort + emptySpace + vLine;
                    // Line 19
                    out += "\n";
                    out += getNicknameOrEmpty(playersIterator.get(0), emptyPlace, emptySpace);
                    out += vLine;
                    out += getNicknameOrEmpty(playersIterator.get(1), emptyPlace, emptySpace);
                    out += emptySpaceShort + emptySpace + vLine;
                    // Line 20
                    out += "\n";
                    out += emptySpace + hLineR + versus + hLineL + emptySpace + vLine;
                    // Line 21
                    out += "\n";
                    out += getNicknameOrEmpty(playersIterator.get(0), emptyPlace, emptySpace);
                    out += (nPlayers < 11) ? emptySpaceShort : vLine;
                    out += getNicknameOrEmpty(playersIterator.get(1), emptyPlace, emptySpace);
                    out += vLine + emptySpace + vLine;
                    
                    // Line 22
                    out += "\n";
                    out += (nPlayers < 11) ? (emptySpace + emptySpaceShort) : (versus + hLineL); 
                    out += emptySpace + vLine + emptySpace + vLine;
                    // Line 23
                    out += "\n";
                    out += getNicknameOrEmpty(playersIterator.get(0), emptyPlace, emptySpace);
                    out += emptySpaceShort + emptySpace + vLine;
                    out += getNicknameOrEmpty(playersIterator.get(2), emptyPlace, emptySpace); 
                    out += vLine;
                    // Line 24
                    out += "\n";
                    out += emptySpace + emptySpaceShort + emptySpace + hLineR + versus + hLineL;
                    // Line 25
                    out += "\n";
                    out += getNicknameOrEmpty(playersIterator.get(0), emptyPlace, emptySpace);
                    out += emptySpaceShort + emptySpace;
                    out += (nPlayers < 13) ? emptySpaceShort : vLine;
                    out += getNicknameOrEmpty(playersIterator.get(2), emptyPlace, emptySpace);
                    // Line 26
                    out += "\n";
                    out += (nPlayers < 13) ? (emptySpace + emptySpaceShort) : (versus + hLineL);
                    out += emptySpace;
                    out += (nPlayers < 13) ? emptySpaceShort : vLine;
                    
                    // Line 27
                    out += "\n";
                    out += getNicknameOrEmpty(playersIterator.get(0), emptyPlace, emptySpace);
                    out += (nPlayers < 13) ? emptySpaceShort : vLine;
                    out += getNicknameOrEmpty(playersIterator.get(1), emptyPlace, emptySpace);
                    out += (nPlayers < 13) ? "" : vLine;
                    // Line 28
                    out += "\n";
                    
                    out += emptySpace;
                    out += (nPlayers < 13) ? (emptySpaceShort + emptySpace) : (hLineR + versus + hLineL);
                    // Line 29
                    out += "\n";
                    out += getNicknameOrEmpty(playersIterator.get(0), emptyPlace, emptySpace);
                    out += (nPlayers < 15 ) ? emptySpaceShort : vLine;
                    out += getNicknameOrEmpty(playersIterator.get(1), emptyPlace, emptySpace);
                    // Line 30
                    out += "\n";
                    out += (nPlayers < 15) ? (emptySpace + emptySpaceShort) : (versus + hLineL);
                    //Line 31
                    out += "\n";
                    out += getNicknameOrEmpty(playersIterator.get(0), emptyPlace, emptySpace);
                    
                } else {
                    out += "\n";
                }
            }
        } else {  
            out += "\n";  
            out += getNicknameOrEmpty(playersIterator.get(0), emptyPlace, emptySpace);
        }
        
        System.out.println(out);
    }

    /**
     * Initializes a bracket for a game with a given list of players, determining the
     * number of rounds and adding additional players if necessary.
     * 
     * @param players List of players participating.
     */
    private void initializeBracket(ArrayList<Player> players) {
        nPlayers = players.size();
        this.nRounds = 1;

        this.players = new ArrayList<>();

        this.players.add(players);

        if (nPlayers > 2) {
            this.nRounds = 2;
            int n = 2 * (int) (Math.ceil((double) nPlayers / 4));
            this.players.add(new ArrayList<>(Collections.nCopies(n, null)));
        }

        if (nPlayers > 4) {
            this.nRounds = 3;
            int n = 2 * (int) (Math.ceil((double) nPlayers / 8));
            this.players.add(new ArrayList<>(Collections.nCopies(n, null)));
        }

        if (nPlayers > 8) {
            this.nRounds = 4;
            this.players.add(new ArrayList<>(Collections.nCopies(2, null)));
        }

        shufflePlayers();

        if (nPlayers % 2 == 1) {
            nPlayers++;
            this.players.get(0).add(new Computer());
        }
    }

    private void shufflePlayers() {
        Random rnd = new Random();
        rnd.setSeed(rnd.nextInt());

        ArrayList<Player> temp = new ArrayList<>(Collections.nCopies(nPlayers, null));
        Iterator<Player> it = players.get(0).iterator();
        Player p;

        while (it.hasNext()) {


            int newIndex = rnd.nextInt(nPlayers);
            
            if (temp.get(newIndex) != null) {
                continue;
            }

            p = it.next();
            temp.set(newIndex, p);
            
        }

        players.set(0, temp);
    }

    private String roundString(int n) {
        return centrializeString("Round  " + n);
    }

    private String centrializeString(String str) {
        int n = 16 - str.length();

        int a = n / 2 + n % 2;
        int b = n / 2;

        return new String(
                new String(new char[a]).replace('\0', ' ') +
                str +
                new String(new char[b]).replace('\0', ' '));
    }

    private String getNicknameOrEmpty(Iterator<Player> it, String fillString, String emptyString) {
        if (!it.hasNext()) {
            return emptyString;
        }

        Player p = it.next();
        return (p != null ? centrializeString(p.getNickname()) : fillString);
    }

}