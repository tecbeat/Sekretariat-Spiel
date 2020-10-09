package de.jspll.frames;

import de.jspll.graphics.GraphicsHandler;
import de.jspll.logic.LogicHandler;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by reclinarka on 05-Oct-20.
 */
public class FrameHandler {

    private boolean running = true;

    private GraphicsHandler graphicsHandler = new GraphicsHandler();
    private FrameStabilizer graphicsStabilizer = new FrameStabilizer(graphicsHandler);

    private LogicHandler logicHandler = new LogicHandler();
    private FrameStabilizer logictSabilizer = new FrameStabilizer(logicHandler);

    public void run() {
        graphicsStabilizer.start();
        while (running){
            //Keep main thread running
        }

    }
}

class FrameStabilizer extends Thread {
    private SubHandler handler;
    int target_fps = 120;
    private boolean DEBUG;
    private long lastFrame = System.currentTimeMillis();
    private AtomicBoolean running = new AtomicBoolean(true);
    public FrameStabilizer(SubHandler handler){
        this.handler = handler;
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
            //System.out.println("FH: Frame routine started");
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
            //System.out.println("FH: current time: " + System.currentTimeMillis());
            elapsedTime = ((float)(currTime - lastFrame)) / 1000;
            lastFrame = currTime;

            //System.out.println("FH: starting drawing with elapsed time: " + elapsedTime);
            //start frame routine
            handler.execute(elapsedTime);
            //System.out.println("FH: finished drawing: " + System.currentTimeMillis());

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