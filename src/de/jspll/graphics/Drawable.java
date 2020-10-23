package de.jspll.graphics;

import java.awt.*;

/**
 * Created by reclinarka on 09-Oct-20.
 */
public interface Drawable {
    void paint(Graphics g, float elapsedTime, float zoom);
}
