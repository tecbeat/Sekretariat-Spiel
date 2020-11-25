package de.jspll.data.objects.game.ui;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.graphics.Camera;

import java.awt.*;

public class HomeCameraAssist extends GameObject {
    int mapWidth;
    int mapHeight;
    float zoom;

    public HomeCameraAssist(int mw, int mh, float z){

        super("hca", "homeCameraAssist", 0, 0, new Dimension(0,0));

        mapHeight = mh;
        mapWidth = mw;
        zoom = z;
    }


    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        //super.paint(g, elapsedTime, camera, currStage);
        camera.instantlyCenterToPos(mapWidth/3, mapHeight/3);
        camera.instantlyZoom(zoom);
    }
}
