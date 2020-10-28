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

    /**
     * Constructor
     * @param ID Sets the object id (inherited from game object)
     * @returns Obejct
     */
    public Counter(String ID) {
        super(ID,"g.tst.Counter");
    }

    /**
     * @return ChannelID
     */
    @Override
    public ChannelID[] getChannels() {
        return new ChannelID[]{ChannelID.FOREGROUND};
    }

    /**
     * This method handles drawing for the counter object
     * @param g graphics object
     * @param elapsedTime time since last draw
     * @param camera camera object
     */
    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera) {
        test += elapsedTime;
        g.setColor(Color.WHITE);
        g.drawString("" + test,50,50); //show time since start
    }
}
