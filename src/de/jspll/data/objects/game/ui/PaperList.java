package de.jspll.data.objects.game.ui;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.graphics.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by reclinarka on 27-Oct-20.
 */
public class PaperList extends GameObject {
    public PaperList(String ID, Dimension size, Point pos) {
        super(ID, "g.ui.PaperList", pos.x, pos.y, size);
        channels = new ChannelID[]{ChannelID.UI, ChannelID.INPUT};
    }

    private static BufferedImage texture;
    private HashMap<String, AtomicBoolean> keyMap;
    private int framesOn = 0;
    private boolean mousedown;
    private int[] currMousePos = new int[]{0, 0};
    private int[] previousMousePos = new int[]{0, 0};

    @Override
    public char update(float elapsedTime) {
        return super.update(elapsedTime);
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera) {
        if(framesOn > 10)
            g.setColor(Color.RED);
        super.paint(g, 0, camera);
        if (texture == null) {
            texture = getParent().getTextureHandler().getTexture("paper.png");
        } else {
            g.drawImage(texture, camera.applyXTransform(x), camera.applyYTransform(y),
                    camera.applyZoom((int) dimension.getWidth()), camera.applyZoom((int) dimension.getHeight()),
                    null);
        }
    }

    @Override
    public char call(Object[] input) {
        super.call(input);
        if (input == null || input.length < 1) {
            return 0;
        } else if (input[0].getClass() == String.class) {
            if (((String) input[0]).contentEquals("input")) {
                if ((boolean) input[3]) {
                    mousedown = true;
                } else {
                    framesOn = 0;
                    mousedown = false;
                }
                int[] pos = (int[]) input[5];
                currMousePos[0] = pos[0];
                currMousePos[1] = pos[1];

                if (input[4].getClass() == HashMap.class) {
                    this.keyMap = (HashMap<String, AtomicBoolean>) input[4];
                }

            }
        }

        if (mousedown) {
            framesOn++;
            if (framesOn > 10) {
                if (currMousePos != null && previousMousePos != null) {
                    x -= - getParent().getSelectedCamera().revertXTransform(currMousePos[0]) + getParent().getSelectedCamera().revertXTransform(previousMousePos[0]);
                    y -= - getParent().getSelectedCamera().revertXTransform(currMousePos[1]) + getParent().getSelectedCamera().revertXTransform(previousMousePos[1]);
                    }
            }
        }
        previousMousePos[0] = currMousePos[0];
        previousMousePos[1] = currMousePos[1];


        return 0;

    }
}
