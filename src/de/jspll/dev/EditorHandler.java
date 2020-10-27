package de.jspll.dev;

import de.jspll.data.*;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.examples.DisplayMover;
import de.jspll.data.objects.examples.MouseFollower;
import de.jspll.handlers.GameObjectHandler;
import de.jspll.handlers.GraphicsHandler;
import de.jspll.handlers.LogicHandler;

import java.awt.*;
import java.awt.Dimension;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static de.jspll.data.ChannelID.COMM1;
import static de.jspll.data.ChannelID.LOGIC;

/**
 * Created by reclinarka on 25-Oct-20.
 */
public class EditorHandler extends GameObject{

    private boolean test = true;

    private GraphicsHandler graphicsHandler;
    private GameObjectHandler gameObjectHandler;
    private LogicHandler logicHandler;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Runnable task;


    public EditorHandler(String ID , String windowTitle, Dimension size) {
        super(ID,"g.dev.EditorHandler");
        graphicsHandler = new GraphicsHandler(windowTitle,size, GraphicsHandler.HandlerMode.DIALOG);
        gameObjectHandler = new GameObjectHandler();
        logicHandler = new LogicHandler(graphicsHandler);

    }

    public void init(){

        logicHandler.setGameObjectHandler(gameObjectHandler);
        graphicsHandler.setGameObjectHandler(gameObjectHandler);
        gameObjectHandler.setGraphicsHandler(graphicsHandler);


        gameObjectHandler.loadObject(new MouseFollower("test"));
        gameObjectHandler.loadObject(new DisplayMover("test1"));
        gameObjectHandler.loadObject(new GameObject("test","g.test.GameObject"));
        //getParent().loadObject(new Repeater("0",frameHandler.getGameObjectHandler()));


    }

    @Override
    public ChannelID[] getChannels() {
        return new ChannelID[]{COMM1,LOGIC};
    }

    @Override
    public char update(float elapsedTime) {
        super.update(elapsedTime);
        if(test){
            init();
            test = false;
            task = () -> {
                logicHandler.execute(elapsedTime);
                graphicsHandler.execute(elapsedTime);
            };
        } else {
            executorService.submit(task);

        }
        return  0;
    }

    @Override
    public char call(Object[] input) {
        return super.call(input);
    }
}
