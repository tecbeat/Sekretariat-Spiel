package de.jspll.data.objects;

import de.jspll.data.ChannelID;
import de.jspll.handlers.GameObjectHandler;
import de.jspll.graphics.Camera;
import de.jspll.graphics.Drawable;
import de.jspll.logic.Interactable;
import sun.security.util.ArrayUtil;

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

    public char update(float elapsedTime){

        return 0;
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera) {
        g.setColor(Color.PINK);
        g.fillRect(camera.applyXTransform(x) , camera.applyYTransform(y), camera.applyZoom(width) , camera.applyZoom(height));

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

    public void dispatchToDifferentObjectHandler(ChannelID channelID, Object[] input){
        Object[] payload = new Object[input.length + 1];
        payload[0] = channelID;
        for(int i = 0; i < input.length;i++){
            payload[i+1] = input[i];
        }
        parent.dispatch(ChannelID.DISPATCH,payload);
    }
    public void dispatchToDifferentObjectHandler(ChannelID[] channelIDS, Object[] input){
        Object[] payload = new Object[input.length + 1];
        payload[0] = channelIDS;
        for(int i = 0; i < input.length;i++){
            payload[i+1] = input[i];
        }
        parent.dispatch(ChannelID.DISPATCH,payload);
    }
    public void dispatchToDifferentObjectHandler(ChannelID channelID,String scope , Object[] input){
        Object[] payload = new Object[input.length + 1];
        payload[0] = channelID;
        for(int i = 0; i < input.length;i++){
            payload[i+1] = input[i];
        }
        parent.dispatch(ChannelID.DISPATCH,scope,payload);
    }
}
