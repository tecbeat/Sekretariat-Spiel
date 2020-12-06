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
 * @author Lukas Becker, Philipp Polland
 * @version 1.0
 */
public class InputHandler implements MouseInputListener, MouseWheelListener, KeyListener {
    private String[] keyList = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "w", "a", "s", "d",
            "q", "e", "p", "r", "+", "-", "ESC", "CTRL", "SHIFT", "ALT", "TAB", "CAPS", "ENTER"};
    private LogicHandler parent;
    private AtomicLong wheelMovement = new AtomicLong(Double.doubleToLongBits(0));
    private AtomicBoolean mouse1 = new AtomicBoolean(false);
    private AtomicBoolean mouse2 = new AtomicBoolean(false);
    private AtomicBoolean mouse3 = new AtomicBoolean(false);
    private AtomicBoolean inFocus = new AtomicBoolean(false);
    private AtomicIntegerArray mousePos = new AtomicIntegerArray(new int[]{0, 0});
    private HashMap<String, AtomicBoolean> keyMap = new HashMap<>(100);
    private ConcurrentLinkedQueue<String> typingQueue = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<MouseEvent> clicks = new ConcurrentLinkedQueue<>();

    /**
     * InputHandler constructor which gets his parent and inits the {@code keyMap}
     *
     * @param parent LogicHandler - handles all input and logic based processes
     */
    public InputHandler(LogicHandler parent) {
        this.parent = parent;
        init();
    }

    /**
     * Fills the Hashmap {@code keyMap} for every String from the {@code keyList} with false
     */
    private void init() {
        for (String s : keyList) {
            keyMap.put(s, new AtomicBoolean(false));
        }
    }

    /**
     * Function reads pressed keys (from {@code typingQueue}), mouse-button-clicks (from {@code clicks}, and mousewheel-movement (from {@code wheelMovement}). <br>
     * The queues getting cleared after reading.
     * <p>
     * the values will be formatted in a specific way:
     *
     * @return Array in form : [<br>
     * 0 String "input",                       - identifier <br>
     * 1 boolean mouse1,                       - true if mouse 1 down <br>
     * 2 boolean mouse2,                       - true if mouse 2 down <br>
     * 3 boolean mouse3,                       - true if mouse 3 down <br>
     * 4 HashMap<String,AtomicBoolean> keyMap, - HashMap for all keys <br>
     * 5 int[] mousePos,                       - Array in form of [ int x, int y] pos of Mouse <br>
     * 6 double mousewheelMovement,            - Double indicating the amount of wheel movement <br>
     * 7 String[] keystrokes,                  - Array containing key presses in order <br>
     * 8 MouseEvent[] clicks                   - Array containing all MouseEvents of all Clicks in order <br>
     * ]
     */
    public Object[] getInputInfo() {
        String[] keyStrokes = typingQueue.toArray(new String[typingQueue.size()]);
        MouseEvent[] clicks = this.clicks.toArray(new MouseEvent[this.clicks.size()]);
        double mousewheelMovement = Double.longBitsToDouble(wheelMovement.get());
        typingQueue.clear();
        this.clicks.clear();
        wheelMovement.set(0);
        return new Object[]{"input", mouse1.get(), mouse2.get(), mouse3.get(), keyMap,
                new int[]{mousePos.get(0), mousePos.get(1)}, keyList, mousewheelMovement, keyStrokes, clicks};
    }

    /**
     * Implements the mouseDragged function from the interface {@code MouseInputListener }
     *
     * @param e MouseEvents
     * @see MouseMotionListener
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        mousePos.set(0, e.getX());
        mousePos.set(1, e.getY());
    }

    /**
     * Implements the mouseMoved function from the interface {@code MouseInputListener}. <br>
     * Sets {@code mousePos} to the current mouse-position.
     *
     * @param e MouseEvents
     * @see MouseMotionListener
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        mousePos.set(0, e.getX());
        mousePos.set(1, e.getY());
    }

    /**
     * Implements the mouseClicked function from the interface {@code MouseListener}. <br>
     * Add the clicked (pressed and released) mouse-button to the {@code clicks} queue.
     *
     * @param e MouseEvents
     * @see MouseListener
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        clicks.add(e);
    }

    /**
     * Implements the mousePressed function from the interface {@code MouseListener}. <br>
     * Set the corresponding mouse-button boolean to true after being pressed.
     *
     * @param e MouseEvents
     * @see MouseListener
     */
    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case 1://leftclick
                mouse1.set(true);
                mousePos.set(0, e.getX());
                mousePos.set(1, e.getY());
                break;
            case 2://middleclick
                mouse2.set(true);
                mousePos.set(0, e.getX());
                mousePos.set(1, e.getY());
                break;
            case 3://rightclick
                mouse3.set(true);
                mousePos.set(0, e.getX());
                mousePos.set(1, e.getY());
                break;
        }
    }

    /**
     * Implements the mouseReleased function from the interface {@code MouseListener}. <br>
     * Set the corresponding mouse-button boolean to false after being released.
     *
     * @param e MouseEvents
     * @see MouseListener
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case 1://leftclick
                mouse1.set(false);
                mousePos.set(0, e.getX());
                mousePos.set(1, e.getY());
                break;
            case 2://middleclick
                mouse2.set(false);
                mousePos.set(0, e.getX());
                mousePos.set(1, e.getY());
                break;
            case 3://rightclick
                mouse3.set(false);
                mousePos.set(0, e.getX());
                mousePos.set(1, e.getY());
                break;
        }
    }

    /**
     * Implements the mouseEntered function from the interface {@code MouseListener}. <br>
     * Sets {@code inFocus} to true when mouse enters a component.
     *
     * @param e MouseEvents
     * @see MouseListener
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        inFocus.set(true);
    }

    /**
     * Implements the mouseExited function from the interface {@code MouseListener}. <br>
     * Sets {@code inFocus} to false when mouse exits a component.
     *
     * @param e MouseEvents
     * @see MouseListener
     */
    @Override
    public void mouseExited(MouseEvent e) {
        inFocus.set(false);
    }

    /**
     * Implements the mouseWheelMoved function from the interface {@code MouseWheelListener}. <br>
     * After the mouse wheel is moved the rotation indicated by a double
     *
     * @param e MouseWheelEvent
     * @see MouseWheelListener
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        wheelMovement.set(Double.doubleToLongBits(Double.longBitsToDouble(wheelMovement.get()) + e.getPreciseWheelRotation()));
    }

    /**
     * Implements the keyTyped function from the interface {@code KeyListener}. <br>
     * Adds the typed key to the {@code typingQueue} if its used in the program {@see keyList}
     *
     * @param e KeyEvent
     * @see KeyListener
     */
    @Override
    public void keyTyped(KeyEvent e) {
        String key = getKey(e.getKeyCode());
        if (key == null) return;
        typingQueue.add(key);
    }

    /**
     * Implements the keyPressed function from the interface {@code KeyListener}. <br>
     * Sets the AtomicBoolean from the pressed Key in the {@code keyMap} to true.
     *
     * @param e KeyEvent
     * @see KeyListener
     */
    @Override
    public void keyPressed(KeyEvent e) {
        //try converting the pressed key to a string (useful for trivial keys, e.g. "1", "2", ..., "a", "b",... )
        AtomicBoolean key = keyMap.get((e.getKeyChar() + "").toLowerCase());
        if (key != null) {
            key.set(true);
            return;
        }
        //alternative, if the pressed Key is not trivial (e.g. "SHIFT", "ESC", ...)
        setKey(e.getKeyCode(), true);
    }

    /**
     * Implements the keyReleased function from the interface {@code KeyListener}. <br>
     * Sets the AtomicBoolean from the pressed Key in the {@code keyMap} to false.
     *
     * @param e KeyEvent
     * @see KeyListener
     */
    @Override
    public void keyReleased(KeyEvent e) {
        //try converting the pressed key to a string (useful for trivial keys, e.g. "1", "2", ..., "a", "b",... )
        AtomicBoolean key = keyMap.get((e.getKeyChar() + "").toLowerCase());
        if (key != null) {
            key.set(false);
            return;
        }
        //alternative, if the pressed Key is not trivial (e.g. "SHIFT", "ESC", ...)
        setKey(e.getKeyCode(), false);
    }

    /**
     * Function which determines which key was pressed and returns it as a string
     *
     * @param keyCode int from keyCode
     * @return name of the pressed key as a String
     * @see KeyEvent
     */
    //TODO check if really necessary
    private String getKey(int keyCode) {
        for (String s : keyList) {
            if (s.contentEquals(((char) keyCode) + "")) {
                return s;
            }
        }
        switch (keyCode) {
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

    /**
     * Sets the given value to the corresponding key in the {@code keyMap}.
     * Key is determined by the {@code keyCode}.
     *
     * @param keyCode int from keyCode
     * @param val     the value to set
     * @see KeyEvent
     */

    private void setKey(int keyCode, boolean val) {
        switch (keyCode) {
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
