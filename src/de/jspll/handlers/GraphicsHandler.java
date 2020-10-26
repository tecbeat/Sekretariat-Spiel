package de.jspll.handlers;

import de.jspll.data.objects.GameObject;
import de.jspll.frames.SubHandler;
import de.jspll.graphics.*;
import de.jspll.logic.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.jspll.data.ChannelID.BACKGROUND;
import static de.jspll.data.ChannelID.FOREGROUND;

/**
 * Created by reclinarka on 05-Oct-20.
 */
public class GraphicsHandler implements SubHandler {

    public GraphicsHandler(String windowTitle, Dimension size, HandlerMode mode){
        slate = new Slate(this);
        this.mode = mode;
        cameras[0] = new Camera(0,0,slate.getWidth(),slate.getHeight(),2);
        switch (mode){
            case DIALOG:
                dialog = new Secondary_window(windowTitle,slate,size);
                break;
            case MAIN:
                this.window = new de.jspll.graphics.Window(windowTitle,slate,size);
                this.windowTitle = windowTitle;
                break;
        }
    }

    private HandlerMode mode;
    private String windowTitle;

    //Main mode
    private Slate slate;
    private de.jspll.graphics.Window window;

    //Dialog mode
    private Secondary_window dialog;

    //time passed since last drawing call
    private float elapsedTime;
    //keeps track if drawing thread is active
    private AtomicBoolean active = new AtomicBoolean();
    private GameObjectHandler gameObjectHandler;
    private Camera[] cameras = new Camera[10];
    private int selectedCamera = 0;


    //gets called according to fps target;
    // - elapsedTime is the time in seconds that has passed since the finish of last call
    public void execute(float elapsedTime){
        this.elapsedTime = elapsedTime;
        switch (mode){
            case MAIN:
                active.set(true);
                this.window.repaint();
                break;
            case DIALOG:
                active.set(true);
                this.dialog.repaint();
                break;
        }
        while(active.get()){
            //lock thread until drawing has finished
        }
    }



    //actual drawing call, keeps
    public void drawingRoutine(Graphics g){
        if(g == null){
            return;
        }
        //fill background
        g.fillRect(0,0,slate.getWidth(),slate.getHeight());

        if(gameObjectHandler != null) {
            for (GameObject object : gameObjectHandler.getChannel(BACKGROUND).allValues()) {
                object.paint(g, elapsedTime, cameras[selectedCamera]);
            }
            for (GameObject object : gameObjectHandler.getChannel(FOREGROUND).allValues()) {
                object.paint(g, elapsedTime, cameras[selectedCamera]);
            }
        }
        switch (mode){
            case MAIN:
                break;
            case DIALOG:
                break;
        }

        //Everything that needs to be drawn goes here...

        //Signal that frame is finished
        active.set(false);
    }

    public Point getMousePos(){
        if(window == null)
            return null;
        //Point global_mouse = MouseInfo.getPointerInfo().getLocation();
        //return new Point(global_mouse.x - window.getLocation().x,global_mouse.y  - window.getRootPane().getContentPane().getLocation().y);
        return  slate.getMousePosition(true);

    }

    public Camera getSelectedCamera() {
        return cameras[selectedCamera];
    }

    public void setInputListener(InputHandler inputHandler){
        slate.addMouseListener(inputHandler);
        slate.addMouseWheelListener(inputHandler);
        slate.addMouseMotionListener(inputHandler);
        slate.addKeyListener(inputHandler);
    }

    public void setGameObjectHandler(GameObjectHandler gameObjectHandler) {
        this.gameObjectHandler = gameObjectHandler;
    }

    public de.jspll.graphics.Window getWindow() {
        return window;
    }

    public Slate getSlate() {
        return slate;
    }


    public enum HandlerMode{
        MAIN,
        DIALOG
    }

}

class Secondary_window extends JDialog
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
