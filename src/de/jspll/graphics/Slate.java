package de.jspll.graphics;

import de.jspll.handlers.GraphicsHandler;
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
public class Slate extends JPanel {
    private GraphicsHandler parent;
    public Slate(GraphicsHandler parent){
        this.parent = parent;
    }
    @Override
    protected void paintComponent(Graphics g) {
        parent.drawingRoutine(g);
    }
}
