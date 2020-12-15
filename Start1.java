/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indproject;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.*;
import java.awt.*;
import javax.imageio.ImageIO;

/**
 *
 * @author s561561
 */
public class Start1 {

    private JFrame frame;
    private JPanel panel;
    private JButton start;

    //Objects used to display image
    private File image;
    private JPanel iconP;
    private JLabel iconL; //Label which holds the icon, itself
    private BufferedImage icon; //Icon prior to being resized
    private Image icon1; //Icon in the process of resizing
    private ImageIcon icon2; //Resized icon

    //Class displays title screen and asks for total number of human players
    public Start1() {
        frame = new JFrame("Wheel of Fortune");
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setSize(500, 275);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        image(); //Method displays the title image

        start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Start2();
                frame.setVisible(false);
            }
        });
        
        panel = new JPanel();
        panel.add(start);

        panel.setBackground(Color.magenta);

        frame.add(panel);
        frame.setVisible(true);
    }

    public void image() {
        image = new File("indproject/Images/logo.png");

        iconL = new JLabel();
        iconL.setSize(425, 200);

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
}
