package de.jspll.data.objects.game.ui;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.graphics.Camera;

import java.awt.*;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Lukas Becker
 *
 * @version 1.0
 */
public class SemiTransparentBackground extends GameObject {
    int posX, posY;
    Dimension dim;

    public SemiTransparentBackground(String ID, String objectID, int x, int y, Dimension dimension) {
        super(ID, objectID);
        this.posX = x;
        this.posY = y;
        this.dim = dimension;
        channels = new ChannelID[]{ChannelID.LOGIC, ChannelID.PLAYER};
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        posX = camera.getCenter()[0] - camera.applyZoom(dim.width / 2);
        posY = camera.getCenter()[1] - camera.applyZoom(dim.height / 2);


        g.setColor(new Color(46, 49, 49,200));
        g.fillRect(posX,posY,camera.applyZoom(dim.width),camera.applyZoom(dim.height));
    }
}
