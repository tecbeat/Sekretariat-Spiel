package de.jspll.logic;

import de.jspll.handlers.LogicHandler;

import javax.swing.event.MouseInputListener;
import java.awt.event.*;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Created by reclinarka on 11-Oct-20.
 */
public class InputHandler implements MouseInputListener, MouseWheelListener, KeyListener {
    //Keys the game will register
    private String[] keyList = new String[]{"w","a","s","d","q","e","+","-","ESC", "CTRL", "SHIFT", "ALT", "TAB","CAPS"};
    private LogicHandler parent;
    //variables for mouse presses
    private AtomicBoolean mouse1 = new AtomicBoolean(false);
    private AtomicBoolean mouse2 = new AtomicBoolean(false);
    private AtomicBoolean mouse3 = new AtomicBoolean(false);
    private AtomicIntegerArray mousePos = new AtomicIntegerArray( new int[]{0,0} );
    private HashMap<String,AtomicBoolean> keyMap = new HashMap<>(100);

    /**
     * fill the keyMap HashMap with all Keys the game should register
     */
    private void init(){
        for (String s: keyList){
            keyMap.put(s,new AtomicBoolean(false));
        }
    }

    /**
     * @param parent set input handler
     */
    public InputHandler(LogicHandler parent){
        this.parent = parent;
        init();
    }


    //Mouse

    /**
     * @return current user input
     */
    public Object[] getInputInfo(){
        return new Object[]{"input",mouse1.get(),mouse2.get(),mouse3.get(),keyMap, new int[]{mousePos.get(0),mousePos.get(1)},keyList};
    }

    /**
     * Updates the stored mouse position, when the user moves the mouse
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        mousePos.set(0,e.getX());
        mousePos.set(1,e.getY());

    }

    /**
     * Updates the stored mouse position, when the user moves the mouse
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        mousePos.set(0,e.getX());
        mousePos.set(1,e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Register and store mouse presses
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case 1://leftclick
                mouse1.set(true);
                mousePos.set(0,e.getX());
                mousePos.set(1,e.getY());
                break;
            case 2://middleclick
                mouse2.set(true);
                mousePos.set(0,e.getX());
                mousePos.set(1,e.getY());
                break;
            case 3://rightclick
                mouse3.set(true);
                mousePos.set(0,e.getX());
                mousePos.set(1,e.getY());
                break;
        }
    }

    /**
     * Register and store mouse releases
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case 1://leftclick
                mouse1.set(false);
                mousePos.set(0,e.getX());
                mousePos.set(1,e.getY());
                break;
            case 2://middleclick
                mouse2.set(false);
                mousePos.set(0,e.getX());
                mousePos.set(1,e.getY());
                break;
            case 3://rightclick
                mouse3.set(false);
                mousePos.set(0,e.getX());
                mousePos.set(1,e.getY());
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

    /**
     * Register and store key presses
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
        AtomicBoolean key = keyMap.get((e.getKeyChar() + "").toLowerCase());
        if( key != null){
            key.set(true);
        }
        setKey(e.getKeyCode(),true);
    }

    /**
     * Update keyMap values for keys not represented by their name (not a,b,c etc.)
     * @param keyCode
     * @param val
     */
    private void setKey(int keyCode, boolean val){
        switch (keyCode){
            case KeyEvent.VK_ESCAPE:
                keyMap.get("ESC").set(val);
                break;
            case KeyEvent.VK_SHIFT:
                keyMap.get("SHIFT").set(val);
                break;
            case KeyEvent.VK_ALT:
                keyMap.get("ALT").set(val);
                break;
            case KeyEvent.VK_TAB:
                keyMap.get("TAB").set(val);
                break;
            case KeyEvent.VK_CONTROL:
                keyMap.get("CTRL").set(val);
                break;
            case KeyEvent.VK_CAPS_LOCK:
                keyMap.get("CAPS").set(val);
                break;
            case KeyEvent.VK_MINUS:
                keyMap.get("-").set(val);
                break;
            case KeyEvent.VK_PLUS:
                keyMap.get("+").set(val);
                break;
        }
    }

    /**
     * Register and store key releases
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {

        AtomicBoolean key = keyMap.get((e.getKeyChar() + "").toLowerCase());
        if( key != null){
            key.set(false);
        }
        setKey(e.getKeyCode(),false);
    }
}
