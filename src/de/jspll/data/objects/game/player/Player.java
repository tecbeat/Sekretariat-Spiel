package de.jspll.data.objects.game.player;

import de.jspll.Main;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.Animation;
import de.jspll.data.objects.TexturedObject;
import de.jspll.graphics.Camera;
import de.jspll.util.Collision;
import de.jspll.util.PaintingUtil;
import de.jspll.util.Vector2D;


import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.jspll.data.ChannelID.*;

/**
 * Created by reclinarka on 23-Oct-20.
 */
public class Player extends TexturedObject {


    private int colorScheme;   //Possible 0 - 4

    //[0] = Forward (W), [1] = Backwards (S), [2] = Left(L), [3] = Right(R)
    private final ArrayList<Animation> movementAnimationList = new ArrayList<>();

    private String lastPressedKey = "s";
    private boolean start = true;
    private Point pos;
    private float continuous_walking_time = 0f;
    private boolean sprinted_last = false;

    //2d array representing map
    private int[][] collisionMap;
    //int arr in form: [ mapX, mapY, mapWidth, mapHeight, tileWidth, tileHeight ]
    private int[] mapPos_and_metaData;

    Point halfResolution;

    private Vector2D velocity = new Vector2D(0, 0);

    HashMap<String, AtomicBoolean> keyMap;

    public Player(String ID, Point pos, Dimension dimension, int colorScheme) {
        super(ID, "g.ntt.OwnPlayer", pos.x, pos.y, dimension);
        this.pos = pos;
        this.colorScheme = colorScheme;
        channels = new ChannelID[]{ChannelID.PLAYER, ChannelID.LOGIC};

        movementAnimationList.add(new Animation("assets\\player_animation\\" + colorScheme + "\\forward_", 6, pos, dimension, this, 1F));
        movementAnimationList.add(new Animation("assets\\player_animation\\" + colorScheme + "\\backward_", 6, pos, dimension, this, 1F));
        movementAnimationList.add(new Animation("assets\\player_animation\\" + colorScheme + "\\left_", 6, pos, dimension, this, 1F));
        movementAnimationList.add(new Animation("assets\\player_animation\\" + colorScheme + "\\right_", 6, pos, dimension, this, 1F));
        movementAnimationList.add(new Animation("assets\\player_animation\\" + colorScheme + "\\idle0_", 1, pos, dimension, this, 1F));
        movementAnimationList.add(new Animation("assets\\player_animation\\" + colorScheme + "\\idle1_", 1, pos, dimension, this, 1F));
        movementAnimationList.add(new Animation("assets\\player_animation\\" + colorScheme + "\\idle2_", 1, pos, dimension, this, 1F));
        movementAnimationList.add(new Animation("assets\\player_animation\\" + colorScheme + "\\idle3_", 1, pos, dimension, this, 1F));


    }

    public Player() {
    }


    public int getColorScheme() {
        return colorScheme;
    }

    @Override
    public boolean isTextureLoaded() {
        for (Animation an : movementAnimationList) {
            if (!an.isLoaded()) {
                an.loadTextures();
                return false;
            }
        }
        return true;
    }

