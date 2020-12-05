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

public class HomeCameraAssist extends GameObject {
    int mapWidth;
    int mapHeight;
    float zoom;

    public HomeCameraAssist(int mw, int mh, float z){

        super("hca", "homeCameraAssist", 0, 0, new Dimension(0,0));

        mapHeight = mh;
        mapWidth = mw;
        zoom = z;

        channels = new ChannelID[]{ChannelID.UI};
    }


    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        camera.instantlyCenterToPos(mapWidth/3, mapHeight/3);
        camera.instantlyZoom(zoom);
    }
}
