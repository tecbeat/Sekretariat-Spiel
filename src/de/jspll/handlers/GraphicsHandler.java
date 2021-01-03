package de.jspll.handlers;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.frames.SubHandler;
import de.jspll.graphics.*;
import de.jspll.logic.InputHandler;
import de.jspll.util.ColorStorage;
import java.awt.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;
import static de.jspll.data.ChannelID.*;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Philipp Polland
 *
 * @version 1.0
 */
public class GraphicsHandler implements SubHandler {

    public GraphicsHandler(String windowTitle, Dimension size, HandlerMode mode){
        slate = new Slate(this);
        this.width = size.width;
        this.height = size.height;
        frame = new BufferedImage(size.width,size.height,BufferedImage.TYPE_INT_ARGB);
        frame_graphics = frame.createGraphics();
        frame_graphics.setClip(0,0,size.width,size.height);
        this.mode = mode;
        switch (mode){
/*            case DIALOG:
                dialog = new Secondary_window(windowTitle,slate,size);
                cameras[0] = new Camera(624,377,(int) size.getWidth(),(int) size.getHeight(),2);
                break;*/
            case MAIN:
                this.window = new de.jspll.graphics.Window(windowTitle,slate,size);
                this.windowTitle = windowTitle;
                cameras[0] = new Camera(624,377,slate.getWidth(),slate.getHeight(),2.5f);
                break;
        }
    }

    private HandlerMode mode;
    private String windowTitle;


    //Main mode
    private Slate slate;
    private de.jspll.graphics.Window window;

    //Dialog mode
    private SecondaryWindow dialog;

    //time passed since last drawing call
    private float elapsedTime;
    //keeps track if drawing thread is layerSelection
    private AtomicBoolean active = new AtomicBoolean();
    private GameObjectHandler gameObjectHandler;
    private Camera[] cameras = new Camera[10];
    private BufferedImage frame = null;
    private Graphics2D frame_graphics;
    private int selectedCamera = 0;
    private int width;
    private int height;

    private boolean sizeHasChanged(){
        int jWidth = slate.getWidth();
        int jHeight = slate.getHeight();
        Camera c = getSelectedCamera();
        if(width != jWidth || height != jHeight)
            return true;
        return false;
    }

    private void createFrameToSlateDim(){
        frame = new BufferedImage(slate.getWidth(),slate.getHeight(),BufferedImage.TYPE_INT_ARGB);
    }


    // gets called according to fps target;
    // - elapsedTime is the time in seconds that has passed since the finish of last call
    public void execute(float elapsedTime){
        if(sizeHasChanged()){
            createFrameToSlateDim();
        }
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
        if(g == null || frame == null){
            return;
        }

        frame_graphics.dispose();
        frame_graphics = frame.createGraphics();
        frame_graphics.setClip(0,0,slate.getWidth(),slate.getHeight());
        getSelectedCamera().updateWindowSize(frame_graphics);


        frame_graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        frame_graphics.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
        frame_graphics.setColor(ColorStorage.BACKGROUND_COLOR);

        //fill background
        frame_graphics.fillRect(0,0,slate.getWidth(),slate.getHeight());

        if(gameObjectHandler != null) {
            for(int i = FIRST_LAYER.valueOf(); i <= LAST_LAYER.valueOf(); i++){
                for (GameObject object : gameObjectHandler.getChannel(ChannelID.getbyID(i)).allValues()) {
                    try {
                        object.paint(frame_graphics, elapsedTime, cameras[selectedCamera], ChannelID.getbyID(i));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

        try {
            g.drawImage(frame, 0, 0, frame.getWidth(), frame.getHeight(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (mode){
            case MAIN:
                break;
            case DIALOG:
                break;
        }

        //Everything that needs to be drawn goes here...

        //Signal that frame is finished
        g.dispose();
        active.set(false);
    }

    public Point getMousePos(){
        if(window == null) {
            return null;
        }
        return slate.getMousePosition(true);
    }

    public Camera getSelectedCamera() {
        return cameras[selectedCamera];
    }

    public void addCamera(Camera c){
        int i = 0;
        for(Camera cam : cameras){
            i++;
        }
        cameras[i+1] = c;
    }

    public void switchCamera(Camera c){
        int i = 0;
        for(Camera cam : cameras){
            i++;
            if(cam == c)
                break;
        }
        selectedCamera = i;
    }

    public void firstCamera(){
        selectedCamera = 0;
    }

    public void setInputListener(InputHandler inputHandler){
        slate.addMouseListener(inputHandler);
        slate.addMouseWheelListener(inputHandler);
        slate.addMouseMotionListener(inputHandler);
        switch (mode){
            case MAIN:
                window.addKeyListener(inputHandler);
                break;
            case DIALOG:
                dialog.addKeyListener(inputHandler);
                break;
        }
    }

    public void setGameObjectHandler(GameObjectHandler gameObjectHandler) {
        this.gameObjectHandler = gameObjectHandler;
        window.setIconImage(gameObjectHandler.getResourceHandler().loadImage("/assets/icon.png") );
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

