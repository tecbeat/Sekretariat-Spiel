package de.jspll.data.objects.game.player;

import de.jspll.Main;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.Animation;
import de.jspll.data.objects.TexturedObject;
import de.jspll.graphics.Camera;
import de.jspll.graphics.ResourceHandler;
import de.jspll.util.Collision;
import de.jspll.util.PaintingUtil;
import de.jspll.util.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Jonas Sperling, Lukas Becker, Philipp Polland
 * @version 1.0
 */

public class Player extends TexturedObject {


    //[0] = Forward (W), [1] = Backwards (S), [2] = Left(L), [3] = Right(R)
    private final ArrayList<Animation> movementAnimationList = new ArrayList<>();
    protected Point pos;
    //2d array representing map
    protected int[][] collisionMap;
    //int arr in form: [ mapX, mapY, mapWidth, mapHeight, tileWidth, tileHeight ]
    protected int[] mapPos_and_metaData;
    protected double referenceSpeed = 95f;
    HashMap<String, AtomicBoolean> keyMap;
    private ColorScheme colorScheme;
    private String lastPressedKey = "s";
    private boolean sprinted_last = false;
    private Dimension collision_Dim;
    private Vector2D velocity = new Vector2D(0, 0);


    public Player(String ID, String ObjectID, Point pos, Dimension dimension, ColorScheme colorScheme) {
        super(ID, ObjectID, pos.x, pos.y, dimension);
        this.pos = pos;
        this.colorScheme = colorScheme;
        channels = new ChannelID[]{ChannelID.PLAYER, ChannelID.LOGIC};
        this.collision_Dim = new Dimension(dimension.width - 2, dimension.height / 2 - 16);
        for (String s : new String[]{"forward", "backward", "left", "right", "idle0", "idle1", "idle2", "idle3"}) {
            movementAnimationList.add(new Animation("/assets/player_animation/" + colorScheme + "/" + s + "_", s.contains("idle") ? 1 : 6, pos, dimension, this, 1F));
        }
    }

    public Player() {

    }

    public ColorScheme getColorScheme() {
        return colorScheme;
    }

    /**
     * Check if all animations are loaded.
     *
     * @return true if all animations are loaded, else false
     * @see TexturedObject
     */
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

    /**
     * Get the {@code ResourceHandler} and request all {@code Textures} needed for the {@code Animation}.<br>
     * Additionally sets the Animation is looping.
     * @see ResourceHandler
     */
    @Override
    public void requestTexture() {
        for (Animation animation : movementAnimationList) {
            animation.requestTextures(this);
            animation.setLooping(true);
        }
    }

    /**
     * Every frame the {@code Player} is moved based on the inputs. <br>
     * The {@code Camera} is centred to the {@code Player}.
     *
     * @param elapsedTime delta time between frames
     * @return exit code - similar to program exit codes in Java/C
     */
    @Override
    public char update(float elapsedTime) {

        Camera c = getParent().getSelectedCamera();

        c.centerToObject(this, elapsedTime);

        // slow down after sprinting
        decayVelocity();

        updateVelocity();

        handleCollision(elapsedTime);

        assimilateXY();

        return 0;
    }

    /**
     * Updates superclass x- and y-coordinate to current position.
     */
    protected void assimilateXY() {
        this.x = pos.x;
        this.y = pos.y;
    }

    /**
     * Checks if the Player will collide with an object on the map.
     * If not the Position will be updated
     *
     * @param elapsedTime Time since last update call
     */
    protected void handleCollision(float elapsedTime) {
        Vector2D scaledVelocity = velocity.scale(elapsedTime);

        if (collisionMap != null) {
            //Collision
            Point newPos = new Point(pos);
            scaledVelocity.updatePos(newPos);

            if (doesCollisionOccur(newPos)) {
                //handle collision
                Vector2D[] vectors = scaledVelocity.splitIntoXY();
                Vector2D outVec = new Vector2D(0, 0);
                Point oldPos = new Point(pos);
                newPos = new Point(pos);

                // try moving in y-coordinate first
                vectors[1].updatePos(newPos);
                if (doesCollisionOccur(newPos)) {
                    newPos = oldPos;
                } else {
                    outVec.setY(vectors[1].getY());
                }

                // if no collision keep new pos
                // try moving in x-coordinate based on new or old coordinate
                vectors[0].updatePos(newPos);
                if (!doesCollisionOccur(newPos)) {
                    outVec.setX(vectors[0].getX());
                }
                outVec.updatePos(pos);
            } else {
                scaledVelocity.updatePos(pos);
            }
        } else {
            getParent().dispatch(ChannelID.SCENE_GAME, "g.dflt.TileMap:Collision", new Object[]{"getCollisionArea", getID()});
            scaledVelocity.updatePos(pos);
        }
    }

