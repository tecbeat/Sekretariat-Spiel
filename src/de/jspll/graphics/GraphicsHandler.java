package de.jspll.graphics;

import de.jspll.frames.SubHandler;
import de.jspll.logic.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by reclinarka on 05-Oct-20.
 */
public class GraphicsHandler implements SubHandler {

    private String windowTitle = "Sekreteriat";
    private Slate slate = new Slate(this);
    private Window window = new Window(windowTitle,slate);
    //time passed since last drawing call
    private float elapsedTime;
    //keeps track if drawing thread is active
    AtomicBoolean active = new AtomicBoolean();

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


        //Signal that frame is finished
        active.set(false);
    }

    public void setInputListener(InputHandler inputHandler){
        window.addMouseListener(inputHandler);
        window.addMouseWheelListener(inputHandler);
        window.addMouseMotionListener(inputHandler);
        window.addKeyListener(inputHandler);
    }
}

class Window extends JFrame{




    public Window(String windowTitle, JPanel content) {
        //setting misc. attributes of the window
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(1920, 1080));
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