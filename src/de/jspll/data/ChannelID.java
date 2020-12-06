package de.jspll.data;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Philipp Polland
 *
 * @version 1.0
 */

public enum ChannelID {
    INSTANCE_REGISTER(0),
    INPUT(1),
    LOGIC(2),
    COMM1(3),

    //GRAPHICS CHANNELS
    FIRST_LAYER(4),
    BACKGROUND(4),
    FOREGROUND(5),
    ENTITIES(6),
    PLAYER(7),
    UI(8),
    EFFECTS(9),
    OVERLAY(10),
    LAST_LAYER(10),

    //LOGIC CHANNELS
    COLLISION(11),


    //SCENES
    SCENE_0(15),
    SCENE_1(16),
    SCENE_GAME(17),
    SCENE_3(18),
    SCENE_LOADING(19),

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
