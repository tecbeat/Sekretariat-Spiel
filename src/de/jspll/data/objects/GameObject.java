package de.jspll.data.objects;

import de.jspll.data.*;
import de.jspll.handlers.GameObjectHandler;
import de.jspll.graphics.Camera;
import de.jspll.graphics.Drawable;
import de.jspll.logic.Interactable;

import java.awt.*;
import java.awt.Dimension;
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


    protected boolean active = true;
    protected int x = 0;
    protected int y = 0;
    protected Dimension dimension;
    protected ChannelID[] channels;


    private GameObjectHandler parent;


    public GameObject() {

    }

    public GameObject(String ID, String objectID) {
        this.ID = ID;
        this.objectID = objectID;
    }

    public GameObject(String ID, String objectID, int x, int y, Dimension dimension) {
        this.x = x;
        this.y = y;
        this.ID = ID;
        this.objectID = objectID;
        this.dimension = dimension;
    }

    public GameObject(String ID, String objectID, int x, int y, Dimension dimension, ChannelID channels) {
        this.x = x;
        this.y = y;
        this.ID = ID;
        this.objectID = objectID;
        this.dimension = dimension;
    }

    private void setID(String ID) {
        this.ID = ID;
    }

    public ChannelID[] getChannels() {
        if(channels == null){
            return new ChannelID[]{ INPUT,BACKGROUND};
        }
        return channels;
    }

    protected GameObjectHandler getParent() {
        return parent;
    }

    public void subscribeToChannel(ChannelID channel){
        parent.subscribe(this,channel);
    }

    public void unsubscribeChannel(ChannelID channel){
        parent.unsubscribe(channel,getID());
    }

    public char update(float elapsedTime){

        return 0;
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera) {
        if(!GameObjectHandler.DEBUG)
            return;
        if(dimension == null)
            return;
        if(elapsedTime != 0)
            g.setColor(Color.PINK);
        g.drawRect(camera.applyXTransform(x) , camera.applyYTransform(y), camera.applyZoom((int) dimension.getWidth()) , camera.applyZoom((int) dimension.getHeight()));

    }

    protected GameObject createInstance(Object[] data){
        if(data == null)
            return null;
        switch (data.length){
            case 0:
                return new GameObject();
            case 2:
                return new GameObject((String)data[0],(String)data[1]);
            case 5:
                return new GameObject((String)data[0],(String)data[1],(int)data[2],(int)data[3],(Dimension) data[4]);
            case 6:
                return new GameObject((String)data[0],(String)data[1],(int)data[2],(int)data[3],(Dimension) data[4],(ChannelID) data[5]);
        }
        return null;
    }

    @Override
    public char call(Object[] input) {
        if (input == null || input.length < 1) {
            return 0;
        } else if (input[0] instanceof String) {
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

    public Dimension getDimension() {
        return dimension;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
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
