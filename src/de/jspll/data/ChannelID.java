package de.jspll.data;

/**
 * Created by reclinarka on 23-Oct-20.
 */
public enum ChannelID {
    INSTANCE_REGISTER(0),
    INPUT(1),
    LOGIC(2),
    COMM1(3),
    BACKGROUND(4),
    DISPATCH(19),
    LAST_CHANNEL(19);

    private int id;
    ChannelID(int id){
        this.id = id;
    }

    public int valueOf() {
        return id;
    }
}
