package de.jspll.graphics;

import de.jspll.data.ChannelID;

import java.awt.*;

/**
 * Created by reclinarka on 09-Oct-20.
 */
public interface Drawable {
    void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage);
}
