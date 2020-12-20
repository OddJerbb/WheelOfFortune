package wheeloffortune;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class Round {

    private static int currentWedge;

    private static boolean flag;
    private static int iterator; //iterator

    private static Thread t1;
    private static Thread t2;
    private static Thread t3;

    private static String letter;
    private static String guesses; //Guessed letters

    private static String[] bank; //Available letters

    //Objects used to generate a puzzle
    private static Random rand = new Random();
    private File puzzleF;
    private Scanner scan;
    private static String solution;
    private static String puzzle;

    private static final int[] wedges = {500, 5000, -1, 300, 500, 450, 500, 800, -2,
        700, 500, 650, -1, 900, 500, 350, 600, 500, 400, 550, 800, 300, 700, 900};

    public Round() throws IOException {
        guesses = "-&, ";
        bank = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        choosePuzzle();

        iterator = 0;
        flag = true;
        play(Setup.getPlayers().get(0), 0);
    }

    public void choosePuzzle() throws IOException {
        switch (rand.nextInt(5)) {
            case 0:
                puzzleF = new File("wheeloffortune/Puzzles/Around the House.txt");
                break;
            case 1:
                puzzleF = new File("wheeloffortune/Puzzles/Before and After.txt");
                break;
            case 2:
                puzzleF = new File("wheeloffortune/Puzzles/Characters.txt");
                break;
            case 3:
                puzzleF = new File("wheeloffortune/Puzzles/Food and Drink.txt");
                break;
            case 4:
                puzzleF = new File("wheeloffortune/Puzzles/Phrase.txt");
                break;
            default:
                break;
        }
        GameWindow.setCategory(puzzleF.getName().substring(0, puzzleF.getName().length() - 4));

        scan = new Scanner(puzzleF);

        int num = rand.nextInt(35);
        for (int x = 0; x < num + 1; x++) {
            if (x == num) {
                solution = scan.nextLine();
                x = num + 1;
            } else {
                scan.nextLine();
            }
        }
        setPuzzle();
    }

    public static void setPuzzle() {
        puzzle = "";

        for (int x = 0; x < solution.length(); x++) {
            if (guesses.contains(Character.toString(solution.charAt(x)))) {
                puzzle = puzzle.concat(Character.toString(solution.charAt(x)));
            } else {
                puzzle = puzzle.concat(Character.toString('#'));
            }
        }
        GameWindow.setPuzzle();
    }

    public static String getPuzzle() {
        return puzzle;
    }

    public static void play(Player P, int cW) throws IOException {
        currentWedge = cW;

        t1 = new Thread(new Runnable() {
            public void run() {
                new CommandPrompt(P);
            }
        });

        t2 = new Thread(new Runnable() {
            public void run() {
                //Player has played a vowel
                if ("aeiou".contains(letter.toLowerCase()) && Arrays.asList(bank).contains(letter.toLowerCase()) && flag) {
                    P.addRoundBalance(-250);
                    GameWindow.updatePlayer(iterator);

                    for (int x = 0; x < bank.length; x++) {
                        if (bank[x].equalsIgnoreCase(letter)) {
                            bank[x] = "#";
                        }
                    }
                    GameWindow.updateLetters(bank);

                    if (solution.contains(letter.toUpperCase())) {
                        guesses = guesses.concat(letter.toUpperCase());
                        setPuzzle();

                        try {
                            play(P, cW);
                        } catch (IOException ex) {
                            Logger.getLogger(Round.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } else {
                        try {
                            iterator += 1;
                            play(Setup.getPlayers().get(iterator %= 3), cW);
                        } catch (IOException ex) {
                            Logger.getLogger(Round.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } //Player has chosen to spin the wheel
                else {
                    //Spin a new wedge!
                    currentWedge = spin(cW);

                    //Player has spun a Bankrupt!! or Lose a Turn! segment
                    if (wedges[currentWedge] < 0) {
                        //Bankrupt!!
                        if (wedges[currentWedge] == -1) {
                            P.clearRoundBalance();
                            GameWindow.updatePlayer(iterator);
                            try {
                                iterator += 1;
                                play(Setup.getPlayers().get(iterator %= 3), currentWedge);
                            } catch (IOException ex) {
                                Logger.getLogger(Round.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } //Lose a Turn!
                        else if (wedges[currentWedge] == -2) {
                            try {
                                iterator += 1;
                                play(Setup.getPlayers().get(iterator %= 3), currentWedge);
                            } catch (IOException ex) {
                                Logger.getLogger(Round.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } 

                    //Player will now choose a consonant
                    else {
                        new CommandPrompt(P, currentWedge);
                    }
                }
            }
        });

        t3 = new Thread(new Runnable() {
            public void run() {
                for (int x = 0; x < bank.length; x++) {
                    if (bank[x].equalsIgnoreCase(letter)) {
                        bank[x] = "#";
                    }
                }
                GameWindow.updateLetters(bank);

                if (solution.contains(letter.toUpperCase()) && !(guesses.contains(letter.toLowerCase()))) {
                    guesses = guesses.concat(letter.toUpperCase());
                    setPuzzle();
                    P.addRoundBalance(wedges[currentWedge]);
                    GameWindow.updatePlayer(iterator);
                    try {
                        play(P, currentWedge);
                    } catch (IOException ex) {
                        Logger.getLogger(Round.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    try {
                        iterator += 1;
                        play(Setup.getPlayers().get(iterator %= 3), currentWedge);
                    } catch (IOException ex) {
                        Logger.getLogger(Round.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        t1.start();
    }

    public static int spin(int wedge) {
        int newWedge = (int) (wedge + rand.nextInt(10) * .233 * 24);

        int change = Math.abs(newWedge - wedge);

        while (newWedge >= 24) {
            newWedge = newWedge - 24;
        }

        GameWindow.spinWheel(15 * change);
        return newWedge;
    }

    public static void clearBoard(Player p) {
        if (p.getRoundBalance() < 500) {
            p.addRoundBalance(500 - p.getRoundBalance());
            p.addBalance(p.getRoundBalance());
            p.clearRoundBalance();
        } else {
            p.addBalance(p.getRoundBalance());
            p.clearRoundBalance();
        }

        switch (p.getNum()) {
            case 1:
                Setup.getPlayers().get(1).clearRoundBalance();
                Setup.getPlayers().get(2).clearRoundBalance();
                break;
            case 2:
                Setup.getPlayers().get(0).clearRoundBalance();
                Setup.getPlayers().get(2).clearRoundBalance();
                break;
            case 3:
                Setup.getPlayers().get(0).clearRoundBalance();
                Setup.getPlayers().get(1).clearRoundBalance();
                break;
            default:
                break;
        }
    }

    public static void resetBank() {
        bank = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    }

    public static void endTurn() throws IOException {
        iterator += 1;
        play(Setup.getPlayers().get(iterator %= 3), currentWedge);
    }

    public static void setLetter(String L) {
        letter = L;
    }

    public static void setIterator(int n) {
        iterator = n;
    }

    public static Thread getThread2() {
        return t2;
    }
    
    public static Thread getThread3() {
        return t3;
    }

    public static String getSolution() {
        return solution;
    }

    public static String[] getBank() {
        return bank;
    }

    public static int getCurrentWedge() {
        return wedges[currentWedge];
    }
}
