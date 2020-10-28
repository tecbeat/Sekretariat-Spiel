package de.jspll.data.objects.game.player;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;

/**
 * Created by reclinarka on 23-Oct-20.
 */
public class Player extends GameObject {
    public Player(String ID){
        super(ID,"g.ntt.Player");
    }

    public Player(){
        super("default","g.ntt.Player");
    }

    /**
     * @return subscribed channels
     */
    @Override
    public ChannelID[] getChannels() {

        return super.getChannels();
    }


    /**
     * gets called during game loop
     * @param elapsedTime time since last update
     * @return status
     */
    @Override
    public char update(float elapsedTime) {
        return super.update(elapsedTime);

    }

    /**
     * gets called during game loop
     * @param input user input
     * @return status
     */
    @Override
    public char call(Object[] input) {
        return super.call(input);

    }


}
