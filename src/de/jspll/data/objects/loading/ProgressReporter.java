package de.jspll.data.objects.loading;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.handlers.GameObjectHandler;
import java.util.ArrayList;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author
 *
 * @version 1.0
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
