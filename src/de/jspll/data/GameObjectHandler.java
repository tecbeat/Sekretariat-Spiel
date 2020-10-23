package de.jspll.data;

import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.GameTrie;

import java.util.ArrayList;

import static de.jspll.data.ChannelID.INSTANCE_REGISTER;

/**
 * Created by reclinarka on 21-Oct-20.
 */
public class GameObjectHandler {
    public GameObjectHandler() {
        for (int i = 0; i < channels.length; i++) {
            channels[i] = new GameTrie();
        }
    }

    private GameTrie[] channels = new GameTrie[20];

    public void register(GameObject item) {
        channels[INSTANCE_REGISTER.valueOf()].insert(item.getID(), item);
        item.setListener(this);
    }

    public void subscribe(GameObject item) {
        if (item.getChannels() != null && item.getChannels().length > 0) {
            for (ChannelID id : item.getChannels()) {
                if (id == INSTANCE_REGISTER)
                    return;
                this.channels[id.valueOf()].insert(item.getID(), item);
            }
        }
    }

    public void subsribe(GameObject item, ChannelID channel) {
        if (channel == INSTANCE_REGISTER)
            return;
        channels[channel.valueOf()].insert(item.getID(), item);
    }

    public void subsribe(GameObject item, ChannelID channel, String id) {
        if (channel == INSTANCE_REGISTER)
            return;
        channels[channel.valueOf()].insert(id, item);
    }

    public void unsubscribe(GameObject item) {
        for (ChannelID id : item.getChannels()) {
            if (id == INSTANCE_REGISTER)
                continue;
            this.channels[id.valueOf()].delete(item.getID());
        }
    }

    public void unsubsribe(GameObject item, ChannelID channel) {
        if (channel == INSTANCE_REGISTER)
            return;
        channels[channel.valueOf()].delete(item.getID());
    }

    public void unsubsribe(ChannelID channel, String id) {
        if (channel == INSTANCE_REGISTER)
            return;
        channels[channel.valueOf()].delete(id);
    }

    public void delete(GameObject item) {
        unsubscribe(item);
        channels[INSTANCE_REGISTER.valueOf()].delete(item.getID());
    }

    public void dispatch(ChannelID[] targets, String scope, Object[] input) {
        for (ChannelID target : targets) {
            dispatch(target, scope, input);
        }
    }

    public void dispatch(ChannelID target, String scope, Object[] input) {
        for (GameObject object : channels[target.valueOf()].allValuesAfter(scope)) {
            object.call(input);
        }
    }

    public void dispatch(ChannelID target, Object[] input) {
        for (GameObject object : channels[target.valueOf()].allValues()) {
            object.call(input);
        }
    }

    public GameTrie getChannel(ChannelID channel) {
        return channels[channel.valueOf()];
    }

    public void loadObjects(ArrayList<GameObject> objects) {
        for (GameObject object : objects) {
            register(object);
            subscribe(object);
        }
    }


}

