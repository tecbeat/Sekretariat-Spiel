package de.jspll.logic;

import de.jspll.data.ChannelID;
import de.jspll.data.GameObjectHandler;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.examples.MouseFollower;
import de.jspll.frames.SubHandler;
import de.jspll.graphics.GraphicsHandler;
import sun.text.normalizer.Trie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static de.jspll.data.ChannelID.MOUSEUPDATES;

/**
 * Created by reclinarka on 05-Oct-20.
 */
public class LogicHandler implements SubHandler {
    public String ID = "main";

    private InputHandler inputHandler = new InputHandler(this);

    private GameObjectHandler gameObjectHandler;

    boolean test = true;


    public LogicHandler(GraphicsHandler graphicsHandler){
        graphicsHandler.setInputListener(inputHandler);
    }

    public void execute(float elapsedTime){
        //test
        if(test){
            ArrayList<GameObject> objects = new ArrayList<>();
            for(int x = 0; x < 20; x++){
                for(int y = 0; y < 10; y++){
                    objects.add(new GameObject( x + "_" + y,"g.dflt.GameObject",x * 16,y * 32 + (16 * x%2)));
                }
            }
            objects.add(new MouseFollower());
            gameObjectHandler.loadObjects(objects);
            test = false;
        }
        gameObjectHandler.dispatch(MOUSEUPDATES,inputHandler.getMouseInfo());

    }


    public void setGameObjectHandler(GameObjectHandler gameObjectHandler) {
        this.gameObjectHandler = gameObjectHandler;
    }
}




