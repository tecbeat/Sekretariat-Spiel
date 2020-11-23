package de.jspll.data.objects.game.map;

import de.jspll.Main;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.TexturedObject;
import de.jspll.graphics.Camera;
import de.jspll.util.Collision;
import de.jspll.util.Logger;

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
    private boolean useConnectedStrategy = true;


    private Dimension defaultTileDimension;
    protected BufferedImage[] tileSets;
    private String[] textureKeys;
    private boolean coveringPlayer = false;

    //2d array representing map
    private int[][] collisionMap;
    //int arr in form: [ mapX, mapY, mapWidth, mapHeight, tileWidth, tileHeight ]
    private int[] mapPos_and_metaData;


    public TileMap(String ID, String objectID, Point playerPos, int x, int y, Dimension dimension, int tileRowCount, int tileColCount, String[] textureKeys) {
        super(ID, objectID, x, y, dimension, null);
        this.channels = new ChannelID[]{ChannelID.BACKGROUND, ChannelID.LOGIC};
        pos = new Point(x, y);
        this.textureKeys = textureKeys;
        defaultTileDimension = new Dimension(this.dimension.width / tileColCount, this.dimension.height / tileRowCount);
        tileMap = new int[tileColCount][tileRowCount];
        initTileMap();
        debugInit();
    }

    public TileMap(String ID, String objectID, int x, int y, Dimension dimension, int tileRowCount, int tileColCount, String[] textureKeys) {
        super(ID, objectID, x, y, dimension, null);
        this.channels = new ChannelID[]{ChannelID.BACKGROUND, ChannelID.LOGIC};
        pos = new Point(x, y);
        defaultTileDimension = new Dimension(this.dimension.width / tileColCount, this.dimension.height / tileRowCount);
        Logger.d.add("Tilemap: " + ID + " tileWidth=" + defaultTileDimension.width + " tileHeight=" + defaultTileDimension.height);
        tileMap = new int[tileColCount][tileRowCount];
        tiles = new Tile[0];
        this.textureKeys = textureKeys;
        initTileMap();
    }

    public TileMap(String ID, String objectID, int x, int y, Dimension dimension, int tileRowCount, int tileColCount, String[] textureKeys, boolean coveringPlayer) {
        super(ID, objectID, x, y, dimension, null);
        this.channels = new ChannelID[]{ChannelID.BACKGROUND, ChannelID.LOGIC, ChannelID.UI};
        pos = new Point(x, y);
        defaultTileDimension = new Dimension(this.dimension.width / tileColCount, this.dimension.height / tileRowCount);
        Logger.d.add("Tilemap: " + ID + " tileWidth=" + defaultTileDimension.width + " tileHeight=" + defaultTileDimension.height);
        tileMap = new int[tileColCount][tileRowCount];
        tiles = new Tile[0];
        this.coveringPlayer = coveringPlayer;
        this.textureKeys = textureKeys;
        initTileMap();
        if (ID.contentEquals("Boden2") || ID.contentEquals("Boden3")) {
            useConnectedStrategy = false;
        } else if(ID.contentEquals("Door")){
            isHalfRes = true;
        }
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
        tiles[0] = new Tile(true, new int[]{32 * 5, 32 * 6, 32, 32, 0}, this);
        setTileToMap(0, 0, 0);
    }

    /**
     * Sets a {@code Tile} to a specified position (xCoord, yCoord) in {@code tileMap}.
     *
     * @param tile   {@code Tile} to set at a specific position in the tileMap
     * @param xCoord x-coordinate
     * @param yCoord y-coordinate
     */
    public void setTileToMap(Tile tile, int xCoord, int yCoord) {
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] != null && tiles[i].equals(tile)) {
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
     * @param xCoord         x-coordinate
     * @param yCoord         y-coordinate
     */
    public void setTileToMap(int tilePosInArray, int xCoord, int yCoord) {
        if (tiles[tilePosInArray] != null) {
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
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] != null && tiles[i].equals(tile)) {
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
        if (tileMap[xCoord][yCoord] != -1) {
            int tileIndexInArray = tileMap[xCoord][yCoord];
            return tiles[tileIndexInArray].isCollidable();
        }
        return false;
    }


    @Override
    public void requestTexture() {
        for (String textureKey : textureKeys) {
            getParent().getResourceHandler().requestTexture(textureKey);
        }
    }

    @Override
    public char call(Object[] input) {
        super.call(input);
        if (input == null || input.length < 1) {
            return 0;
        } else if (input[0] instanceof String) {
            String cmd = (String) input[0];
            if (cmd.contentEquals("tilemap")) {
                if (input[1] instanceof String) {
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
            } else if (cmd.contentEquals("player")) {
                getParent().dispatch(ChannelID.PLAYER, new Object[]{"collision", tileMap, new int[]{pos.x, pos.y, dimension.width, dimension.height, defaultTileDimension.width, defaultTileDimension.height}});
            } else if (cmd.contentEquals("getCollisionArea")) {
                getParent().dispatch(ChannelID.LOGIC,(String) input[1], new Object[]{"collision", tileMap, new int[]{pos.x, pos.y, dimension.width, dimension.height, defaultTileDimension.width, defaultTileDimension.height}});
            } else if (cmd.contentEquals("playerPos")) {
                playerPos = (Point) input[1];
            } else if(cmd.contentEquals("collision")){
                collisionMap = (int[][]) input[1];
                mapPos_and_metaData = (int[]) input[2];
            }
        }
        return 0;
    }

    private void callAdd(Object[] input) {
        if (input[2] instanceof Tile) {
            addTile((Tile) input[2]);
        }
    }

    private void callSet(Object[] input) {
        if (input[2] instanceof String) {
            switch ((String) input[2]) {
                case "index":
                    if (input[3] instanceof Integer && input[4] instanceof Integer && input[5] instanceof Integer) {
                        setTileToMap((Integer) input[3], (Integer) input[4], (Integer) input[5]);
                    }
                    break;
                case "tile":
                    if (input[3] instanceof Tile && input[4] instanceof Integer && input[5] instanceof Integer) {
                        setTileToMap((Tile) input[3], (Integer) input[4], (Integer) input[5]);
                    }
                    break;
            }
        }
    }

    private void callRemove(Object[] input) {
        if (input[2] instanceof String) {
            switch ((String) input[2]) {
                case "map":
                    if (input[3] instanceof Integer && input[4] instanceof Integer) {
                        removeTileFromMap((Integer) input[3], (Integer) input[4]);
                    }
                    break;
                case "array":
                    if (input[3] instanceof Tile) {
                        removeTileFromTileArray((Tile) input[3]);
                    }
                    if (input[3] instanceof Integer) {
                        removeTileFromTileArray((Integer) input[3]);
                    }
                    break;
            }
        }
    }

    @Override
    public void loadTexture() {
        if (isTextureLoaded())
            return;
        BufferedImage[] builder = new BufferedImage[textureKeys.length];
        for (int i = 0; i < textureKeys.length; i++) {
            if (!getParent().getResourceHandler().isAvailable(textureKeys[i]) ||
                    getParent().getResourceHandler().getTexture(textureKeys[i]) == null) {
                if (!getParent().getResourceHandler().getLoadingQueue().contains(textureKeys[i])) {
                    getParent().getResourceHandler().requestTexture(textureKeys[i]);

                }
                return;
            }
            builder[i] = getParent().getResourceHandler().getTexture(textureKeys[i]);
        }
        tileSets = builder;
        setTextureLoaded(true);
    }

    public Dimension getDefaultTileDimension() {
        return defaultTileDimension;
    }

    @Override
    public char update(float elapsedTime) {
        super.update(elapsedTime);
        if (playerPos == null) {
            getParent().dispatch(ChannelID.PLAYER, new Object[]{"playerPos", getID(), ChannelID.LOGIC});
        }

        return 0;
    }

    @Override
    protected void drawFrame(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        if (currStage == ChannelID.BACKGROUND) {
            drawMap(g, elapsedTime, camera);
        } else if (currStage == ChannelID.UI) {
            drawPlayerCover(g, elapsedTime, camera);
        }
    }

    private boolean isHalfRes = false;

    private void drawPlayerCover(Graphics g, float elapsedTime, Camera camera) {
        if (playerPos == null)
            return;
        if(collisionMap == null){
            getParent().dispatch(ChannelID.SCENE_2, "g.dflt.TileMap:Collision", new Object[]{ "getCollisionArea", getID()});
        } else {
            int tileWidth = defaultTileDimension.width;
            int tileHeight = defaultTileDimension.height;
            int playerX = playerPos.x - pos.x;
            int playerY = playerPos.y - Math.round(0.375f * tileHeight) - pos.y;
            int playerWidth = 32;
            int playerHeight = 64;
            int zoomedTileWidth = camera.applyZoom(tileWidth);
            int zoomedTileHeight = camera.applyZoom(tileHeight);


            int startX ,
                    endX,
                    startY,
                    endY;
            if(isHalfRes) {
                int tile2Width = tileWidth * 2;
                int tile2Height = tileHeight * 2;
                startX  = Math.max(0, (playerX / tile2Width) );
                endX = (playerX + 2 * playerWidth) / tile2Width;
                startY = (playerY + 2 * playerHeight) / tile2Height ;
                endY = Math.max(0, (playerY + tile2Height) / tile2Height);
                startX *= 2;
                endX *= 2;
                startY = startY * 2 - 1;
                endY = endY * 2 - 2;
            } else {
                startX  = Math.max(0, (playerX / tileWidth) );
                endX = (playerX + 2 * playerWidth) / tileWidth;
                startY = (playerY + 2 * playerHeight) / tileHeight - 1;
                endY = Math.max(0, (playerY + tileHeight) / tileHeight);
            }


            Graphics2D g2d = (Graphics2D) g;
            Composite c = g2d.getComposite();
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.95f);
            g2d.setComposite(ac);

//            for (int x = Math.max(0, playerX / tileWidth); x < tileMap.length && x < (playerX + 2 * playerWidth) / tileWidth; x++) {
//                for (int y = Math.max(0, (playerY + tileHeight) / tileHeight); y < tileMap[x].length && y < (playerY + 2 * playerHeight) / tileWidth; y++) {
            boolean behindWall = Collision.doesWallOverlap(
                    collisionMap,
                    mapPos_and_metaData,
                    new Point(playerPos.x,playerPos.y + 48),
                    new Dimension(playerWidth - 2,playerHeight / 2 - 16));

            int skip = 0;

            for (int x = startX; x < tileMap.length && x < endX ; x++) {
                for (int y = startY ; y < tileMap[x].length && y >= endY; y--) {
                    if(skip > 0){
                        skip --;
                        continue;
                    }

                    //debugging
                    if(Main.DEBUG) {
                        g.setColor(Color.GREEN);
                        g.drawRect(camera.applyXTransform(pos.x + x * defaultTileDimension.width),
                                camera.applyYTransform(pos.y + y * defaultTileDimension.height), camera.applyZoom(tileWidth), camera.applyZoom(tileHeight));
                    }
                    //end of debugging

                    if( playerY + 60 > y * tileHeight){
                        //debugging
                        g.setColor(Color.BLUE);
                        if(Main.DEBUG) {
                            g.drawString("y=" + y,camera.applyXTransform(pos.x + x * defaultTileDimension.width),
                                    camera.applyYTransform(pos.y + y * defaultTileDimension.height) );
                            g.setColor(Color.GREEN);
                            g.drawRect(camera.applyXTransform(pos.x + x * defaultTileDimension.width),
                                    camera.applyYTransform(pos.y + y * defaultTileDimension.height), camera.applyZoom(tileWidth), camera.applyZoom(tileHeight));
                        }
                        //end of debugging
                        if( Collision.doesCollisionOccur(collisionMap, mapPos_and_metaData, new Point(x * tileWidth + pos.x, y * tileHeight + pos.y), defaultTileDimension)) {
                            if(useConnectedStrategy)
                                break;
                            else if( !behindWall)
                                continue;
                        } else if(isHalfRes && Collision.doesDoorOverlap(collisionMap, mapPos_and_metaData, new Point(x * tileWidth + pos.x, y * tileHeight + pos.y), defaultTileDimension)){
                            skip = 1;
                            continue;
                        }
                    }
                    // switched with if statement above because of debbuging, worse for performace
                    if ( useConnectedStrategy && !Collision.doesOverlapOccur(collisionMap, mapPos_and_metaData, new Point(x * tileWidth + pos.x, y * tileHeight + pos.y), defaultTileDimension))
                        continue;

                    if (tileMap[x][y] < 0) {
                        continue;
                    }
                    if (!isTextureLoaded()) {
                        continue;
                    }



                    g2d.drawImage(tiles[tileMap[x][y]].getTexture(this, zoomedTileWidth, zoomedTileHeight),
                            camera.applyXTransform(pos.x + x * defaultTileDimension.width),
                            camera.applyYTransform(pos.y + y * defaultTileDimension.height),
                            null);


                }
            }
            g2d.setComposite(c);
        }
    }

    private void drawMap(Graphics g, float elapsedTime, Camera camera) {
        int[] bounds = camera.getRevertedBounds();


        int tileWidth = camera.applyZoom(defaultTileDimension.width);
        int tileHeight = camera.applyZoom(defaultTileDimension.height);
        for (int xCoord = Math.max(bounds[0] / defaultTileDimension.width, 0); xCoord < tileMap.length && xCoord * defaultTileDimension.width < bounds[2]; xCoord++) {
            for (int yCoord = Math.max(bounds[1] / defaultTileDimension.height, 0); yCoord < tileMap[xCoord].length && yCoord * defaultTileDimension.height < bounds[3]; yCoord++) {
                if (tileMap[xCoord][yCoord] != -1) {
                    /*g.drawRect(camera.applyXTransform(x + xCoord * defaultTileDimension.width),
                            camera.applyYTransform(y + yCoord * defaultTileDimension.height),
                            camera.applyZoom(defaultTileDimension.width),
                            camera.applyZoom(defaultTileDimension.height));*/
                    if (tileMap[xCoord][yCoord] < 0) {
                        tileMap[xCoord][yCoord] = -1;
                        continue;
                    }
                    if (!isTextureLoaded()) {
                        continue;
                    }
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.drawImage(tiles[tileMap[xCoord][yCoord]].getTexture(this, tileWidth, tileHeight),
                            camera.applyXTransform(x + xCoord * defaultTileDimension.width),
                            camera.applyYTransform(y + yCoord * defaultTileDimension.height), tileWidth, tileHeight,
                            null);
                }
            }
        }
    }
}