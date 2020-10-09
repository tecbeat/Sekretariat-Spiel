package de.jspll.graphics;

import de.jspll.frames.SubHandler;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by reclinarka on 05-Oct-20.
 */
public class GraphicsHandler implements SubHandler {
    private String windowTitle = "Sekreteriat";
    private Slate slate = new Slate(this);
    private Window window = new Window(windowTitle,slate,this);
    private float elapsedTime;
    AtomicBoolean active = new AtomicBoolean();

    public void execute(float elapsedTime){
        active.set(true);
        this.elapsedTime = elapsedTime;
        this.window.repaint();
        while(active.get()){
            //wait
        }
    }

    double x;

    public void drawingRoutine(Graphics g){

        g.fillRect(0,0,window.getWidth(),window.getHeight());
        g.setColor(new Color(41, 69, 134, 255));
        x += (elapsedTime * 100);
        x = x % window.getWidth();
        g.fillOval((int)x,100,50,50);


        //Signal that frame is finished
        active.set(false);
    }

}

class Window extends JFrame{

    private GraphicsHandler parent;



    public void repaint() {
        super.repaint();
    }

    public Window(String windowTitle, JPanel content, GraphicsHandler parent) {
        this.parent = parent;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(1920, 1080));
        setResizable(true);
        setTitle(windowTitle);
        init(content);


    }

    private void init(JPanel content) {
        //setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 1, 0, 0));

        getContentPane().add(content);
        pack();
        setVisible(true);
    }
}

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