package de.jspll.graphics;

import de.jspll.data.ChannelID;
import java.awt.*;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author
 *
 * @version 1.0
 */

public interface Drawable {
    void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage);
}
