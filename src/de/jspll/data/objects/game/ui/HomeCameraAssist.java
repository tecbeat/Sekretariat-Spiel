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
    private Camera cam;
    Dimension windowDim;

    public HomeCameraAssist(int mw, int mh, float z){
        super("hca", "homeCameraAssist", 0, 0, new Dimension(0,0));

        mapHeight = mh;
        mapWidth = mw;
        zoom = z;

        channels = new ChannelID[]{ChannelID.UI};
    }


    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        //getParent().getGraphicsHandler().addCamera(cam);
        //getParent().getGraphicsHandler().switchCamera(cam);
        //System.out.println("w: " + mapWidth + " h: " + mapHeight + " z: " + zoom);

        windowDim = getParent().getGraphicsHandler().getWindow().getSize();

        //System.out.println(windowDim);

        System.out.println(((float)windowDim.width / (float)mapWidth));
        zoom = Math.max(((float)windowDim.width / (float)mapWidth), ((float)windowDim.height / (float)mapHeight));
        //System.out.println("new Zoom: " + zoom);

        int posX = Math.min(camera.getWidth() / 2, (int)(mapWidth/2 * zoom));
        int posY = Math.min(camera.getHeight() / 2, (int)(mapHeight/2 * zoom));

        camera.instantlyCenterToPos(posX, posY);
        camera.instantlyZoom(zoom);
    }
}
