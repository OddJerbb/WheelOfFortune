package wheeloffortune;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class CommandPrompt {

    private static JFrame commandFrame;
    private JFrame solveFrame;
    private JFrame winFrame;

    private JPanel middlePanel;
    private JPanel titlePanel;
    private JPanel bottomPanel;

    private GridBagConstraints c;

    private String solveOP;

    private JTextField letterTXT;
    private JButton choose;
    private JButton spin;
    private JButton solve;
    private JLabel title;
    private JLabel letterL;
    private JLabel extraLabel;

    //First command screen
    public CommandPrompt(Player P) {
        commandFrame = new JFrame("What will you do?");

        titlePanel = new JPanel();
        titlePanel.setBackground(Color.magenta);
        bottomPanel = new JPanel();
        middlePanel = new JPanel(new GridBagLayout());
        middlePanel.setBackground(Color.magenta);

        c = new GridBagConstraints();
        c.insets = new Insets(3, 3, 3, 3);

        title = new JLabel("<html><font color=yellow>" + P.getName() + "'s Turn!</font>");
        title.setFont(title.getFont().deriveFont(27.5F));
        titlePanel.add(title);

        letterL = new JLabel("Choose Vowel? (-$250): ");
        letterL.setFont(letterL.getFont().deriveFont(17.5F));
        letterTXT = new JTextField(2);

        extraLabel = new JLabel("                    or...");
        extraLabel.setFont(extraLabel.getFont().deriveFont(17.5F));

        choose = new JButton("OK");
        choose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean flag = true;
                try {
                    letterTXT.getText();
                } catch (NullPointerException n) {
                    flag = false;
                }

                if ("aeiou".contains(letterTXT.getText().toLowerCase()) && letterTXT.getText().length() == 1 && flag) {
                    if (P.getRoundBalance() < 250) {
                        flag = false;
                    }
                } else {
                    flag = false;
                }

                if (flag) {
                    Round.setLetter(letterTXT.getText());
                    commandFrame.setVisible(false);
                    Round.getThread2().start();
                }
            }
        });

        spin = new JButton("Spin!");
        spin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Round.setLetter("#");
                commandFrame.setVisible(false);
                Round.getThread2().start();
            }
        });

        solve = new JButton("Solve?");
        solve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    solveOP = JOptionPane.showInputDialog(solveFrame, "Solve the Puzzle: ", "Solve", JOptionPane.INFORMATION_MESSAGE);
                    solveFrame.setVisible(true);
                } catch (NullPointerException n) {
                }

                //Is the guess correct?
                if (solveOP != null && solveOP.equalsIgnoreCase(Round.getSolution())) {
                    Round.clearBoard(P);
                    JOptionPane.showMessageDialog(winFrame, P.getName() + " has won the round!", P.getName() + " wins!", JOptionPane.INFORMATION_MESSAGE);
                    commandFrame.setVisible(false);

                    GameWindow.resetWheel();
                    Round.resetBank();
                    GameWindow.updateLetters(Round.getBank());

                    switch (GameWindow.getRoundnum()) {
                        case 0:
                            GameWindow.setRoundnum(1);
                            try {
                                GameWindow.start(0);
                            } catch (IOException ex) {
                            }
                            break;
                        case 1:
                            GameWindow.setRoundnum(2);
                            try {
                                GameWindow.start(1);
                            } catch (IOException ex) {
                            }
                            break;
                        case 2:
                            int maxBalance = 0;
                            int balance1 = Setup.getPlayers().get(0).getTotalBalance();
                            int balance2 = Setup.getPlayers().get(1).getTotalBalance();
                            int balance3 = Setup.getPlayers().get(2).getTotalBalance();
                            Player winner = new Player();
                            //Determining the winner
                            if (balance1 > maxBalance) {
                                maxBalance = balance1;
                                winner = Setup.getPlayers().get(0);
                            }
                            if (balance2 > maxBalance) {
                                maxBalance = balance2;
                                winner = Setup.getPlayers().get(1);
                            }
                            if (balance3 > maxBalance) {
                                maxBalance = balance3;
                                winner = Setup.getPlayers().get(3);
                            }
                            //Testing for a draw
                            if ((balance1 == maxBalance) && (balance2 == maxBalance) || (balance1 == maxBalance) && (balance3 == maxBalance) || (balance2 == maxBalance) && (balance3 == maxBalance)) {
                                JOptionPane.showMessageDialog(winFrame, "The game has ended in a tie. ://", "DRAW", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(winFrame, winner.getName() + " has won the game!", "Congradulations, " + winner.getName(), JOptionPane.INFORMATION_MESSAGE);
                            }
                            break;
                    }
                    commandFrame.setVisible(false);
                    GameWindow.close();
                    new Setup();
                } else {
                    commandFrame.setVisible(false);
                    try {
                        Round.endTurn();
                    } catch (IOException ex) {
                    }
                }
            }
        });
        bottomPanel.add(spin);
        bottomPanel.add(solve);

        c.gridy = 0;
        middlePanel.add(letterL, c);
        middlePanel.add(letterTXT, c);
        middlePanel.add(choose, c);

        c.gridy = 1;
        middlePanel.add(extraLabel, c);

        commandFrame.add(titlePanel, BorderLayout.NORTH);
        commandFrame.add(middlePanel);
        commandFrame.add(bottomPanel, BorderLayout.SOUTH);

        commandFrame.setLocationRelativeTo(null);
        commandFrame.setSize(350, 200);
        commandFrame.setResizable(false);
        commandFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        commandFrame.setVisible(true);
    }

    //Spinning and choosing a consonant
    public CommandPrompt(Player P, int wedge) {
        commandFrame = new JFrame("Choose a consonant!");

        titlePanel = new JPanel();
        titlePanel.setBackground(Color.magenta);
        bottomPanel = new JPanel();
        middlePanel = new JPanel(new GridBagLayout());
        middlePanel.setBackground(Color.magenta);

        title = new JLabel("<html><font color=yellow>" + P.getName() + "'s Turn!</font>");
        title.setFont(title.getFont().deriveFont(27.5F));
        titlePanel.add(title);

        extraLabel = new JLabel("Wedge: $" + Round.getCurrentWedge());
        extraLabel.setFont(extraLabel.getFont().deriveFont(20F));

        letterL = new JLabel("Consonant: ");
        letterTXT = new JTextField(2);

        choose = new JButton("OK");
        choose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean flag = true;
                try {
                    letterTXT.getText();
                } catch (NullPointerException n) {
                    flag = false;
                }

                if ("bcdfghjklmnpqrstvwxyz".contains(letterTXT.getText().toLowerCase()) && letterTXT.getText().length() == 1 && flag) {
                    Round.setLetter(letterTXT.getText());
                    commandFrame.setVisible(false);
                    Round.getThread3().start();
                }
            }
        });
        
        middlePanel.add(extraLabel);
        
        bottomPanel.add(letterL);
        bottomPanel.add(letterTXT);
        bottomPanel.add(choose);
        
        commandFrame.add(titlePanel, BorderLayout.NORTH);
        commandFrame.add(middlePanel);
        commandFrame.add(bottomPanel, BorderLayout.SOUTH);
        
        commandFrame.setLocationRelativeTo(null);
        commandFrame.setSize(350, 200);
        commandFrame.setResizable(false);
        commandFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        commandFrame.setVisible(true);
    }
}
