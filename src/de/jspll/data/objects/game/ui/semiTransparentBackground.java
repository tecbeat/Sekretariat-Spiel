package de.jspll.data.objects.game.ui;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.graphics.Camera;

import java.awt.*;

public class semiTransparentBackground extends GameObject {
    int posX, posY;
    Dimension dim;

    public semiTransparentBackground(String ID, String objectID, int x, int y, Dimension dimension) {
        super(ID, objectID);
        this.posX = x;
        this.posY = y;
        this.dim = dimension;
        channels = new ChannelID[]{ChannelID.LOGIC, ChannelID.BACKGROUND};
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {

        g.setColor(new Color(46, 49, 49,200));
        g.fillRect(posX, posY,dim.width,dim.height);

        //super.paint(g, elapsedTime, camera, currStage);

    }
}
