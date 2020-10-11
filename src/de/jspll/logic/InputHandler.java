package de.jspll.logic;

import javax.swing.event.MouseInputListener;
import java.awt.event.*;

/**
 * Created by reclinarka on 11-Oct-20.
 */
public class InputHandler implements MouseInputListener, MouseWheelListener, KeyListener {
    private LogicHandler parent;

    public InputHandler(LogicHandler parent){
        this.parent = parent;
    }


    //Mouse

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }



    //Keyboard

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
