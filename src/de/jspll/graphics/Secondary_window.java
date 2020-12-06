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

public class Secondary_window extends JDialog
{
    public Secondary_window(String windowTitle, JPanel content, Dimension size) {
        //setting misc. attributes of the window
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        getContentPane().setPreferredSize(size);
        setResizable(true);
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
