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

public class MenuObject extends GameObject {
    protected String text;
    protected boolean border;

    public MenuObject(){

    }


    public MenuObject(String ID, String objectID, int x, int y, Dimension dimension, String text, boolean border) {
        super(ID, objectID, x, y, dimension);
        this.text = text;
        this.border = border;
        channels = new ChannelID[]{ChannelID.INPUT, ChannelID.LOGIC, ChannelID.UI};
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        //super.paint(g,elapsedTime,camera,currStage);

        if(border){
            g.setColor(Color.BLUE);
            g.drawRect(camera.applyXTransform(x), camera.applyYTransform(y), camera.applyZoom((int) dimension.getWidth()),
                    camera.applyZoom((int) dimension.getHeight()));
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.PLAIN, 28));
        g.drawString(text, camera.applyXTransform(x+5), camera.applyYTransform(y+13));


    }
}
