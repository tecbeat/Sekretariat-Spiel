package de.jspll.data.objects;

import de.jspll.data.ChannelID;
import de.jspll.handlers.GameObjectHandler;
import de.jspll.graphics.Camera;
import de.jspll.graphics.Drawable;
import de.jspll.logic.Interactable;

import java.awt.*;
import java.awt.image.BufferedImage;

import static de.jspll.data.ChannelID.BACKGROUND;
import static de.jspll.data.ChannelID.INPUT;

/**
 * Created by reclinarka on 21-Oct-20.
 */
public class GameObject implements Drawable, Interactable {

    //InstanceSpecific ID: i.e 1
    private String ID;
    //ID that is specific for this class: i.e g.ntt.Player
    private String objectID;
    private boolean active = true;
    private int x = 0;
    private int y = 0;
    private int width = 16;
    private int height = 16;
    private BufferedImage texture = null;

    private GameObjectHandler parent;


    public GameObject() {

    }

    public GameObject(String ID, String objectID) {
        this.ID = ID;
        this.objectID = objectID;
    }

    public GameObject(String ID, String objectID, int x, int y) {
        this.x = x;
        this.y = y;
        this.ID = ID;
        this.objectID = objectID;
    }

    private void setID(String ID) {
        this.ID = ID;
    }

    public ChannelID[] getChannels() {
        return new ChannelID[]{BACKGROUND, INPUT};
    }

    protected GameObjectHandler getParent() {
        return parent;
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera) {
        if (texture == null) {
            g.setColor(Color.PINK);
            g.fillRect(camera.applyXTransform(x) , camera.applyYTransform(y), camera.applyZoom(width) , camera.applyZoom(height));
        } else {
            g.drawImage(texture,camera.applyXTransform(x) , camera.applyYTransform(y), camera.applyZoom(width) , camera.applyZoom(height),null);
        }
    }

    @Override
    public char call(Object[] input) {
        if (input == null || input.length < 1) {
            return 0;
        } else if (input[0].getClass() == String.class) {
            if (((String) input[0]).contentEquals("pause")) {
                active = false;
            } else if (((String) input[0]).contentEquals("continue")) {
                active = true;
            }
        }

        return 0;
    }


    public boolean isActive() {
        return active;
    }

    @Override
    public String getID() {
        return objectID + ":" + ID;
    }

    @Override
    public void setListener(GameObjectHandler listener) {
        parent = listener;
    }
}
