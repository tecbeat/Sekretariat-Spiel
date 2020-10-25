package de.jspll.handlers;

import de.jspll.data.*;
import de.jspll.data.objects.GameObject;
import de.jspll.frames.SubHandler;
import de.jspll.graphics.Camera;
import de.jspll.logic.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.jspll.data.ChannelID.BACKGROUND;

/**
 * Created by reclinarka on 05-Oct-20.
 */
public class GraphicsHandler implements SubHandler {

    public GraphicsHandler(String windowTitle, Dimension size){
        slate = new Slate(this);
        this.window = new Window(windowTitle,slate,size);
        this.windowTitle = windowTitle;
        cameras[0] = new Camera(0,0,slate.getWidth(),slate.getHeight(),2);
    }

    private String windowTitle;
    private Slate slate;
    private Window window;
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
        active.set(true);
        this.elapsedTime = elapsedTime;
        this.window.repaint();
        while(active.get()){
            //lock thread until drawing has finished
        }
    }



    //actual drawing call, keeps
    public void drawingRoutine(Graphics g){
        //fill background
        g.fillRect(0,0,window.getWidth(),window.getHeight());

        //Everything that needs to be drawn goes here...
        if(gameObjectHandler != null) {
            for (GameObject object : gameObjectHandler.getChannel(BACKGROUND).allValues()) {
                object.paint(g, elapsedTime, cameras[selectedCamera]);
            }
        }

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
        window.addMouseListener(inputHandler);
        window.addMouseWheelListener(inputHandler);
        window.addMouseMotionListener(inputHandler);
        window.addKeyListener(inputHandler);
    }

    public void setGameObjectHandler(GameObjectHandler gameObjectHandler) {
        this.gameObjectHandler = gameObjectHandler;
    }

    public Window getWindow() {
        return window;
    }

    public Slate getSlate() {
        return slate;
    }


}

class Window extends JFrame{




    public Window(String windowTitle, JPanel content, Dimension size) {
        //setting misc. attributes of the window
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

//used for rerouting drawing method
class Slate extends JPanel {
    private GraphicsHandler parent;
    public Slate(GraphicsHandler parent){
        this.parent = parent;
    }
    @Override
    protected void paintComponent(Graphics g) {
        parent.drawingRoutine(g);
    }
}

