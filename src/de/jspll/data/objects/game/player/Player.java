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

    @Override
    public ChannelID[] getChannels() {

        return super.getChannels();
    }


    @Override
    public char update(float elapsedTime) {
        return super.update(elapsedTime);

    }

    @Override
    public char call(Object[] input) {
        return super.call(input);

    }


}
