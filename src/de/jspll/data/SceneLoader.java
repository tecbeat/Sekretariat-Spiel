package de.jspll.data;

import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.loading.ProgressReporter;
import de.jspll.handlers.GameObjectHandler;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author
 *
 * @version 1.0
 */

public class SceneLoader extends Thread implements ProgressReporter{
    private AtomicInteger objectTotal = new AtomicInteger(),
            texturesTotal = new AtomicInteger(),
            objectsLoaded = new AtomicInteger(),
            texturesLoaded = new AtomicInteger();


    private ChannelID targetScene;

    public SceneLoader(ChannelID targetScene, String json){


    }

    @Override
    public float getProgress() {
        return   (objectsLoaded.get() + texturesLoaded.get()) / (objectTotal.get() + texturesTotal.get());
    }

    @Override
    public void update() {

    }

    @Override
    public void setCount(int count) {

    }

    @Override
    public void setNextScene(ChannelID scene) {

    }

    @Override
    public void setPayload(ArrayList<GameObject> payload) {

    }

    @Override
    public ArrayList<GameObject> getPayload() {
        return null;
    }

    @Override
    public ChannelID getNextScene() {
        return targetScene;
    }

    @Override
    public void run() {

    }

    @Override
    public void setGameObjectHandler(GameObjectHandler goh) {

    }
}
