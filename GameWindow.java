/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indproject;

import java.awt.BorderLayout;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author S561561
 */
public class GameWindow {

    private GridBagConstraints c;
    private static ArrayList<Player> list = Start2.getList();

    private static JLabel player1L;
    private static JLabel player2L;
    private static JLabel player3L;

    private static JLabel puzzleL;
    private static JLabel category;

    private final static JFrame frame = new JFrame("Wheel of Fortune");

    private JPanel titleP;
    private static JLabel title;

    private JPanel middleP;

    private JPanel leftP;
    private JPanel puzzleP;
    private JPanel playersP;

    private JPanel wheelP;

    private static JLabel lettersL;
    private JPanel lettersP;

    private File markImage;
    private JPanel iconP;
    private JLabel iconL; //Label which holds the icon, itself
    private BufferedImage icon; //Icon prior to being resized
    private Image icon1; //Icon in the process of resizing
    private ImageIcon icon2; //Resized icon

    private File wheelImage;
    private JPanel iconP2;
    private static JLabel iconL2;
    private BufferedImage icon3;
    private static Image icon4;
    private static ImageIcon icon5;

    private static int counter;
    private Thread t1;
    private static Thread t2;
    private static Thread t3;

    public GameWindow(int x) throws IOException {
        frame.setSize(900, 485);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        middleP = new JPanel(new GridLayout(1, 2));

        setTitle(1);
        setPlayers();

        top();
        left();
        right();
        bottom();

        frame.add(titleP, BorderLayout.NORTH);
        frame.add(middleP);
        frame.add(lettersP, BorderLayout.SOUTH);

        frame.setVisible(true);

        int counter = 0;
        t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    new Round();
                } catch (IOException ex) {
                    Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    new Round();
                } catch (IOException ex) {
                    Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t3 = new Thread(new Runnable() {
            public void run() {
                try {
                    new Round();
                } catch (IOException ex) {
                    Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        t1.start();
    }

    public void top() {
        titleP = new JPanel();
        titleP.add(title);
        titleP.setBackground(Color.yellow);
    }

    public void left() {
        c = new GridBagConstraints();

        leftP = new JPanel(new GridLayout(2, 1));

        puzzleP = new JPanel(new GridBagLayout());
        puzzleP.setBackground(Color.green);
        puzzleL = new JLabel();
        category = new JLabel();
        category.setFont(category.getFont().deriveFont(15.0F));

        c.weightx = 1;
        c.weighty = 1;
        c.gridy = 0;
        puzzleP.add(puzzleL, c);
        c.gridy = 1;
        puzzleP.add(category, c);

        playersP = new JPanel(new GridBagLayout());
        playersP.setBackground(Color.cyan);

        c.gridy = 0;
        playersP.add(player1L, c);
        c.gridy = 1;
        playersP.add(player2L, c);
        c.gridy = 2;
        playersP.add(player3L, c);

        leftP.add(puzzleP);
        leftP.add(playersP);
        middleP.add(leftP);
    }

    public void right() {
        wheelP = new JPanel(new GridBagLayout());
        wheelP.setBackground(Color.magenta);

        c.insets = new Insets(0, 0, 0, 0);
        markImage();
        wheelImage();

        middleP.add(wheelP);
    }

    public void bottom() {
        lettersL = new JLabel("A B C D E F G H I J K L M N O P Q R S T U V W X Y Z");
        lettersL.setFont(lettersL.getFont().deriveFont(20.0f));

        lettersP = new JPanel();
        lettersP.setBackground(Color.white);
        lettersP.add(lettersL);
    }

    public static void setTitle(int x) {
        if (x == 1) {
            title = new JLabel("Round One");
        } else if (x == 2) {
            title.setText("Round Two");
        } else {
            title.setText("Final Round");
        }
        title.setFont(title.getFont().deriveFont(30.0f));
    }

    public static void setPuzzle() {
        puzzleL.setText("<html>Puzzle:<br/>" + Round.getPuzzle() + "</html>");
        puzzleL.setFont(puzzleL.getFont().deriveFont(25.0f));
    }

    public void setPlayers() {
        player1L = new JLabel(list.get(0).getName() + ": $" + list.get(0).getRoundBalance() + " => $" + list.get(0).getBalance());
        player2L = new JLabel(list.get(1).getName() + ": $" + list.get(1).getRoundBalance() + " => $" + list.get(1).getBalance());
        player3L = new JLabel(list.get(2).getName() + ": $" + list.get(2).getRoundBalance() + " => $" + list.get(2).getBalance());

        player1L.setFont(player1L.getFont().deriveFont(20.0f));
        player2L.setFont(player2L.getFont().deriveFont(20.0f));
        player3L.setFont(player3L.getFont().deriveFont(20.0f));
    }

    public static void updatePlayer(int x) {
        switch (x) {
            case 0:
                player1L.setText(list.get(0).getName() + ": $" + list.get(0).getRoundBalance() + " => $" + list.get(0).getBalance());
                break;
            case 1:
                player2L.setText(list.get(1).getName() + ": $" + list.get(1).getRoundBalance() + " => $" + list.get(1).getBalance());
                break;
            case 2:
                player3L.setText(list.get(2).getName() + ": $" + list.get(2).getRoundBalance() + " => $" + list.get(2).getBalance());
                break;
            default:
                break;
        }
        frame.revalidate();
    }

    public static void updateLetters(String[] bank) {
        String b = "";
        for (int x = 0; x < bank.length; x++) {
            b = b.concat(bank[x].toUpperCase() + " ");
        }
        lettersL.setText(b);
    }

    public void markImage() {
        markImage = new File("indproject/Images/mark.png");
        iconL = new JLabel();
        iconL.setSize(25, 20);

        try {
            icon = ImageIO.read(markImage);
            icon1 = icon.getScaledInstance(iconL.getWidth(), iconL.getHeight(), Image.SCALE_SMOOTH);
            icon2 = new ImageIcon(icon1);
        } catch (IOException e) {
            System.out.println("Nope");
        }

        iconL.setIcon(icon2);

        iconP = new JPanel();
        iconP.add(iconL);
        iconP.setBackground(Color.magenta);

        c.gridy = 0;
        wheelP.add(iconP, c);
    }

    public void wheelImage() {
        wheelImage = new File("indproject/images/wheel.png");
        iconL2 = new JLabel();
        iconL2.setSize(325, 325);

        try {
            icon3 = ImageIO.read(wheelImage);
            icon4 = icon3.getScaledInstance(iconL2.getWidth(), iconL2.getHeight(), Image.SCALE_SMOOTH);
            icon5 = new ImageIcon(icon4);
        } catch (IOException e) {
            System.out.println("Nope");
        }

        iconL2.setIcon(icon5);
        iconL2.setBackground(Color.magenta);

        iconP2 = new JPanel();
        iconP2.add(iconL2);
        iconP2.setBackground(Color.magenta);

        c.gridy = 1;
        wheelP.add(iconP2, c);
    }

    public static void spinWheel(int angle) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        BufferedImage image = gc.createCompatibleImage(icon5.getIconHeight(), icon5.getIconWidth(), Transparency.TRANSLUCENT);
        Graphics2D g2d = image.createGraphics();
        
        double x = (icon5.getIconHeight()-icon5.getIconWidth())/2.0;
        double y = (icon5.getIconWidth()-icon5.getIconHeight())/2.0;
        
        AffineTransform at = AffineTransform.getTranslateInstance(x,y);
        at.rotate(Math.toRadians(angle), icon5.getIconWidth() / 2, icon5.getIconHeight() / 2);
        g2d.drawImage(icon5.getImage(),at, null);
        g2d.dispose();
        
        icon5 = new ImageIcon(image);
        iconL2.setIcon(icon5);
    }
    
    public static void resetWheel() {
        icon5 = new ImageIcon(icon4);
        iconL2.setIcon(icon5);
    }
    
    public static void start1() {
        setTitle(2);
        t2.start();
    }

    public static void start2() {
        setTitle(3);
        t3.start();
    }

    public static void setCounter(int c) {
        counter = c;
    }

    public static int getCounter() {
        return counter;
    }

    public static void close() {
        frame.setVisible(false);
    }

    public static void setCategory(String cat) {
        category.setText(cat);
    }
}
