package de.jspll.data.objects.loading;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.handlers.GameObjectHandler;

import java.util.ArrayList;

/**
 * Created by reclinarka on 05-Nov-20.
 */
public interface ProgressReporter {
    float getProgress();
    void update();
    void setCount(int count);
    void setNextScene(ChannelID scene);
    void setGameObjectHandler(GameObjectHandler goh);
    void setPayload(ArrayList<GameObject> payload);
    ArrayList<GameObject> getPayload();
    ChannelID getNextScene();
}
