package de.jspll.data.objects;

import de.jspll.data.ChannelID;
import de.jspll.handlers.GameObjectHandler;
import static de.jspll.data.ChannelID.DISPATCH;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Philipp Polland
 *
 * @version 1.0
 */
@Deprecated
public class Repeater extends GameObject{
    GameObjectHandler reciever;
    public Repeater(String ID,GameObjectHandler reciever) {
        super(ID,"g.tst.Repeater");
        this.reciever = reciever;
    }

    public void setReciever(GameObjectHandler reciever) {
        this.reciever = reciever;
    }

    @Override
    public char call(Object[] input) {
        if(input[0].getClass() == ChannelID.class){
            reciever.dispatch((ChannelID) input[0],(Object[]) input[1]);
            return 0;
        }
        return super.call(input);
    }

    @Override
    public ChannelID[] getChannels() {
        return new ChannelID[]{DISPATCH};
    }
}