    @Override
    public void requestTexture() {
        for (Animation animation : movementAnimationList) {
            animation.setLooping(true);
            animation.requestTextures(this);
        }
    }


    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        super.paint(g, elapsedTime, camera, currStage);
    }


    @Override
    public char update(float elapsedTime) {
        super.update(elapsedTime);

        Camera c = getParent().getSelectedCamera();

        c.centerToObject(this);

        decayVelocity();

        updateVelocity();

        handleCollision(elapsedTime);

        //maybe move the following code block to different method

        assimilateXY();

        return super.update(elapsedTime);
    }

    private void assimilateXY() {
        this.x = pos.x;
        this.y = pos.y;
    }

    private void handleCollision(float elapsedTime) {
        Vector2D scaledVelocity = velocity.scale(elapsedTime);

        if (collisionMap != null) {
            //Collision


            Point newPos = new Point(pos);
            scaledVelocity.updatePos(newPos);

            if (doesCollisionOccur(newPos)) {
                //Logger.d.add("Collission occured");
                //handle collision
                Vector2D[] vectors = scaledVelocity.splitIntoXY();
                Vector2D outVec = new Vector2D(0, 0);
                Point oldPos = new Point(pos);
                newPos = new Point(pos);
                vectors[1].updatePos(newPos);
                if (doesCollisionOccur(newPos)) {
                    newPos = oldPos;
                } else {
                    outVec.setY(vectors[1].getY());
                }
                oldPos = new Point(newPos);
                vectors[0].updatePos(newPos);
                if (doesCollisionOccur(newPos)) {
                    newPos = oldPos;
                } else {
                    outVec.setX(vectors[0].getX());
                }
                outVec.updatePos(pos);

            } else {
                scaledVelocity.updatePos(pos);

            }


        } else {
            getParent().dispatch(ChannelID.SCENE_2, "g.dflt.TileMap:Collision", new Object[]{"player", "getCollisionArea"});
            scaledVelocity.updatePos(pos);
        }
    }

    private void decayVelocity() {
        if (sprinted_last) {
            velocity.instanceScale(0.9d);
            if (velocity.euclideanDistance() < 1) {
                sprinted_last = false;
            }
        } else {
            velocity.instanceScale(0d);
        }
    }

    private void updateVelocity() {
        if (keyMap != null) {
            double speed = 95d;

            boolean w = keyMap.get("w").get();
            boolean a = keyMap.get("a").get();
            boolean s = keyMap.get("s").get();
            boolean d = keyMap.get("d").get();
            boolean shift = keyMap.get("SHIFT").get();

            if (shift) {
                sprinted_last = true;
                speed *= 1.7;
                for (Animation animation : movementAnimationList) {
                    animation.updateDuration(0.5f);
                }
            } else {
                for (Animation animation : movementAnimationList) {
                    animation.updateDuration(1f);
                }
            }

            if (w || a || s || d) {
                if (!shift) {
                    sprinted_last = false;
                }
                velocity.setY(0);
                velocity.setX(0);
                if (w && s || a && d) {
                    idleAnimation();
                }

                if (w) {
                    velocity.addToY(-1);
                }
                if (a) {
                    velocity.addToX(-1);
                }
                if (s) {
                    velocity.addToY(1);
                }
                if (d) {
                    velocity.addToX(1);
                }

                if (w && a) {
                    moveLeft();
                } else if (w && d) {
                    moveRight();
                } else if (s && a) {
                    moveBackward();
                } else if (s && d) {
                    moveBackward();
                } else if (w) {
                    moveForward();
                } else if (a) {
                    moveLeft();
                } else if (s) {
                    moveBackward();
                } else if (d) {
                    moveRight();
                }
                velocity.normalize();
                velocity.instanceScale(speed);
            } else {
                idleAnimation();
            }

        }
    }

    private boolean doesCollisionOccur(Point newPos) {
        if (!Main.DEBUG || Main.DEBUG) {
            if (keyMap != null) {
                if (keyMap.get("q").get())
                    return false;
            }
        }


        int mapX = mapPos_and_metaData[0],
                mapY = mapPos_and_metaData[1],
                mapWidth = mapPos_and_metaData[2],
                mapHeight = mapPos_and_metaData[3],
                playerWidth = dimension.width - 2,
                playerHeight = dimension.height / 2 - 16;
        if (!(
                pos.x + playerWidth < mapX || pos.x > mapWidth + mapX ||
                        pos.y + playerHeight < mapY || pos.y > mapY + mapHeight ||
                        newPos.x + playerWidth < mapX || newPos.x > mapWidth + mapX ||
                        newPos.y + playerHeight < mapY || newPos.y > mapY + mapHeight
        )) {
            int tileWidth = mapPos_and_metaData[4],
                    tileHeight = mapPos_and_metaData[5],
                    relX = newPos.x + 1 - mapX,
                    relY = (newPos.y + 32 + 16) - mapY,
                    leftTile = relX / tileWidth,
                    rightTile = (relX + playerWidth) / tileWidth,
                    topTile = relY / tileHeight,
                    bottomTile = (relY + playerHeight) / tileHeight;
            //collisionMap[x][y] == 0 -> not passable;

            int[] newPlayerPos = new int[]{relX,
                    relY,
                    playerWidth,
                    playerHeight};
            for (int x = leftTile; x <= rightTile; x++) {
                for (int y = topTile; y <= bottomTile; y++) {
                    if (collisionMap[x][y] == 0) {
                        if (Collision.doesRectCollide(newPlayerPos,
                                new int[]{mapX + x * tileWidth,
                                        mapY + y * tileHeight,
                                        tileWidth,
                                        tileHeight}))
                            return true;
                    }
                }
            }


            //Logger.d.add("playerTile x=" + playerTileX + " y=" + playerTileY);


        }
        return false;
    }

    @Override
    protected void drawFrame(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        for (Animation animation : movementAnimationList) {
            animation.draw((Graphics2D) g, elapsedTime, camera);
        }


        //debugging
        if (!Main.DEBUG)
            return;
        Point t = new Point(pos);
        velocity.updatePos(t);
        g.setColor(Color.MAGENTA);
        g.drawLine(camera.applyXTransform(pos.x), camera.applyYTransform(pos.y), camera.applyXTransform(t.x), camera.applyYTransform(t.y));
        //Logger.d.add("vector len=" + velocity.euclideanDistance());
        if (!sprinted_last)
            PaintingUtil.paintCircleFromCenter(camera.applyXTransform(pos.x), camera.applyYTransform(pos.y), camera.applyZoom(95), (Graphics2D) g, false);
        else
            PaintingUtil.paintCircleFromCenter(camera.applyXTransform(pos.x), camera.applyYTransform(pos.y), camera.applyZoom(95) * 1.7, (Graphics2D) g, false);


        if (keyMap != null) {
            if (keyMap.get("q").get())
                g.setColor(Color.red);
        }

        int playerX = pos.x + 1,
                playerY = pos.y + 32 + 16,
                playerWidth = dimension.width - 2,
                playerHeight = dimension.height / 2 - 16;
        g.drawRect(camera.applyXTransform(playerX), camera.applyYTransform(playerY), camera.applyZoom(playerWidth), camera.applyZoom(playerHeight));
        g.drawString("x=" + pos.x + " y=" + pos.y, camera.applyXTransform(pos.x), camera.applyYTransform(pos.y));



        if (collisionMap != null) {
            for (int x = 0; x < collisionMap.length; x++) {
                for (int y = 0; y < collisionMap[x].length; y++) {
                    if (collisionMap[x][y] == 0) {
                        g.drawRect(camera.applyXTransform(mapPos_and_metaData[0] + x * mapPos_and_metaData[4]),
                                camera.applyYTransform(mapPos_and_metaData[1] + y * mapPos_and_metaData[5]),
                                camera.applyZoom(mapPos_and_metaData[4]),
                                camera.applyZoom(mapPos_and_metaData[5]));
                    }
                }
            }
        }

    }

    @Override
    public char call(Object[] input) {
        HashMap<String, AtomicBoolean> keyMap;
        super.call(input);
        if (input == null || input.length < 1) {
            return 0;
        } else if (input[0] instanceof String) {
            String cmd = (String) input[0];
            if (cmd.contentEquals("collision") && input[1] instanceof int[][] && input[2] instanceof int[]) {
                // [ "collision", collisionMap, mapPos_and_Metadata ]
                collisionMap = (int[][]) input[1];
                mapPos_and_metaData = (int[]) input[2];

            } else if(cmd.contentEquals("playerPos")){ // [ "playerPos", scope ]
                if(input[1] instanceof String){
                    String scope = (String) input[1];
                    ChannelID targetChannel = (ChannelID) input[2];
                    getParent().dispatch(targetChannel,scope, new Object[]{"playerPos", pos, dimension});
                    //sends ["playerPos", pos, dimension] to scope
                }
            }
        }
        return 0;
    }

    public void stopAllAnimation() {
        for (Animation animation : movementAnimationList) {
            animation.stopAnimation();
        }
    }

    public void idleAnimation() {
        stopAllAnimation();

        switch (lastPressedKey) {
            case "w":
                movementAnimationList.get(4).startAnimation(start);
                break;
            case "s":
                movementAnimationList.get(5).startAnimation(start);
                break;
            case "a":
                movementAnimationList.get(6).startAnimation(start);
                break;
            case "d":
                movementAnimationList.get(7).startAnimation(start);
                break;
        }
    }


    public void moveForward() {
        stopAllAnimation();
        movementAnimationList.get(0).startAnimation(start);
        start = false;
        lastPressedKey = "w";
    }

    public void moveBackward() {
        stopAllAnimation();
        movementAnimationList.get(1).startAnimation(start);
        start = false;

        lastPressedKey = "s";
    }

    public void moveLeft() {
        stopAllAnimation();

        movementAnimationList.get(2).startAnimation(start);
        start = false;

        lastPressedKey = "a";
    }

    public void moveRight() {
        stopAllAnimation();

        movementAnimationList.get(3).startAnimation(start);
        start = false;

        lastPressedKey = "d";
    }

    @Override
    public void updateReferences() {
        super.updateReferences();
        keyMap = getParent().getLogicHandler().getInputHandler().getKeyMap();

        for (Animation a : movementAnimationList)
            a.setPos(pos);
    }




}
