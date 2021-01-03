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
    //the "how maniest" element below the others this is
    protected int offset;

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
        int xCenter = camera.getCenter()[0];
        int yCenter = camera.getCenter()[1];

        //Center the object (and place multiple below each other by offset)
        x = xCenter - (camera.applyZoom(dimension.width) / 2);
        y = yCenter - camera.applyZoom (40) - ((camera.applyZoom(dimension.height) - offset * camera.applyZoom(50)) / 2);

        if(border){
            g.setColor(Color.BLUE);
            g.drawRect(x, y, camera.applyZoom((int) dimension.getWidth()),
                    camera.applyZoom((int) dimension.getHeight()));
        }

        g.setFont(new Font("Kristen ITC", Font.PLAIN, camera.applyZoom(18)));
        g.setColor(Color.WHITE);
        g.drawString(text, x+camera.applyZoom(5), y+camera.applyZoom(13));
    }
}
