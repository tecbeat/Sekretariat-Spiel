package de.jspll.graphics;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    public Window(String windowTitle, JPanel content, Dimension size) {
        //setting misc. attributes of the window
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(size);
        setResizable(false);
        setTitle(windowTitle);
        init(content);
    }

    private void init(JPanel content) {
        //setLocationRelativeTo(null);

        //I wrote this like 6 years ago, I have no clue what it does. I only know that I needed it...
        setLayout(new GridLayout(1, 1, 0, 0));

        getContentPane().add(content);
        pack();
        setVisible(true);
    }
}