    /**
     * After stopping the player gradually slows down.
     * If the player sprinted just before stopping the slowdown is more noticeable.
     *
     */
    protected void decayVelocity() {
        if (sprinted_last) {
            velocity.instanceScale(0.9d);
            if (velocity.euclideanDistance() < 1) {
                sprinted_last = false;
            }
        } else {
            velocity.instanceScale(0.1d);
        }
    }

    /**
     * Calculate movement velocity based on input.
     */
    protected void updateVelocity() {
        if (keyMap != null) {
            double speed = referenceSpeed;

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
                velocity.instanceNormalize();
                velocity.instanceScale(speed);
            } else {
                idleAnimation();
            }

        }
    }

    /**
     * Checks if the player is about to move into a wall. If so then return true, else false.
     * <p>DEBUG: If "Q" is pressed the {@code Player} can move trough walls.</p>
     * @param newPos Position of the Player
     * @return true if collision occurs, else false
     */
    private boolean doesCollisionOccur(Point newPos) {
        if (Main.DEBUG) {
            if (keyMap != null) {
                if (keyMap.get("q").get())
                    return false;
            }
        }

        // calculate hitbox origin
        Point collPos = new Point(newPos.x + 1, newPos.y + 32 + 16);

        // check for collision between hitbox and tilemap
        return Collision.doesCollisionOccur(collisionMap, mapPos_and_metaData, collPos, collision_Dim);
    }

    /**
     * Draw all movement animations.
     *
     * <p> DEBUG: Display velocity vector as circle around player. <br> Draw a grid of 16x16 blocks with positions inside the {@code TileMap}</p>
     * @param g Graphics to draw
     * @param elapsedTime delta time between frames
     * @param camera selected Camera
     * @param currStage current active ChannelID
     * @see Animation
     */

    @Override
    protected void drawFrame(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {

        for (Animation animation : movementAnimationList) {
            animation.draw((Graphics2D) g, elapsedTime, camera);
        }

        //debugging
        if (!Main.DEBUG) {
            return;
        }
        Point t = new Point(pos);
        velocity.updatePos(t);
        g.setColor(Color.MAGENTA);
        g.drawLine(camera.applyXTransform(pos.x), camera.applyYTransform(pos.y), camera.applyXTransform(t.x), camera.applyYTransform(t.y));
        //Logger.d.add("vector len=" + velocity.euclideanDistance());
        if (!sprinted_last) {
            PaintingUtil.paintCircleFromCenter(camera.applyXTransform(pos.x), camera.applyYTransform(pos.y), camera.applyZoom(95), (Graphics2D) g, false);
        } else {
            PaintingUtil.paintCircleFromCenter(camera.applyXTransform(pos.x), camera.applyYTransform(pos.y), camera.applyZoom(95) * 1.7, (Graphics2D) g, false);
        }

        if (keyMap != null) {
            if (keyMap.get("q").get()) {
                g.setColor(Color.red);
            }
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
                        g.setColor(Color.RED);
                        g.drawRect(camera.applyXTransform(mapPos_and_metaData[0] + x * mapPos_and_metaData[4]),
                                camera.applyYTransform(mapPos_and_metaData[1] + y * mapPos_and_metaData[5]),
                                camera.applyZoom(mapPos_and_metaData[4]),
                                camera.applyZoom(mapPos_and_metaData[5]));
                    }
                    if (collisionMap[x][y] == 1) {
                        g.setColor(Color.CYAN);
                        g.drawRect(camera.applyXTransform(mapPos_and_metaData[0] + x * mapPos_and_metaData[4]),
                                camera.applyYTransform(mapPos_and_metaData[1] + y * mapPos_and_metaData[5]),
                                camera.applyZoom(mapPos_and_metaData[4]),
                                camera.applyZoom(mapPos_and_metaData[5]));
                    }
                }
            }
        }
    }


    /**
     * Implement how to response when {@code Player} is getting called. <br>
     *  1. The {@code collisionMap} and {@code mapPos_and_metaData} get transmitted to the {@code Player}. <br>
     *  2. Another {@code GameObject} requests for the current player position {@code pos}.  Send it back to them.
     *  3. Another {@code GameObject} requests the {@code Player} as an Object.
     *
     * @param input Array of Objects
     * @return exit code - similar to program exit codes in Java/C
     */
    @Override
    public char call(Object[] input) {
        if (input == null || input.length < 1) {
            return 0;
        } else if (input[0] instanceof String) {
            String cmd = (String) input[0];
            if (cmd.contentEquals("collision") && input[1] instanceof int[][] && input[2] instanceof int[]) {
                // [ "collision", collisionMap, mapPos_and_Metadata ]
                collisionMap = (int[][]) input[1];
                mapPos_and_metaData = (int[]) input[2];

            } else if (cmd.contentEquals("playerPos")) { // [ "playerPos", scope ]
                if (input[1] instanceof String) {
                    String scope = (String) input[1];
                    ChannelID targetChannel = (ChannelID) input[2];
                    getParent().dispatch(targetChannel, scope, new Object[]{"playerPos", pos, dimension});
                    //sends ["playerPos", pos, dimension] to scope
                }
            } else if (cmd.contentEquals("playerObject")) { // [ "playerObject" ]
                ChannelID targetChannel = (ChannelID) input[1];
                getParent().dispatch(targetChannel, new Object[]{"playerObj", this});
            }

        }

        return 0;
    }

    /**
     * Iterate over all {@code Animation} in {@code movementAnimationList} and stop there Animation.
     * @see Animation
     */
    public void stopAllAnimation() {
        for (Animation animation : movementAnimationList) {
            animation.stopAnimation();
        }
    }

    /**
     * Is called when no key ("W", "A", "S", "D") is pressed.
     * Based on the last pressed Key the idle state will start.
     */
    public void idleAnimation() {
        stopAllAnimation();

        switch (lastPressedKey) {
            case "w":
                movementAnimationList.get(4).startAnimation(false);
                break;
            case "s":
                movementAnimationList.get(5).startAnimation(false);
                break;
            case "a":
                movementAnimationList.get(6).startAnimation(false);
                break;
            case "d":
                movementAnimationList.get(7).startAnimation(false);
                break;
        }
    }

    /**
     * Is called when the "W" key was pressed. <br>
     * Start the corresponding movement animation. <br>
     * Set {@code lastPressedKey} to "w".
     */
    public void moveForward() {
        stopAllAnimation();
        movementAnimationList.get(0).startAnimation(false);
        lastPressedKey = "w";
    }

    /**
     * Is called when the "S" key was pressed. <br>
     * Start the corresponding movement animation. <br>
     * Set {@code lastPressedKey} to "s".
     */
    public void moveBackward() {
        stopAllAnimation();
        movementAnimationList.get(1).startAnimation(false);
        lastPressedKey = "s";
    }

    /**
     * Is called when the "A" key was pressed. <br>
     * Start the corresponding movement animation. <br>
     * Set {@code lastPressedKey} to "a".
     */
    public void moveLeft() {
        stopAllAnimation();
        movementAnimationList.get(2).startAnimation(false);
        lastPressedKey = "a";
    }

    /**
     * Is called when the "D" key was pressed. <br>
     * Start the corresponding movement animation. <br>
     * Set {@code lastPressedKey} to "d".
     */
    public void moveRight() {
        stopAllAnimation();
        movementAnimationList.get(3).startAnimation(false);
        lastPressedKey = "d";
    }

    /**
     * Update the {@code keyMap} and the Positions of all {@code Animation} in {@code movementAnimationList}.
     */
    @Override
    public void updateReferences() {
        keyMap = getParent().getLogicHandler().getInputHandler().getKeyMap();
        for (Animation a : movementAnimationList) a.setPos(pos);
    }
}

