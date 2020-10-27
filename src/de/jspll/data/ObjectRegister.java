package de.jspll.data;

import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.game.player.Player;

/**
 * Created by reclinarka on 21-Oct-20.
 */
public enum ObjectRegister {
    DEFAULT(new GameObject()),
    PLAYER(new Player());

    ObjectRegister(GameObject object){
        this.object = object;
    }
    private GameObject object;

}
