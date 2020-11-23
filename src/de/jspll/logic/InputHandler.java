package de.jspll.logic;

import de.jspll.handlers.LogicHandler;
import javax.swing.event.MouseInputListener;
import java.awt.event.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author
 *
 * @version 1.0
 */

public class InputHandler implements MouseInputListener, MouseWheelListener, KeyListener {
    private String[] keyList = new String[]{"1","2","3","4","5","6","7","8","9","0","w","a","s","d","q","e","+","-","ESC", "CTRL", "SHIFT", "ALT", "TAB","CAPS", "ENTER"};
    private LogicHandler parent; // ToDo: Parent Kruse fix
    private AtomicLong wheelMovement = new AtomicLong(Double.doubleToLongBits(0));
    private AtomicBoolean mouse1 = new AtomicBoolean(false);
    private AtomicBoolean mouse2 = new AtomicBoolean(false);
    private AtomicBoolean mouse3 = new AtomicBoolean(false);
    private AtomicBoolean inFocus = new AtomicBoolean(false);
    private AtomicIntegerArray mousePos = new AtomicIntegerArray( new int[]{0,0} );
    private HashMap<String,AtomicBoolean> keyMap = new HashMap<>(100);
    private ConcurrentLinkedQueue<String> typingQueue = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<MouseEvent> clicks = new ConcurrentLinkedQueue<>();

    private void init(){
        for (String s: keyList){
            keyMap.put(s,new AtomicBoolean(false));
        }
    }

    public InputHandler(LogicHandler parent){
        this.parent = parent;
        init();
    }

    //Mouse

    //returns Array in form :
    // [
    //0 String "input",                       //identifyer
    //1 boolean mouse1,                       //true if mouse 1 down
    //2 boolean mouse2,                       //true if mouse 2 down
    //3 boolean mouse3,                       //true if mouse 3 down
    //4 HashMap<String,AtomicBoolean> keyMap, //HashMap for all keys
    //5 int[] mousePos,                       // Array in form of [ int x, int y] pos of Mouse
    //6 double mousewheelMovement,            // Double indicating the amount of wheel movement
    //7 String[] keystrokes,                  // Array containing key presses in order
    //8 MouseEvent[] clicks                   // Array containing all MouseEvents of all Clicks in order
    // ]
    public Object[] getInputInfo(){
        String[] keyStrokes = typingQueue.toArray(new String[typingQueue.size()]);
        MouseEvent[] clicks = this.clicks.toArray(new MouseEvent[this.clicks.size()]);
        double mousewheelMovement = Double.longBitsToDouble(wheelMovement.get());
        typingQueue.clear();
        this.clicks.clear();
        wheelMovement.set(0);
        return new Object[]{"input",mouse1.get(),mouse2.get(),mouse3.get(),keyMap, new int[]{mousePos.get(0),mousePos.get(1)},keyList, mousewheelMovement, keyStrokes, clicks};
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mousePos.set(0,e.getX());
        mousePos.set(1,e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePos.set(0,e.getX());
        mousePos.set(1,e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clicks.add(e);
    }

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
        inFocus.set(true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        inFocus.set(false);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        wheelMovement.set(Double.doubleToLongBits(Double.longBitsToDouble(wheelMovement.get()) + e.getPreciseWheelRotation()));
    }

    //Keyboard

    @Override
    public void keyTyped(KeyEvent e) {
        String key = getKey(e.getKeyCode());
        if(key == null) return;
        typingQueue.add(key);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        AtomicBoolean key = keyMap.get((e.getKeyChar() + "").toLowerCase());
        if( key != null){
            key.set(true);
            return;
        }
        setKey(e.getKeyCode(),true);
    }

    private String getKey(int keyCode){
        for(String s : keyList){
            if(s.contentEquals(((char)keyCode) + "")){
                return s;
            }
        }
        switch (keyCode){
            case KeyEvent.VK_ENTER:
                return "ENTER";
            case KeyEvent.VK_ESCAPE:
                return "ESC";
            case KeyEvent.VK_SHIFT:
                return "SHIFT";
            case KeyEvent.VK_ALT:
                return "ALT";
            case KeyEvent.VK_TAB:
                return "TAB";
            case KeyEvent.VK_CONTROL:
                return "CTRL";
            case KeyEvent.VK_CAPS_LOCK:
                return "CAPS";
            case KeyEvent.VK_MINUS:
                return "-";
            case KeyEvent.VK_PLUS:
                return "+";
            case KeyEvent.VK_0:
                return "0";
            case KeyEvent.VK_1:
                return "1";
            case KeyEvent.VK_2:
                return "2";
            case KeyEvent.VK_3:
                return "3";
            case KeyEvent.VK_4:
                return "4";
            case KeyEvent.VK_5:
                return "5";
            case KeyEvent.VK_6:
                return "6";
            case KeyEvent.VK_7:
                return "7";
            case KeyEvent.VK_8:
                return "8";
            case KeyEvent.VK_9:
                return "9";
        }
        return null;
    }

    private void setKey(int keyCode, boolean val){
        switch (keyCode){
            case KeyEvent.VK_ENTER:
                keyMap.get("ENTER").set(val);
                break;
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
            case KeyEvent.VK_0:
                keyMap.get("0").set(val);
                break;
            case KeyEvent.VK_1:
                keyMap.get("1").set(val);
                break;
            case KeyEvent.VK_2:
                keyMap.get("2").set(val);
                break;
            case KeyEvent.VK_3:
                keyMap.get("3").set(val);
                break;
            case KeyEvent.VK_4:
                keyMap.get("4").set(val);
                break;
            case KeyEvent.VK_5:
                keyMap.get("5").set(val);
                break;
            case KeyEvent.VK_6:
                keyMap.get("6").set(val);
                break;
            case KeyEvent.VK_7:
                keyMap.get("7").set(val);
                break;
            case KeyEvent.VK_8:
                keyMap.get("8").set(val);
                break;
            case KeyEvent.VK_9:
                keyMap.get("9").set(val);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        AtomicBoolean key = keyMap.get((e.getKeyChar() + "").toLowerCase());
        if( key != null){
            key.set(false);
            return;
        }
        setKey(e.getKeyCode(),false);
    }

    public HashMap<String, AtomicBoolean> getKeyMap() {
        return keyMap;
    }

    public AtomicIntegerArray getMousePos() {
        return mousePos;
    }

    public AtomicBoolean getMouse1() {
        return mouse1;
    }

    public AtomicBoolean getMouse2() {
        return mouse2;
    }

    public AtomicBoolean getMouse3() {
        return mouse3;
    }
}
