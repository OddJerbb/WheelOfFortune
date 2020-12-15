/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indproject;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author S561561
 */
//Class opens menu to setup player settings
public class Start2 {

    private JFrame frame;

    private JTextField name1T;
    private JTextField name2T;
    private JTextField name3T;
    private JLabel name1L;
    private JLabel name2L;
    private JLabel name3L;
    private String name1;
    private String name2;
    private String name3;

    private JPanel panel;
    private JPanel pb;

    private JButton back;
    private JButton okay3;

    private File image;
    private JPanel iconP;
    private JLabel iconL; //Label which holds the icon, itself
    private BufferedImage icon; //Icon prior to being resized
    private Image icon1; //Icon in the process of resizing
    private ImageIcon icon2; //Resized icon

    private final Integer[] diff = {1, 2, 3};
    private static ArrayList<Player> players = new ArrayList<>();

    public Start2() {
        setup();
        frame.setSize(215, 230);

        iconL = new JLabel();
        iconL.setSize(175, 50);
        image("indproject/Images/triple.png", iconL);

        name1L = new JLabel(" Player 1 Name: ");
        name2L = new JLabel(" Player 2 Name: ");
        name3L = new JLabel(" Player 3 Name: ");

        name1L.setFont(name1L.getFont().deriveFont(15.0f));
        name2L.setFont(name2L.getFont().deriveFont(15.0f));
        name3L.setFont(name3L.getFont().deriveFont(15.0f));

        panel = new JPanel();
        panel.setBackground(Color.magenta);

        panel.add(name1L);
        panel.add(name1T);

        panel.add(name2L);
        panel.add(name2T);

        panel.add(name3L);
        panel.add(name3T);

        pb = new JPanel();
        pb.add(okay3);
        pb.add(back);

        frame.add(panel);
        frame.add(pb, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    //Method sets up components common to each Start2 constructor
    public void setup() {
        frame = new JFrame("Game Options");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        name1T = new JTextField(5);
        name2T = new JTextField(5);
        name3T = new JTextField(5);

        back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new Start1();
            }
        });

        okay3 = new JButton("Okay");
        okay3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkNames();
                Player p1 = new Player(name1, 1);
                Player p2 = new Player(name2, 2);
                Player p3 = new Player(name3, 3);

                players.add(p1);
                players.add(p2);
                players.add(p3);

                frame.setVisible(false);
                try {
                    new GameWindow(1);
                } catch (IOException ex) {
                    Logger.getLogger(Start2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void image(String title, JLabel iconL) {
        image = new File(title);

        try {
            icon = ImageIO.read(image);
            icon1 = icon.getScaledInstance(iconL.getWidth(), iconL.getHeight(), Image.SCALE_SMOOTH);
            icon2 = new ImageIcon(icon1);
        } catch (IOException e) {
            System.out.println("Nope");
        }

        iconL.setIcon(icon2);

        iconP = new JPanel();
        iconP.add(iconL);
        iconP.setBackground(Color.magenta);

        frame.add(iconP, BorderLayout.NORTH);
    }

    //Checks if player names are empty. If so, default names are used
    public void checkNames() {
        name1 = name1T.getText();
        name2 = name2T.getText();
        name3 = name3T.getText();

        if (name1.equals("")) {
            name1 = "Player 1";
        }
        if (name2.equals("")) {
            name2 = "Player 2";
        }
        if (name3.equals("")) {
            name3 = "Player 3";
        }
    }
    
    //Returns the list of players set up, whether human or bot
    public static ArrayList<Player> getList() {
        return players;
    }
}
