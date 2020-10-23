package de.jspll.data.objects;

/**
 * Created by reclinarka on 23-Oct-20.
 */
public class Player extends GameObject{
    public Player(String ID){
        super(ID,"g.ntt.Player");
    }

    public Player(){
        super("default","g.ntt.Player");
    }


}
