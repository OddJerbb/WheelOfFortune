/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indproject;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author s561561
 */
public class Round {
    private static int cW;

    private static boolean flag;
    private static int iterator; //iterator

    private static Thread t1;
    private static Thread t2;

    private static Integer power;
    private static String letter;
    private static String guesses;

    private static String[] bank;

    private Random rand;
    private File puzzleF;
    private Scanner scan;
    private static String solution;
    private static String puzzle;

    //private static final int[] wedges = {500, 900, 700, 300, 800, 550, 400, 500, 600,
        //350, 500, 900, -1, 650, 500, 700, -2, 800, 500, 450, 500, 300, -1, 5000};
    private static final int[] wedges = {500, 5000, -1, 300, 500, 450, 500, 800, -2, 
        700, 500, 650, -1, 900, 500, 350, 600, 500, 400, 550, 800, 300, 700, 900};

    public Round() throws IOException {
        guesses = "-& ";
        bank = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        choosePuzzle();

        iterator = 0;
        flag = true;
        play(Start2.getList().get(0), 0);
    }

    public static void play(Player P, int currentWedge) throws IOException {
        cW = currentWedge;

        t1 = new Thread(new Runnable() {
            public void run() {
                new commandPrompt(P);
            }
        });

        t2 = new Thread(new Runnable() {
            public void run() {
                //Chosen a vowel
                if ("aeiou".contains(letter.toLowerCase()) && Arrays.asList(bank).contains(letter.toLowerCase()) && flag) {
                    P.addRoundBalance(-200);
                    GameWindow.updatePlayer(iterator);

                    for (int x = 0; x < bank.length; x++) {
                        if (bank[x].equalsIgnoreCase(letter)) {
                            bank[x] = "*";
                        }
                    }
                    GameWindow.updateLetters(bank);

                    //Is part of the solution?
                    if (solution.contains(letter.toUpperCase())) {
                        guesses = guesses.concat(letter.toUpperCase());
                        setPuzzle();

                        try {
                            play(P, currentWedge);
                        } catch (IOException ex) {
                            Logger.getLogger(Round.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } else {
                        try {
                            iterator += 1;
                            play(Start2.getList().get(iterator %= 3), currentWedge);
                        } catch (IOException ex) {
                            Logger.getLogger(Round.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    //Chosen a consonant
                } else if (Arrays.asList(bank).contains(letter.toLowerCase()) && flag) {
                    int newWedge = spin(currentWedge);

                    if (wedges[newWedge] > 0) {
                        for (int x = 0; x < bank.length; x++) {
                            if (bank[x].equalsIgnoreCase(letter)) {
                                bank[x] = "*";
                            }
                        }
                        GameWindow.updateLetters(bank);

                        if (solution.contains(letter.toUpperCase()) && !(guesses.contains(letter.toLowerCase()))) {
                            guesses = guesses.concat(letter.toUpperCase());
                            setPuzzle();
                            P.addRoundBalance(wedges[newWedge]);
                            GameWindow.updatePlayer(iterator);
                            try {
                                play(P, newWedge);
                            } catch (IOException ex) {
                                Logger.getLogger(Round.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else {
                            try {
                                iterator += 1;
                                play(Start2.getList().get(iterator %= 3), newWedge);
                            } catch (IOException ex) {
                                Logger.getLogger(Round.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else if (wedges[newWedge] == -1) {
                        P.addRoundBalance(-(P.getRoundBalance()));
                        GameWindow.updatePlayer(iterator);
                        try {
                            iterator += 1;
                            play(Start2.getList().get(iterator %= 3), newWedge);
                        } catch (IOException ex) {
                            Logger.getLogger(Round.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (wedges[newWedge] == -2) {
                        try {
                            iterator += 1;
                            play(Start2.getList().get(iterator %= 3), newWedge);
                        } catch (IOException ex) {
                            Logger.getLogger(Round.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else if (flag) {
                    try {
                        iterator += 1;
                        play(Start2.getList().get(iterator %= 3), currentWedge);
                    } catch (IOException ex) {
                        Logger.getLogger(Round.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        t1.start();
    }

    public static int spin(int wedge) {
        int newWedge = (int) (wedge + power * .233 * 24);
        
        int change = Math.abs(newWedge - wedge);
        
        while (newWedge >= 24) {
            newWedge = newWedge - 24;
        }
        
        GameWindow.spinWheel(15 * change);
        return newWedge;
    }

    public void choosePuzzle() throws IOException {
        rand = new Random();

        switch (rand.nextInt(5)) {
            case 0:
                puzzleF = new File("indproject/Puzzles/Around The House.txt");
                break;
            case 1:
                puzzleF = new File("indproject/Puzzles/Before and After.txt");
                break;
            case 2:
                puzzleF = new File("indproject/Puzzles/Characters.txt");
                break;
            case 3:
                puzzleF = new File("indproject/Puzzles/Food And Drink.txt");
                break;
            case 4:
                puzzleF = new File("indproject/Puzzles/Phrase.txt");
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
                puzzle = puzzle.concat(Character.toString('*'));
            }
        }
        GameWindow.setPuzzle();
    }

    public static void clearBoard(Player p) {
        if (p.getRoundBalance() < 500) {
            p.addRoundBalance(500 - p.getRoundBalance());
            p.addBalance(p.getRoundBalance());
            p.addRoundBalance(-(p.getRoundBalance()));
        } else {
            p.addBalance(p.getRoundBalance());
            p.addRoundBalance(-(p.getRoundBalance()));
        }

        if (p.getNum() == 1) {
            Start2.getList().get(1).addRoundBalance(-(Start2.getList().get(1).getRoundBalance()));
            Start2.getList().get(2).addRoundBalance(-(Start2.getList().get(2).getRoundBalance()));
        } else if (p.getNum() == 2) {
            Start2.getList().get(0).addRoundBalance(-(Start2.getList().get(0).getRoundBalance()));
            Start2.getList().get(2).addRoundBalance(-(Start2.getList().get(2).getRoundBalance()));
        } else {
            Start2.getList().get(0).addRoundBalance(-(Start2.getList().get(0).getRoundBalance()));
            Start2.getList().get(1).addRoundBalance(-(Start2.getList().get(1).getRoundBalance()));
        }

        GameWindow.updatePlayer(0);
        GameWindow.updatePlayer(1);
        GameWindow.updatePlayer(2);
    }
    
    public static void resetBank() {
        bank = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    }
    
    public static String[] getBank() {
        return bank;
    }
    
    public static String getPuzzle() {
        return puzzle;
    }

    public static String getSolution() {
        return solution;
    }

    public static void setPower(Integer i) {
        power = i;
    }

    public static void setLetter(String s) {
        letter = s;
    }

    public static Thread getThread() {
        return t2;
    }

    public static void setFlag(boolean b) {
        flag = b;
    }

    public static void endTurn() {
        try {
            iterator += 1;
            play(Start2.getList().get(iterator %= 3), cW);
        } catch (IOException ex) {
            Logger.getLogger(Round.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
