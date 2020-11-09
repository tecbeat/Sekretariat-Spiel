package de.jspll.data;

import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.game.player.Player;

/**
 * Created by reclinarka on 21-Oct-20.
 */
public enum ObjectRegister {
    DEFAULT(new GameObject(), "GameObject"),
    PLAYER(new Player(), "Player");

    ObjectRegister(GameObject object, String type){
        this.type = type;
        this.object = object;
    }

    public boolean isType(String type){
        return this.type.contentEquals(type);
    }

    public GameObject getObject() {
        return object;
    }

    private GameObject object;
    private String type;

}
