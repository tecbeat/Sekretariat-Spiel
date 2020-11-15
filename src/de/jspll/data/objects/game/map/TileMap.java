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
    private Point pos;
    private Dimension defaultTileDimension;

    public TileMap(String ID, String objectID, Point playerPos, int x, int y , Dimension dimension, int tileRowCount, int tileColCount){
        super(ID, objectID, x, y, dimension,null);
        pos = new Point(x, y);
        defaultTileDimension = new Dimension(this.dimension.width / tileColCount, this.dimension.height / tileRowCount);
        tileMap = new int[tileColCount][tileRowCount];
        initTileMap();
        debugInit();
    }

    private void initTileMap() {
        for(int xCoord = 0; xCoord < tileMap.length; xCoord++) {
            for(int yCoord = 0; yCoord < tileMap[xCoord].length; yCoord++) {
                tileMap[xCoord][yCoord] = -1;
            }
        }
    }

    private void debugInit() {
        tiles = new Tile[1];
        tiles[0] = new Tile(true, new Texture("assets\\map\\testTexture", pos, defaultTileDimension, this));
        setTileToMap(tiles[0], 0, 0);
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
    public void removeTileFromTileArray(Tile tile) {
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
        for (Tile t : tiles) {
            t.getTexture().requestTextures();
        }
    }

    @Override
    public char call(Object[] input) {
        if(input == null || input.length < 1) {
            return 0;
        } else if (input[0] instanceof String && ((String) input[0]).contentEquals("tilemap")) {
            if(input[1] instanceof String){
                switch ((String) input[1]) {
                    case "add":
                        callAdd(input);
                        break;
                    case "set":
                        callSet(input);
                        break;
                    case "remove":
                        callRemove(input);
                        break;
                    default:
                        return 0;
                }
            }
        }
        return 0;
    }

    private void callAdd(Object[] input) {
        if(input[2] instanceof Tile) {
            addTile((Tile) input[2]);
        }
    }

    private void callSet(Object[] input) {
        if(input[2] instanceof String) {
            switch ((String) input[2]) {
                case "index":
                    if(input[3] instanceof Integer && input[4] instanceof Integer && input[5] instanceof Integer) {
                        setTileToMap((Integer) input[3], (Integer) input[4], (Integer) input[5]);
                    }
                    break;
                case "tile":
                    if(input[3] instanceof Tile && input[4] instanceof Integer && input[5] instanceof Integer) {
                        setTileToMap((Tile) input[3], (Integer) input[4], (Integer) input[5]);
                    }
                    break;
            }
        }
    }

    private void callRemove(Object[] input) {
        if(input[2] instanceof String) {
            switch ((String) input[2]) {
                case "map":
                    if(input[3] instanceof Integer && input[4] instanceof Integer) {
                        removeTileFromMap((Integer) input[3], (Integer) input[4]);
                    }
                    break;
                case "array":
                    if(input[3] instanceof Tile) {
                        removeTileFromTileArray((Tile) input[3]);
                    }
                    if(input[3] instanceof Integer) {
                        removeTileFromTileArray((Integer) input[3]);
                    }
                    break;
            }
        }
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera) {
        super.paint(g, elapsedTime, camera);
        for (int xCoord = 0; xCoord < tileMap.length; xCoord++) {
            for (int yCoord = 0; yCoord < tileMap[xCoord].length; yCoord++) {
                if (tileMap[xCoord][yCoord] != -1) {
                    g.drawRect(camera.applyXTransform(x + xCoord * defaultTileDimension.width),
                            camera.applyYTransform(y + yCoord * defaultTileDimension.height),
                            camera.applyZoom(defaultTileDimension.width),
                            camera.applyZoom(defaultTileDimension.height));
                    tiles[tileMap[xCoord][yCoord]].getTexture().draw((Graphics2D) g, elapsedTime, camera,
                            xCoord * (int) tiles[tileMap[xCoord][yCoord]].getTexture().getDimension().getWidth(),
                            yCoord * (int) tiles[tileMap[xCoord][yCoord]].getTexture().getDimension().getWidth());
                }
            }
        }
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
