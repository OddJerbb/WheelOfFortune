package wheeloffortune;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.util.logging.*;

public class Setup {
    //Components of title screen
    private JFrame titleFrame;
    private JPanel titlePanel;
    private JButton start;
    //Components of setup screen
    private JFrame setupFrame;
    private JPanel setupPanel;
    private JPanel buttonsPanel;
    private JButton okay;
    private JButton back;
    private JTextField name1T, name2T, name3T;
    private JLabel name1L, name2L, name3L;
    private String name1, name2, name3;
    
    //Objects used to display images
    private File titleImage, setupImage;
    private JPanel titleIconP, setupIconP;
    private JLabel titleIconL, setupIconL; //Label which holds the icon, itself
    private BufferedImage icon; //Icon prior to being resized
    private Image icon1; //Icon in the process of resizing
    private ImageIcon titleIcon, setupIcon; //Resized icon
    
    private static ArrayList<Player> players = new ArrayList<>();
    
    //Sets and displays title window
    public Setup() {
        titleFrame = new JFrame("Wheel of Fortune");
        titleFrame.setResizable(false);
        titleFrame.setSize(775, 430);
        titleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setTitleImage(); //Method displays the title image

        start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    setSetup();
                } catch (IOException ex) {
                    Logger.getLogger(Setup.class.getName()).log(Level.SEVERE, null, ex);
                }
                titleFrame.setVisible(false);
            }
        });
        
        titlePanel = new JPanel();
        titlePanel.add(start);
        titlePanel.setBackground(Color.magenta);
        titleFrame.add(titlePanel);
        titleFrame.setVisible(true);
    }
    
    //Sets and displays setup window
    public void setSetup() throws IOException {
        setupFrame = new JFrame("Set your names");
        setupFrame.setSize(225, 250);
        setupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupFrame.setLocationRelativeTo(null);
        setupPanel = new JPanel();
        setupPanel.setBackground(Color.magenta);
        
        setSetupImage();
        
        name1L = new JLabel(" Player 1 Name: ");
        name1L.setFont(name1L.getFont().deriveFont(17.0f));
        name2L = new JLabel(" Player 2 Name: ");
        name2L.setFont(name2L.getFont().deriveFont(17.0f));
        name3L = new JLabel(" Player 3 Name: ");
        name3L.setFont(name3L.getFont().deriveFont(17.0f));
        
        name1T = new JTextField(5);
        name2T = new JTextField(5);
        name3T = new JTextField(5);
        
        back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setupFrame.setVisible(false);
                titleFrame.setVisible(true);
            }
        });
        
        okay = new JButton("Okay");
        okay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkNames();
                Player p1 = new Player(name1, 1);
                Player p2 = new Player(name2, 2);
                Player p3 = new Player(name3, 3);

                players.add(p1);
                players.add(p2);
                players.add(p3);

                setupFrame.setVisible(false);
                
                try {
                    new GameWindow();
                } catch (IOException ex) {
                    Logger.getLogger(Setup.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        setupPanel.add(name1L);
        setupPanel.add(name1T);
        setupPanel.add(name2L);
        setupPanel.add(name2T);
        setupPanel.add(name3L);
        setupPanel.add(name3T);
        
        buttonsPanel = new JPanel();
        buttonsPanel.add(okay);
        buttonsPanel.add(back);
        
        setupFrame.add(setupPanel);
        setupFrame.add(buttonsPanel, BorderLayout.SOUTH);
        setupFrame.setVisible(true);
    }
    
    //Sets title screen image
    public void setTitleImage() {
        titleImage = new File("wheeloffortune/Images/title.png");

        titleIconL = new JLabel();
        titleIconL.setSize(750, 350);

        try {
            icon = ImageIO.read(titleImage);
            icon1 = icon.getScaledInstance(titleIconL.getWidth(), titleIconL.getHeight(), Image.SCALE_SMOOTH);
            titleIcon = new ImageIcon(icon1);
        } catch (IOException e) {
            System.out.println("Nope");
        }

        titleIconL.setIcon(titleIcon);

        titleIconP = new JPanel();
        titleIconP.add(titleIconL);
        titleIconP.setBackground(Color.magenta);

        titleFrame.add(titleIconP, BorderLayout.NORTH);
    }
    
    //Sets up the title image of the setup page
    public void setSetupImage() {
        setupImage = new File("wheeloffortune/Images/setup.png");

        setupIconL = new JLabel();
        setupIconL.setSize(200, 55);

        try {
            icon = ImageIO.read(setupImage);
            icon1 = icon.getScaledInstance(setupIconL.getWidth(), setupIconL.getHeight(), Image.SCALE_SMOOTH);
            setupIcon = new ImageIcon(icon1);
        } catch (IOException e) {
            System.out.println("Nope");
        }

        setupIconL.setIcon(setupIcon);

        setupIconP = new JPanel();
        setupIconP.add(setupIconL);
        setupIconP.setBackground(Color.magenta);

        setupFrame.add(setupIconP, BorderLayout.NORTH);
    }
    
    //Checks if player names are empty. If so, default names are used, instead
    public void checkNames() {
        name1 = name1T.getText();
        name2 = name2T.getText();
        name3 = name3T.getText();

        if (name1.equals("")) 
            name1 = "Player 1";
        if (name2.equals("")) 
            name2 = "Player 2";        
        if (name3.equals("")) 
            name3 = "Player 3";
    }
    
    public static ArrayList<Player> getPlayers() {
        return players;
    }
}
