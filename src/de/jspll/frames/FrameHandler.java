package de.jspll.frames;

import de.jspll.audio.AudioHandler;
import de.jspll.data.ChannelID;
import de.jspll.handlers.GameObjectHandler;
import de.jspll.handlers.GraphicsHandler;
import de.jspll.handlers.LogicHandler;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Samuel Assmann
 *
 * @version 1.0
 */

public class FrameHandler {

    public FrameHandler(String title, Dimension size) {
        gameObjectHandler = new GameObjectHandler();
        graphicsHandler = new GraphicsHandler(title,size, GraphicsHandler.HandlerMode.MAIN);
        logicHandler = new LogicHandler(graphicsHandler);
        frameStabilizer = new FrameStabilizer(new SubHandler[]{logicHandler,graphicsHandler});
        audioHandler = new AudioHandler();
        graphicsHandler.setGameObjectHandler(gameObjectHandler);
        logicHandler.setGameObjectHandler(gameObjectHandler);
        gameObjectHandler.setGraphicsHandler(graphicsHandler);
        gameObjectHandler.setLogicHandler(logicHandler);
        gameObjectHandler.setAudioHandler(audioHandler);
        //gameObjectHandler.setup();
        gameObjectHandler.switchScene(ChannelID.SCENE_LOADING);

    }

    private boolean running = true;
    private GameObjectHandler gameObjectHandler;

    //Graphics Handler and according frame stabilizer
    private GraphicsHandler graphicsHandler;

    //Logic Handler and according frame stabilizer
    private LogicHandler logicHandler;
    private FrameStabilizer frameStabilizer;

    //Audio
    private AudioHandler audioHandler;

    public void run() {
        //start stabilizers

        frameStabilizer.start();

        //while (running){
            //Keep main thread running
        //}
    }

    public GameObjectHandler getGameObjectHandler() {
        return gameObjectHandler;
    }

    public GraphicsHandler getGraphicsHandler() {
        return graphicsHandler;
    }

    public LogicHandler getLogicHandler() {
        return logicHandler;
    }
}

//tries to keep execution of tasks aligned with fps target
class FrameStabilizer extends Thread {
    private SubHandler[] handlers;
    private int target_fps = 60;
    private boolean DEBUG;
    private long lastFrame = System.currentTimeMillis();
    private AtomicBoolean running = new AtomicBoolean(true);

    public FrameStabilizer(SubHandler[] handlers){
        this.handlers = handlers;
    }
    public void stopStabilizer(){
        running.set(false);
    }

    @Override
    public void run() {
        boolean delayActive = false;
        float delay = 0;
        int currentCell = 0;
        float[] timesTaken = new float[120];
        float elapsedTime = 0f;
        long currTime;

        long timeTaken;
        while (running.get()){
            if(delayActive){
                long delayStart = System.currentTimeMillis();
                System.out.println("FH: Started delay, current time: " + delayStart + "\n Waiting for " + (long) (elapsedTime * 1000) + " Milliseconds");
                long currentTime = System.currentTimeMillis();
                while(currentTime - delayStart < elapsedTime * 1000){
                    currentTime = System.currentTimeMillis();
                    System.out.println("FH: current Time: " + currentTime);
                }
                System.out.println("FH: finished delay");
            }
            //start calculating elapsed time
            currTime = System.currentTimeMillis();
            elapsedTime = ((float)(currTime - lastFrame)) / 1000;
            lastFrame = currTime;

            //start frame routine
            try {
                for(SubHandler handler: handlers)
                    handler.execute(elapsedTime);
            } catch (Exception e){
                e.printStackTrace();
            }

            timeTaken = currTime - System.currentTimeMillis() / 1000;
            timesTaken[currentCell] = timeTaken;
            currentCell++;
            if(currentCell == target_fps){
                float a = 0;
                for(float t: timesTaken){
                    a += t;
                }
                a /= (float) timesTaken.length;
                delay = ( 1f /(float) target_fps ) - a;

                if(delay > 0){
                    delayActive = true;
                } else if(delayActive){
                    delayActive = false;
                }
            }
            currentCell = currentCell % 120;
        }
    }
}
