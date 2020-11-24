package de.jspll.data;

import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.game.player.Player;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author
 *
 * @version 1.0
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
