package de.jspll.handlers;

import de.jspll.data.objects.GameObject;
import de.jspll.frames.SubHandler;
import de.jspll.logic.InputHandler;
import static de.jspll.data.ChannelID.*;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author
 *
 * @version 1.0
 */

public class LogicHandler implements SubHandler {
    public String ID = "main";

    private InputHandler inputHandler = new InputHandler(this);

    private GameObjectHandler gameObjectHandler;

    private boolean test = true;

    public LogicHandler(GraphicsHandler graphicsHandler){
        graphicsHandler.setInputListener(inputHandler);
    }

    public void execute(float elapsedTime){
        //test
        gameObjectHandler.dispatch(INPUT,inputHandler.getInputInfo());

        if(gameObjectHandler != null) {
            for (GameObject object : gameObjectHandler.getChannel(LOGIC).allValues()) {
                object.update(elapsedTime);
            }
        }
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public void setGameObjectHandler(GameObjectHandler gameObjectHandler) {
        this.gameObjectHandler = gameObjectHandler;
    }
}




