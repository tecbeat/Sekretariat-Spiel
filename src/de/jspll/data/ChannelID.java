package de.jspll.data;

/**
 * Created by reclinarka on 23-Oct-20.
 */
public enum ChannelID {
    INSTANCE_REGISTER(0),
    INPUT(1),
    LOGIC(2),
    COMM1(3),

    FIRST_LAYER(4),
    BACKGROUND(4),
    FOREGROUND(5),
    ENTITIES(6),
    PLAYER(7),
    UI(8),
    EFFECTS(9),
    OVERLAY(10),
    LAST_LAYER(10),


    DISPATCH(19),
    LAST_CHANNEL(19);

    private int id;
    ChannelID(int id){
        this.id = id;
    }
    private static ChannelID[] index;

    private static void init(){
        if(index == null){
            index = new ChannelID[LAST_CHANNEL.valueOf()+1];
            for(ChannelID id: ChannelID.values()){
                index[id.valueOf()] = id;
            }
        }
    }

    public static ChannelID getbyID(int id){
        init();
        return index[id];
    }

    public int valueOf() {
        return id;
    }
}
