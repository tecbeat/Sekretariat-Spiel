package de.jspll.logic;

import javax.swing.event.MouseInputListener;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Created by reclinarka on 11-Oct-20.
 */
public class InputHandler implements MouseInputListener, MouseWheelListener, KeyListener {
    private LogicHandler parent;
    private AtomicBoolean mouse1 = new AtomicBoolean(false);
    private AtomicBoolean mouse2 = new AtomicBoolean(false);
    private AtomicBoolean mouse3 = new AtomicBoolean(false);
    private AtomicIntegerArray mousePos = new AtomicIntegerArray( new int[]{0,0} );

    public void init(){

    }

    public InputHandler(LogicHandler parent){
        this.parent = parent;
    }


    //Mouse

    public Object[] getMouseInfo(){
        return new Object[]{"mouse",mouse1.get(),mouse2.get(),mouse3.get(),mousePos.get(0),mousePos.get(1)};
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePos.set(0,e.getX());
        mousePos.set(1,e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case 1://leftclick
                mouse1.set(true);
                break;
            case 2://middleclick
                mouse1.set(true);
                break;
            case 3://rightclick
                mouse1.set(true);
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case 1://leftclick
                mouse1.set(false);
                break;
            case 2://middleclick
                mouse1.set(false);
                break;
            case 3://rightclick
                mouse1.set(false);
                break;
        }
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
