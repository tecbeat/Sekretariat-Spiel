package de.jspll.data.objects.game.map;

import com.google.gson.annotations.Expose;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.Texture;
import de.jspll.data.objects.TexturedObject;
import de.jspll.graphics.Camera;
import de.jspll.graphics.ResourceHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
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
    protected BufferedImage[] tileSets;
    private String[] textureKeys;

    public TileMap(String ID, String objectID, Point playerPos, int x, int y , Dimension dimension, int tileRowCount, int tileColCount, String[] textureKeys){
        super(ID, objectID, x, y, dimension,null);
        this.channels = new ChannelID[]{ChannelID.INPUT,ChannelID.BACKGROUND};
        pos = new Point(x, y);
        this.textureKeys = textureKeys;
        defaultTileDimension = new Dimension(this.dimension.width / tileColCount, this.dimension.height / tileRowCount);
        tileMap = new int[tileColCount][tileRowCount];
        initTileMap();
        debugInit();
    }

    /**
     * Initializes the {@code tileMap} with the value -1 at each position. -1 is the undefined value. That
     * means that there is no {@code Tile} set at the position.
     */
    private void initTileMap() {
        for (int[] ints : tileMap) {
            Arrays.fill(ints, -1);
        }
    }

    /**
     * Only for debug purposes.
     */
    private void debugInit() {
        tiles = new Tile[1];
        tiles[0] = new Tile(true, new int[]{32*5,32*6,32,32,0}, this);
        setTileToMap(0, 0, 0);
    }

    /**
     * Sets a {@code Tile} to a specified position (xCoord, yCoord) in {@code tileMap}.
     *
     * @param tile {@code Tile} to set at a specific position in the tileMap
     * @param xCoord x-coordinate
     * @param yCoord y-coordinate
     */
    public void setTileToMap(Tile tile, int xCoord, int yCoord) {
        for(int i = 0; i < tiles.length; i++) {
            if(tiles[i] != null && tiles[i].equals(tile)) {
                tileMap[xCoord][yCoord] = i;
            }
        }
    }

    /**
     * Sets a {@code Tile} to the {@code tileMap} given the index where the {@code Tile}
     * is in the array {@code tiles}. The coordinates where the {@code Tile} are specified
     * by xCoord and yCoord.
     *
     * @param tilePosInArray position where {@code Tile} is in the array {@code tiles}
     * @param xCoord x-coordinate
     * @param yCoord y-coordinate
     */
    public void setTileToMap(int tilePosInArray, int xCoord, int yCoord) {
        if(tiles[tilePosInArray] != null) {
            tileMap[xCoord][yCoord] = tilePosInArray;
        }
    }

    /**
     * Sets the value at (xCoord, yCoord) to -1 which means that the value at the position
     * (xCoord, yCoord) isn't set/undefinied.
     *
     * @param xCoord x-coordinate
     * @param yCoord y-coordinate
     */
    public void removeTileFromMap(int xCoord, int yCoord) {
        tileMap[xCoord][yCoord] = -1;
    }

    /**
     * Adds a {@code Tile} to array {@code tiles}.
     *
     * @param tileToAdd {@code Tile} to add
     */
    public void addTile(Tile tileToAdd) {
        Tile[] updatedTilesArray = Arrays.copyOf(tiles, tiles.length + 1);
        updatedTilesArray[updatedTilesArray.length - 1] = tileToAdd;
        tiles = updatedTilesArray;
    }

    /**
     * Removes {@code Tile} from array {@code tiles} based on the given index.
     *
     * @param index position of Tile that gets removed
     */
    public void removeTileFromTileArray(int index) {
        tiles[index] = null;
    }

    /**
     * Removes {@code Tile} from array {@code tiles} based on the given {@code Tile}-object.
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
        for( String textureKey: textureKeys)
            getParent().getResourceHandler().requestTexture(textureKey, ResourceHandler.FileType.PNG);
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
    public void loadTexture() {
        if(isTextureLoaded())
            return;
        BufferedImage[] builder = new BufferedImage[textureKeys.length];
        for(int i = 0; i < textureKeys.length; i++){
            if(!getParent().getResourceHandler().isAvailable(textureKeys[i], ResourceHandler.FileType.PNG) ||
                    getParent().getResourceHandler().getTexture(textureKeys[i], ResourceHandler.FileType.PNG) == null){
                if(!getParent().getResourceHandler().getLoadingQueue().contains(textureKeys[i])){
                    getParent().getResourceHandler().requestTexture(textureKeys[i], ResourceHandler.FileType.PNG);

                }
                return;
            }
            builder[i] = getParent().getResourceHandler().getTexture(textureKeys[i], ResourceHandler.FileType.PNG);
        }
        tileSets = builder;
        setTextureLoaded(true);
    }

    @Override
    protected void drawFrame(Graphics g, float elapsedTime, Camera camera) {
        for (int xCoord = 0; xCoord < tileMap.length; xCoord++) {
            for (int yCoord = 0; yCoord < tileMap[xCoord].length; yCoord++) {
                if (tileMap[xCoord][yCoord] != -1) {
                    g.drawRect(camera.applyXTransform(x + xCoord * defaultTileDimension.width),
                            camera.applyYTransform(y + yCoord * defaultTileDimension.height),
                            camera.applyZoom(defaultTileDimension.width),
                            camera.applyZoom(defaultTileDimension.height));
                    if(!isTextureLoaded()){
                        continue;
                    }
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.drawImage(tiles[tileMap[xCoord][yCoord]].getTexture(this),
                            camera.applyXTransform(x + xCoord * defaultTileDimension.width),
                            camera.applyYTransform(y + yCoord * defaultTileDimension.height),
                            camera.applyZoom(defaultTileDimension.width),
                            camera.applyZoom(defaultTileDimension.height),null);

                }
            }
        }
    }
}

class Tile {
    private transient TileMap parent;
    private final boolean collidable;
    private final int[] textureReference;

    //Can Be Excluded
    private BufferedImage cache;

    public Tile(boolean collidable, int[] textureReference, TileMap parent) {
        this.collidable = collidable;
        this.textureReference = textureReference;
        this.parent = parent;
    }

    public void setParent(TileMap parent) {
        this.parent = parent;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public BufferedImage getTexture(TileMap gO) {
        if(parent == null)
            parent = gO;
        if(cache == null){
           cache = parent.tileSets[textureReference[4]].getSubimage(textureReference[0],textureReference[1],textureReference[2],textureReference[3]);
        }
        return cache;
    }
}
