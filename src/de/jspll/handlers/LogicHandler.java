package de.jspll.handlers;

import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.examples.DisplayMover;
import de.jspll.data.objects.examples.MouseFollower;
import de.jspll.frames.SubHandler;
import de.jspll.logic.InputHandler;

import java.util.ArrayList;

import static de.jspll.data.ChannelID.INPUT;
import static de.jspll.data.ChannelID.LOGIC;

/**
 * Created by reclinarka on 05-Oct-20.
 */
public class LogicHandler implements SubHandler {
    public String ID = "main";

    private InputHandler inputHandler = new InputHandler(this);

    private GameObjectHandler gameObjectHandler;

    private boolean test = true;


    public LogicHandler(GraphicsHandler graphicsHandler){
        graphicsHandler.setInputListener(inputHandler);
    }

    /**
     * Calls all objects in the input and logic channels
     * @param elapsedTime
     */
    public void execute(float elapsedTime){


        gameObjectHandler.dispatch(INPUT,inputHandler.getInputInfo());

        if(gameObjectHandler != null) {
            for (GameObject object : gameObjectHandler.getChannel(LOGIC).allValues()) {
                object.update(elapsedTime);
            }
        };

    }


    /** Set game object handler
     * @param gameObjectHandler
     */
    public void setGameObjectHandler(GameObjectHandler gameObjectHandler) {
        this.gameObjectHandler = gameObjectHandler;
    }
}




