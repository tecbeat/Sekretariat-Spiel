package de.jspll.graphics;

import javax.swing.*;
import java.awt.*;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Philipp Polland
 *
 * @version 1.0
 */
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
        setLayout(new GridLayout(1, 1, 0, 0));

        getContentPane().add(content);
        pack();
        setVisible(true);
    }
}
