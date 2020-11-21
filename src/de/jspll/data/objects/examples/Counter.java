package de.jspll.data.objects.examples;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.graphics.Camera;

import java.awt.*;

/**
 * Created by reclinarka on 26-Oct-20.
 */
public class Counter extends GameObject {
    private float test = 0f;

    public Counter(String ID) {
        super(ID,"g.tst.Counter");
    }

    @Override
    public ChannelID[] getChannels() {
        return new ChannelID[]{ChannelID.FOREGROUND};
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        test += elapsedTime;
        g.setColor(Color.WHITE);
        g.drawString("" + test,50,50);
    }
}
