package de.jspll.handlers;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.GameTrie;
import de.jspll.graphics.Camera;
import de.jspll.graphics.TextureHandler;

import java.awt.*;
import java.util.ArrayList;

import static de.jspll.data.ChannelID.INSTANCE_REGISTER;
import static de.jspll.data.ChannelID.LAST_CHANNEL;

/**
 * Created by reclinarka on 21-Oct-20.
 */
public class GameObjectHandler {
    public GameObjectHandler() {
        for (int i = 0; i < channels.length; i++) {
            channels[i] = new GameTrie();
        }
    }

    public Point getMousePos() {
        if (graphicsHandler == null)
            return null;
        return graphicsHandler.getMousePos();
    }

    //Initializing / updating handlers
    public Camera getSelectedCamera() {
        return graphicsHandler.getSelectedCamera();
    }

    public Dimension getScreenDimensions() {
        return graphicsHandler.getWindow().getSize();
    }

    public GraphicsHandler getGraphicsHandler() {
        return graphicsHandler;
    }

    public TextureHandler getTextureHandler() {
        return textureHandler;
    }

    private TextureHandler textureHandler = new TextureHandler(this);

    private GraphicsHandler graphicsHandler;

    public static boolean DEBUG = true;

    private GameTrie[] channels = new GameTrie[LAST_CHANNEL.valueOf() + 1];

    public void setGraphicsHandler(GraphicsHandler graphicsHandler) {
        this.graphicsHandler = graphicsHandler;
    }

    /**
     * Register item in instance register
     * @param item
     */
    public void register(GameObject item) {
        channels[INSTANCE_REGISTER.valueOf()].insert(item.getID(), item);
        item.setListener(this);
    }

    /**
     * Subscribe item to channels listet in object
     * @param item
     */
    public void subscribe(GameObject item) {
        if (item.getChannels() != null && item.getChannels().length > 0) {
            for (ChannelID id : item.getChannels()) {
                if (id == INSTANCE_REGISTER)
                    return;
                this.channels[id.valueOf()].insert(item.getID(), item);
            }
        }
    }

    /**
     * Subscribe item to channel
     * @param item
     * @param channel
     */
    public void subscribe(GameObject item, ChannelID channel) {
        if (channel == INSTANCE_REGISTER)
            return;
        channels[channel.valueOf()].insert(item.getID(), item);
    }

    /**
     * Subscribe item to channel
     * @param item
     * @param channel
     * @param id
     */
    public void subscribe(GameObject item, ChannelID channel, String id) {
        if (channel == INSTANCE_REGISTER)
            return;
        channels[channel.valueOf()].insert(id, item);
    }

    /**
     * Unsibscribe item from everything except the instance register
     * @param item
     */
    public void unsubscribe(GameObject item) {
        for (ChannelID id : item.getChannels()) {
            if (id == INSTANCE_REGISTER)
                continue;
            this.channels[id.valueOf()].delete(item.getID());
        }
    }

    /**
     * Unsubscribe item from channel
     * @param item
     * @param channel
     */
    public void unsubscribe(GameObject item, ChannelID channel) {
        if (channel == INSTANCE_REGISTER)
            return;
        channels[channel.valueOf()].delete(item.getID());
    }

    /**
     * Unsubscribe item (by id) from channel
     * @param channel
     * @param id
     */
    public void unsubscribe(ChannelID channel, String id) {
        if (channel == INSTANCE_REGISTER)
            return;
        channels[channel.valueOf()].delete(id);
    }

    /**
     * Remove item from instance channel
     * @param item item to delete
     */
    public void delete(GameObject item) {
        unsubscribe(item);
        channels[INSTANCE_REGISTER.valueOf()].delete(item.getID());
    }

    /**
     * Call all objects in submitted channel list
     * @param targets channel array
     * @param scope
     * @param input
     */
    public void dispatch(ChannelID[] targets, String scope, Object[] input) {
        for (ChannelID target : targets) {
            dispatch(target, scope, input);
        }
    }

    /**
     * Call all objects in target channel, fitting scope
     * @param target channel
     * @param scope scope
     * @param input user input
     */
    public void dispatch(ChannelID target, String scope, Object[] input) {
        for (GameObject object : channels[target.valueOf()].allValuesAfter(scope)) {
            object.call(input);
        }
    }

    /**
     * Call all objects in target channel
     * @param target channel
     * @param input user input
     */
    public void dispatch(ChannelID target, Object[] input) {
        for (GameObject object : channels[target.valueOf()].allValues()) {
            object.call(input);
        }
    }

    public GameTrie getChannel(ChannelID channel) {
        return channels[channel.valueOf()];
    }

    /**
     * @param objects list of objects to load
     */
    public void loadObjects(ArrayList<GameObject> objects) {
        for (GameObject object : objects) {
            register(object);
            subscribe(object);
        }
    }

    /**
     * @param object object to load
     */
    public void loadObject(GameObject object) {
        register(object);
        subscribe(object);
    }


}

