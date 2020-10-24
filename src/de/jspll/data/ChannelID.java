package de.jspll.data;

/**
 * Created by reclinarka on 23-Oct-20.
 */
public enum ChannelID {
    INSTANCE_REGISTER(0),
    INPUT(1),
    BACKGROUND(2),
    LAST_CHANNEL(19);

    private int id;
    ChannelID(int id){
        this.id = id;
    }

    public int valueOf() {
        return id;
    }
}
