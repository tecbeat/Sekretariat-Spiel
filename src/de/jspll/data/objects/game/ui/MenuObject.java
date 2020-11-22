package de.jspll.data.objects.game.ui;

import de.jspll.Main;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.graphics.Camera;

import java.awt.*;

public class MenuObject extends GameObject {
    private String text;
    private boolean border;

    public MenuObject(){

    }


    public MenuObject(String ID, String objectID, int x, int y, Dimension dimension, String text, boolean border) {
        super(ID, objectID, x, y, dimension);
        this.text = text;
        this.border = border;
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        //super.paint(g,elapsedTime,camera,currStage);
        g.setColor(Color.BLUE);
        g.setFont(new Font("Serif", Font.PLAIN, 28));
        g.drawString(text, camera.applyXTransform(x+5), camera.applyYTransform(y+13));

        if(border){
            g.drawRect(camera.applyXTransform(x), camera.applyYTransform(y), camera.applyZoom((int) dimension.getWidth()),
                    camera.applyZoom((int) dimension.getHeight()));
        }
    }
}
