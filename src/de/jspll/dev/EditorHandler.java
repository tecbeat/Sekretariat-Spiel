package de.jspll.dev;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.Repeater;
import de.jspll.data.objects.examples.MouseFollower;
import de.jspll.frames.FrameHandler;
import de.jspll.graphics.Slate;
import de.jspll.handlers.GameObjectHandler;
import de.jspll.handlers.GraphicsHandler;
import de.jspll.handlers.LogicHandler;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static de.jspll.data.ChannelID.COMM1;
import static de.jspll.data.ChannelID.LOGIC;

/**
 * Created by reclinarka on 25-Oct-20.
 */
public class EditorHandler extends GameObject{

    private boolean test = true;

    private GraphicsHandler graphicsHandler = new GraphicsHandler("test",new Dimension(200, 600), GraphicsHandler.HandlerMode.DIALOG);
    private GameObjectHandler gameObjectHandler = new GameObjectHandler();
    private LogicHandler logicHandler = new LogicHandler(graphicsHandler);
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Runnable task;


    public EditorHandler(String ID) {
        super(ID,"g.dev.EditorHandler");

    }

    public void init(){
        gameObjectHandler.loadObject(new MouseFollower("test"));

        logicHandler.setGameObjectHandler(gameObjectHandler);
        graphicsHandler.setGameObjectHandler(gameObjectHandler);

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
            task = () -> graphicsHandler.execute(elapsedTime);
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
