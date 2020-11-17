package de.jspll.data;

import de.jspll.data.objects.loading.ProgressReporter;
import de.jspll.handlers.JSONSupport;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by reclinarka on 17-Nov-20.
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
    public ChannelID getNextScene() {
        return targetScene;
    }

    @Override
    public void run() {

    }
}
