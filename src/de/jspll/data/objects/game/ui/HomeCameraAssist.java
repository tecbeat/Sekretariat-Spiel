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
    //map and window properties
    int mapWidth;
    int mapHeight;
    Dimension windowDim;

    //temp storage
    float zoom;

    //During the Game the Camera centers to the player. On the homescreen there is no Player to center to.
    //The HCA Class positions the camera so that the tilemap is always screen filling.
    public HomeCameraAssist(int mw, int mh, float z){
        super("hca", "homeCameraAssist", 0, 0, new Dimension(0,0));

        mapHeight = mh;
        mapWidth = mw;
        zoom = z;

        channels = new ChannelID[]{ChannelID.FIRST_LAYER};
    }


    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        //Could also be taken from Camera - does not matter
        windowDim = getParent().getGraphicsHandler().getWindow().getSize();

        //to always fill the screen you need to take the bigger zoom faktor
        zoom = Math.max(((float)windowDim.width / (float)mapWidth), ((float)windowDim.height / (float)mapHeight));

        //Coordinates to center the camera to
        int posX = Math.min(camera.getWidth() / 2, (int)(mapWidth/2 * zoom));
        int posY = Math.min(camera.getHeight() / 2, (int)(mapHeight/2 * zoom));


        camera.instantlyCenterToPos(posX, posY);
        camera.instantlyZoom(zoom);
    }
}
