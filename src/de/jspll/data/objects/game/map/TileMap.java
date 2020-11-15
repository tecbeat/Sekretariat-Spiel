package de.jspll.data.objects.game.map;


import de.jspll.data.objects.Texture;
import de.jspll.data.objects.TexturedObject;
import de.jspll.graphics.Camera;

import java.awt.*;
import java.util.Arrays;

/**
 * Representation of the map.
 *
 * @author Laura Schmidt
 */
public class TileMap extends TexturedObject {

    private Point playerPos;
    private int[][] tileMap;
    private Tile[] tiles;

    public TileMap(String ID, String objectID, Point playerPos, int x, int y , Dimension dimension, int tileRowCount, int tileColCount){
        super(ID, objectID, x, y, dimension,null);
        tileMap = new int[tileRowCount][tileColCount];
    }

    public void setTileToMap(Tile tile, int xCoord, int yCoord) {
        for(int i = 0; i < tiles.length; i++) {
            if(tiles[i] != null && tiles[i].equals(tile)) {
                tileMap[xCoord][yCoord] = i;
            }
        }
    }

    public void setTileToMap(int tilePosInArray, int xCoord, int yCoord) {
        if(tiles[tilePosInArray] != null) {
            tileMap[xCoord][yCoord] = tilePosInArray;
        }
    }

    public void removeTileFromMap(int xCoord, int yCoord) {
        tileMap[xCoord][yCoord] = -1;
    }

    /**
     * Adds Tile to array of tiles.
     *
     * @param tileToAdd Tile to add
     */
    public void addTile(Tile tileToAdd) {
        Tile[] updatedTilesArray = Arrays.copyOf(tiles, tiles.length + 1);
        updatedTilesArray[updatedTilesArray.length - 1] = tileToAdd;
    }

    /**
     * Removes Tile from array of tiles.
     *
     * @param index position of Tile that gets removed
     */
    public void removeTileFromTileArray(int index) {
        tiles[index] = null;
    }

    /**
     * Removes Tile from array of tiles.
     *
     * @param tile Tile that gets removed
     */
    public void removeTilesFromTileArray(Tile tile) {
        for(int i = 0; i < tiles.length; i++) {
            if(tiles[i] != null && tiles[i].equals(tile)) {
                tiles[i] = null;
            }
        }
    }

    /**
     * Checks if Tile on position (xCoord, yCoord) is collidable.
     *
     * @param xCoord x-coordinate
     * @param yCoord y-coordinate
     * @return {@code true} if Tile is collidable, {@code false} if Tile isn't collidable or position in tileMap is undefined
     * @see Tile#isCollidable()
     */
    public boolean isTileCollidable(int xCoord, int yCoord) {
        if(tileMap[xCoord][yCoord] != -1) {
            int tileIndexInArray = tileMap[xCoord][yCoord];
            return tiles[tileIndexInArray].isCollidable();
        }
        return false;
    }

    @Override
    public void requestTexture() {

    }

    @Override
    public char call(Object[] input) {
        super.call(input);
        return 0;
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera) {
        super.paint(g, elapsedTime, camera);
        // durch TileMap iterieren und Tiles zeichnen, die vorhanden sind
        // Texture.draw --> offset gleich indezes von tileMap
    }
}

class Tile {
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
