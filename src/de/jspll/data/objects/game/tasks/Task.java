package de.jspll.data.objects.game.tasks;

import de.jspll.data.ChannelID;
import de.jspll.graphics.Camera;
import java.awt.*;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Samuel Assmann
 *
 * @version 1.0
 */

public interface Task {

    void activate();

    boolean isActive();

    char update(float elapsedTime);

    char call(Object[] input);

    void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage);

    void setHolder(TaskHolder holder);

    void requestTexture();

    boolean isLoaded();

    void loadTextures();

    TaskHolder getHolder();
}
