package de.jspll.data.objects.game.map;


import de.jspll.data.objects.Texture;
import de.jspll.data.objects.TexturedObject;
import de.jspll.graphics.Camera;

import java.awt.*;
/**
 * Created by reclinarka on 09-Nov-20.
 */
public class TileMap extends TexturedObject {

    public TileMap(String ID, String objectID, Point playerPos, int x, int y , Dimension dimension, int tileRowCount, int tileColCount){
        super(ID,objectID,x,y, dimension,null);
        tileMap = new Tile[tileRowCount][tileColCount];

    }

    Point playerPos;
    Tile[][] tileMap;
    Texture[] textures;

    @Override
    public void requestTexture() {

    }

    @Override
    public char call(Object[] input) {
        return super.call(input);
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera) {
        //for ()
    }
}

class Tile {
    Dimension hitbox;
    Point pos;
    int textureID;
    public Tile(Dimension hitbox, Point pos){
        this.hitbox = hitbox;
        this.pos = pos;
    }
}
