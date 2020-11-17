package de.jspll.data.objects.game.map;

import de.jspll.data.objects.Texture;

public class Tile {
    private final boolean collidable;
    private final Texture texture;

    public Tile(boolean collidable, Texture texture) {
        this.collidable = collidable;
        this.texture = texture;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public Texture getTexture() {
        return texture;
    }
}
