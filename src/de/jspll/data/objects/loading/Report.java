package de.jspll.data.objects.loading;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.handlers.GameObjectHandler;
import java.util.ArrayList;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @version 1.0
 */
public class Report implements ProgressReporter {
    private float count;
    private float initalCount;
    private float percentage;
    private ChannelID nextScene;
    private ArrayList<GameObject> payload;
    private GameObjectHandler gameObjectHandler;

    public Report(int count, ChannelID nextScene){
        this.initalCount = count * 60;
        this.count = count * 60;
        this.nextScene = nextScene;
    }

    public void setNextScene(ChannelID nextScene) {
        this.nextScene = nextScene;
    }

    public Report(){

    }

    public void setCount(int count){
        this.initalCount = count;
        this.count = count;
    }

    @Override
    public ArrayList<GameObject> getPayload() {
        return payload;
    }

    public void setPayload(ArrayList<GameObject> payload) {
        this.payload = payload;
    }

    @Override
    public ChannelID getNextScene() {
        return nextScene;
    }

    public void update(){
        this.count--;
        percentage = (1-(count/initalCount));

        if(count<=0) {
            percentage = -1;
            if(gameObjectHandler != null) {
                gameObjectHandler.switchScene(nextScene);
                System.out.println("Loaded");
                if(nextScene == ChannelID.SCENE_GAME) {
                    gameObjectHandler.getGameManager().startGame();
                }
            }
        }
    }

    @Override
    public float getProgress() {
        return percentage;
    }

    @Override
    public void setGameObjectHandler(GameObjectHandler goh) {
        gameObjectHandler = goh;
    }
}
