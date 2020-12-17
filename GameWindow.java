package zetaomicron;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GameWindow {
    private static JFrame gameFrame = new JFrame("Wheel of Fortune");
    
    private JPanel titlePanel, middlePanel, leftPanel, puzzlePanel, 
            playersPanel, wheelPanel, bankPanel;
    private static JLabel player1L, player2L, player3L, puzzleL, 
            category, bankL;
    private GridBagConstraints c;
    
    //Objects used to display images on frame
    private static File titleImageF;
    private File markImageF, wheelImageF;
    private JPanel markIconP, wheelIconP;
    private static JLabel titleLabel;
    private JLabel markLabel;
    private static JLabel wheelLabel;
    private static BufferedImage icon;
    private static Image image;
    private static ImageIcon titleIcon;
    private ImageIcon markIcon;
    private static ImageIcon wheelIcon;
    
    private static int roundnum;
    private Thread t1;
    private static Thread t2, t3;
    
    public GameWindow() throws IOException {
        gameFrame.setSize(1200, 690);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Setting title panel
        titlePanel = new JPanel();
        setTitleImage(1);
        titlePanel.add(titleLabel);
        titlePanel.setBackground(Color.white);
        
        //Setting middle panel
        middlePanel = new JPanel(new GridLayout(1, 2));
        setPlayers();
        setLeftPanel();
        setRightPanel();
        
        //Setting bank panel
        setBankPanel();
        
        gameFrame.add(titlePanel, BorderLayout.NORTH);
        gameFrame.add(middlePanel);
        gameFrame.add(bankPanel, BorderLayout.SOUTH);
        gameFrame.setVisible(true);
        
        //Setting up threads, each of which initiates a new Round
        t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    new Round();
                } catch (IOException ex) {
                }
            }
        });
        t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    new Round();
                } catch (IOException ex) {
                }
            }
        });
        t3 = new Thread(new Runnable() {
            public void run() {
                try {
                    new Round();
                } catch (IOException ex) {
                }
            }
        });
        //Start the first Round
        t1.start();
    }
    
    public static void setTitleImage(int roundNum) throws IOException {
        switch (roundNum) {
            case 1:
                titleImageF = new File("zetaomicron/Images/title1.png");
                break;
            case 2:
                titleImageF = new File("zetaomicron/Images/title2.png");
                break;
            default:
                titleImageF = new File("zetaomicron/Images/title3.png");
                break;
        }
        
        titleLabel = new JLabel();
        titleLabel.setSize(550, 100);
        
        icon = ImageIO.read(titleImageF);
        image = icon.getScaledInstance(titleLabel.getWidth(), 
                titleLabel.getHeight(), Image.SCALE_SMOOTH);
        titleIcon = new ImageIcon(image);
        
        titleLabel.setIcon(titleIcon);
        titleLabel.setBackground(Color.WHITE);
    }
    
    public void setPlayers() {
        player1L = new JLabel(Setup.getPlayers().get(0).getName() + ": $" 
                + Setup.getPlayers().get(0).getRoundBalance() + " => $" 
                + Setup.getPlayers().get(0).getTotalBalance());
        player2L = new JLabel(Setup.getPlayers().get(1).getName() + ": $" 
                + Setup.getPlayers().get(1).getRoundBalance() + " => $" 
                + Setup.getPlayers().get(1).getTotalBalance());
        player3L = new JLabel(Setup.getPlayers().get(2).getName() + ": $" 
                + Setup.getPlayers().get(2).getRoundBalance() + " => $" 
                + Setup.getPlayers().get(2).getTotalBalance());

        player1L.setFont(player1L.getFont().deriveFont(30.0f));
        player2L.setFont(player2L.getFont().deriveFont(30.0f));
        player3L.setFont(player3L.getFont().deriveFont(30.0f));
    }
    
    public static void updatePlayer(int x) {
        switch (x) {
            case 0:
                player1L.setText(Setup.getPlayers().get(0).getName() + ": $" + 
                        Setup.getPlayers().get(0).getRoundBalance() + " => $" + 
                        Setup.getPlayers().get(0).getTotalBalance());
                break;
            case 1:
                player2L.setText(Setup.getPlayers().get(1).getName() + ": $" + 
                        Setup.getPlayers().get(1).getRoundBalance() + " => $" + 
                        Setup.getPlayers().get(1).getTotalBalance());
                break;
            case 2:
                player3L.setText(Setup.getPlayers().get(2).getName() + ": $" + 
                        Setup.getPlayers().get(2).getRoundBalance() + " => $" + 
                        Setup.getPlayers().get(2).getTotalBalance());
                break;
            default:
                break;
        }
    }
    
    public static void updateLetters(String[] bank) {
        String b = "";
        for (int x = 0; x < bank.length; x++) {
            b = b.concat(bank[x].toUpperCase() + " ");
        }
        bankL.setText(b);
    }
    
    public void setLeftPanel() {
        leftPanel = new JPanel(new GridLayout(2, 1));
        c = new GridBagConstraints();
        
        //Setting up the panel displaying the puzzle
        puzzlePanel = new JPanel(new GridBagLayout());
        puzzlePanel.setBackground(Color.green);
        puzzleL = new JLabel();
        category = new JLabel();
        category.setFont(category.getFont().deriveFont(25.0F));
        
        c.weightx = 1;
        c.weighty = 1;
        c.gridy = 0;
        puzzlePanel.add(puzzleL, c);
        c.gridy = 1;
        puzzlePanel.add(category, c);
        
        //Setting up the panel displaying player information
        playersPanel = new JPanel(new GridBagLayout());
        playersPanel.setBackground(Color.cyan);
        
        c.gridy = 0;
        playersPanel.add(player1L, c);
        c.gridy = 1;
        playersPanel.add(player2L, c);
        c.gridy = 2;
        playersPanel.add(player3L, c);
        
        leftPanel.add(puzzlePanel);
        leftPanel.add(playersPanel);
        middlePanel.add(leftPanel);
    }
    
    public void setRightPanel() {
        wheelPanel = new JPanel(new GridBagLayout());
        wheelPanel.setBackground(Color.magenta);
        
        c.insets = new Insets(0, 0, 0, 0);
        setMarkImage();
        setWheelImage();
        
        middlePanel.add(wheelPanel);
    }
    
    public void setMarkImage() {
        markImageF = new File("zetaomicron/Images/mark.png");
        markLabel = new JLabel();
        markLabel.setSize(40, 30);

        try {
            icon = ImageIO.read(markImageF);
            image = icon.getScaledInstance(markLabel.getWidth(), markLabel.getHeight(), Image.SCALE_SMOOTH);
            markIcon = new ImageIcon(image);
        } catch (IOException e) {
            System.out.println("Nope");
        }

        markLabel.setIcon(markIcon);

        markIconP = new JPanel();
        markIconP.add(markLabel);
        markIconP.setBackground(Color.magenta);

        c.gridy = 0;
        wheelPanel.add(markIconP, c);
    }
    
    public void setWheelImage() {
        wheelImageF = new File("zetaomicron/Images/wheel.png");
        wheelLabel = new JLabel();
        wheelLabel.setSize(425, 425);

        try {
            icon = ImageIO.read(wheelImageF);
            image = icon.getScaledInstance(wheelLabel.getWidth(), wheelLabel.getHeight(), Image.SCALE_SMOOTH);
            wheelIcon = new ImageIcon(image);
        } catch (IOException e) {
            System.out.println("Nope");
        }

        wheelLabel.setIcon(wheelIcon);
        wheelLabel.setBackground(Color.magenta);

        wheelIconP = new JPanel();
        wheelIconP.add(wheelLabel);
        wheelIconP.setBackground(Color.magenta);

        c.gridy = 1;
        wheelPanel.add(wheelIconP, c);
    }
    
    public static void spinWheel(int angle) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        BufferedImage image = gc.createCompatibleImage(wheelIcon.getIconHeight(), wheelIcon.getIconWidth(), Transparency.TRANSLUCENT);
        Graphics2D g2d = image.createGraphics();
        
        double x = (wheelIcon.getIconHeight()-wheelIcon.getIconWidth())/2.0;
        double y = (wheelIcon.getIconWidth()-wheelIcon.getIconHeight())/2.0;
        
        AffineTransform at = AffineTransform.getTranslateInstance(x,y);
        at.rotate(Math.toRadians(angle), wheelIcon.getIconWidth() / 2, wheelIcon.getIconHeight() / 2);
        g2d.drawImage(wheelIcon.getImage(),at, null);
        g2d.dispose();
        
        wheelIcon = new ImageIcon(image);
        wheelLabel.setIcon(wheelIcon);
    }
    
    public static void resetWheel() {
        wheelIcon = new ImageIcon(image);
        wheelLabel.setIcon(wheelIcon);
    }
    
    public void setBankPanel() {
        bankL = new JLabel("A B C D E F G H I J K L M N O P Q R S T U V W X Y Z");
        bankL.setFont(bankL.getFont().deriveFont(30.0f));

        bankPanel = new JPanel();
        bankPanel.setBackground(Color.white);
        bankPanel.add(bankL);
    }
    
    public static void setCategory(String cat) {
        category.setText(cat);
    }
    
    public static void setPuzzle() {
        puzzleL.setText("<html>Puzzle:<br/>" + Round.getPuzzle() + "</html>");
        puzzleL.setFont(puzzleL.getFont().deriveFont(33.0f));
    }
    
    public static void setRoundnum(int n) {
        roundnum = n;
    }
    
    public static int getRoundnum() {
        return roundnum;
    }
    
    public static void start(int n) throws IOException {
        switch(n) {
            case 0:
                setTitleImage(2);
                t2.start();
                break;
            case 1:
                setTitleImage(3);
                t3.start();
                break;
        }
    }
    
    public static void close() {
        gameFrame.setVisible(false);
    }
} 
