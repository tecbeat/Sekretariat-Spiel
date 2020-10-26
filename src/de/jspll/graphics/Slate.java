package de.jspll.graphics;

import de.jspll.handlers.GraphicsHandler;

import javax.swing.*;
import java.awt.*;

//used for rerouting drawing method
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
