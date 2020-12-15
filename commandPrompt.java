/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indproject;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author s561561
 */
public class commandPrompt {

    private JFrame winFrame;

    private static JFrame frame;
    private JPanel panel;
    private JPanel middleP;

    private GridBagConstraints c;
    private JPanel titleP;

    private JPanel buttonP;
    private JButton okay;
    private JButton solve;
    private JFrame opF;
    private String op;

    private JTextField letter;
    private JComboBox power;
    private Integer[] powerValues = {5, 6, 7, 8, 9, 10};

    private JLabel title;
    private JLabel letterL;
    private JLabel powerL;

    public commandPrompt(Player P) {
        frame = new JFrame("What will you do?");
        panel = new JPanel();
        panel.setBackground(Color.magenta);

        titleP = new JPanel();
        titleP.setBackground(Color.magenta);
        buttonP = new JPanel();
        middleP = new JPanel(new GridBagLayout());
        middleP.setBackground(Color.magenta);

        c = new GridBagConstraints();
        c.insets = new Insets(1, 1, 1, 1);

        title = new JLabel("<html><font color=yellow>" + P.getName() + "'s Turn!</font>");
        title.setFont(title.getFont().deriveFont(25.0F));
        titleP.add(title);

        letterL = new JLabel("Choose a Letter: ");
        letterL.setFont(letterL.getFont().deriveFont(15.0F));
        powerL = new JLabel("Spin Power: ");
        powerL.setFont(powerL.getFont().deriveFont(15.0F));

        power = new JComboBox(powerValues);
        letter = new JTextField(2);

        okay = new JButton("Okay");
        okay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean flag = true;
                try {
                    letter.getText();
                } catch (NullPointerException n) {
                    flag = false;
                }

                if ("aeiou".contains(letter.getText().toLowerCase()) && letter.getText().length() == 1 && flag) {
                    if (P.getRoundBalance() < 200) {
                        flag = false;
                    }
                } else if (!("bcdfghjklmnpqrstvwxyz".contains(letter.getText().toLowerCase()) && letter.getText().length() == 1) && flag) {
                    flag = false;
                }

                if (flag) {
                    Round.setPower((Integer) power.getSelectedItem());
                    Round.setLetter(letter.getText());
                    frame.setVisible(false);
                    Round.getThread().start();
                }
            }
        });
        buttonP.add(okay);

        solve = new JButton("Solve?");
        solve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //boolean flag = true;
                try {
                    op = JOptionPane.showInputDialog(opF, "Solve the Puzzle: ", "Solve", JOptionPane.INFORMATION_MESSAGE);
                    opF.setVisible(true);
                } catch (NullPointerException n) {
                    //flag = false;
                }

                if (op.equalsIgnoreCase(Round.getSolution())) {
                    Round.clearBoard(P);
                    JOptionPane.showMessageDialog(winFrame, P.getName() + " has won the round!", P.getName() + " wins!", JOptionPane.INFORMATION_MESSAGE);
                    frame.setVisible(false);
                    Round.setFlag(false);
                    
                    GameWindow.resetWheel();
                    Round.resetBank();
                    GameWindow.updateLetters(Round.getBank());
                    
                    if (GameWindow.getCounter() == 0) {
                        GameWindow.setCounter(1);
                        GameWindow.start1();
                    } else if (GameWindow.getCounter() == 1) {
                        GameWindow.setCounter(2);
                        GameWindow.start2();
                    } else {
                        int max = 0;
                        Player winner = new Player();
                        int b1 = Start2.getList().get(0).getBalance();
                        int b2 = Start2.getList().get(1).getBalance();
                        int b3 = Start2.getList().get(2).getBalance();

                        if (b1 > max) {
                            max = b1;
                            winner = Start2.getList().get(0);
                        } 
                        if (b2 > max) {
                            max = b2;
                            winner = Start2.getList().get(1);
                        } 
                        if (b3 > max) {
                            max = b3;
                            winner = Start2.getList().get(2);
                        }

                        if ((b1 == max) && (b2 == max) || (b1 == max) && (b3 == max) || (b2 == max) && (b3 == max)) {
                            JOptionPane.showMessageDialog(winFrame, "The game has ended in a tie. ://", "DRAW", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(winFrame, winner.getName() + " has won the game!", "Congradulations, " + winner.getName(), JOptionPane.INFORMATION_MESSAGE);
                        }
                        frame.setVisible(false);
                        GameWindow.close();
                        new Start1();
                    }
                } else {
                    Round.endTurn();
                }
            }
        });
        buttonP.add(solve);

        c.gridy = 0;
        middleP.add(letterL, c);
        middleP.add(letter, c);

        c.gridy = 1;
        middleP.add(powerL, c);
        middleP.add(power, c);

        panel.add(titleP, BorderLayout.NORTH);
        panel.add(middleP);
        frame.add(buttonP, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setSize(275, 190);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
    }
}
