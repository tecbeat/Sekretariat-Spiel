package de.jspll.data.objects.game.tasks;

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
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Jonas Sperling, Lukas Becker, Philipp Polland
 * @version 1.0
 */
public class TaskHolder extends TexturedObject {

    private int DURATION = 120;
    private boolean firstUpdate = true;
    private float initTime = 0;
    private boolean active = true;

    private final Animation taskIndicationArrow;
    private Point playerPos;
    private Dimension playerDim;
    private Point pos;
    private final Task task;
    private final double radius;
    private boolean inProximity = false;
    private HashMap<String, AtomicBoolean> keyMap;
    private BufferedImage texture;
    private final int ArrowOffset = 30;
    private boolean showBox = true;
    private boolean canEnd = true;

    public TaskHolder(String ID, String objectID, Point pos, Dimension dimension, Task task, double radius) {
        super(ID, objectID, pos.x, pos.y, dimension);
        this.channels = new ChannelID[]{ChannelID.LOGIC, ChannelID.OVERLAY, ChannelID.UI};
        this.pos = pos;
        this.task = task;
        this.radius = radius;
        if (task != null)
            task.setHolder(this);
        taskIndicationArrow = new Animation("/assets/task/indication_arrow_", 20,
                new Point(this.pos.x, this.pos.y - ArrowOffset), new Dimension(32, 32), this, 3F);
    }

    public TaskHolder(String ID, String objectID, Point pos, Dimension dimension, Task task, double radius, boolean showBox) {
        super(ID, objectID, pos.x, pos.y, dimension);
        this.showBox = showBox;
        this.channels = new ChannelID[]{ChannelID.LOGIC, ChannelID.OVERLAY, ChannelID.UI};
        this.pos = pos;
        this.task = task;
        this.radius = radius;
        if (task != null)
            task.setHolder(this);
        taskIndicationArrow = new Animation("/assets/task/indication_arrow_", 20, new Point(this.pos.x, this.pos.y - ArrowOffset), new Dimension(32, 32), this, 3F);
    }

    public TaskHolder(String ID, String objectID, Point pos, Dimension dimension, Task task, double radius, boolean showBox, boolean canEnd) {
        super(ID, objectID, pos.x, pos.y, dimension);
        this.channels = new ChannelID[]{ChannelID.LOGIC, ChannelID.OVERLAY, ChannelID.UI};
        this.pos = pos;
        this.showBox = showBox;
        this.task = task;
        this.radius = radius;
        if (task != null)
            task.setHolder(this);
        taskIndicationArrow = new Animation("/assets/task/indication_arrow_", 20,
                new Point(this.pos.x, this.pos.y - ArrowOffset), new Dimension(32, 32), this, 3F);

        this.canEnd = canEnd;
    }


    /**
     * Get the {@code ResourceHandler} and request all {@code Textures} needed for the {@code Animation}.<br>
     * Additionally sets animation loop to true and starts the {@code Animation}.
     * @see ResourceHandler
     */
    @Override
    public void requestTexture() {
        taskIndicationArrow.requestTextures(this);
        taskIndicationArrow.setLooping(true);
        taskIndicationArrow.startAnimation();
        task.requestTexture();
    }

    /**
     * Check if the arrow animations are loaded.
     *
     * @return true if all animations are loaded, else false
     * @see TexturedObject
     */
    @Override
    public boolean isTextureLoaded() {
        if (!taskIndicationArrow.isLoaded()) {
            taskIndicationArrow.loadTextures();
            return false;
        }
        if (!task.isLoaded()) {
            task.loadTextures();
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TaskHolder{" +
                "task=" + task +
                ", inProximity=" + inProximity +
                ", ID='" + ID + '\'' +
                "} " + super.toString();
    }

    /**
     * Draw the Indication Arrow
     *
     * @param g           Graphics for drawing
     * @param elapsedTime delta time between frames
     * @param camera      selected Camera
     * @param currStage   current active ChannelID
     */
    @Override
    protected void drawFrame(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        taskIndicationArrow.draw((Graphics2D) g, elapsedTime, camera);
    }

    /**
     * Call update from the {@code task}. <br>
     * Request the {@code playerPos} and {@code playerDim} from the {@code Player}. <br>
     *
     * @param elapsedTime delta time between frames
     * @return exit code - similar to program exit codes in Java/C
     */
    @Override
    public char update(float elapsedTime) {

        if(firstUpdate)
            DURATION = Math.round((float)60 / ((float)(getParent().getGameManager().getLevel()) * 0.5f));

        if (task != null && task.isActive()) {
            task.update(elapsedTime);
            return 0;
        }

        if (playerPos == null || playerDim == null) {
            requestPlayerPosAndSize();
        }
        if (keyMap == null && getParent() != null) {
            keyMap = getParent().getLogicHandler().getInputHandler().getKeyMap();
        }
        inProximity = isInProximity();

        if (inProximity) {
            if (keyMap.get("e").get() && task != null) {
                task.activate();
            }
        }
        taskIndicationArrow.setPos(new Point(pos.x, pos.y - ArrowOffset));
        if(initTime == 0){
            initTime = getParent().getGameManager().getRemainingTime();
        }
        if(canEnd && (initTime - DURATION) > getParent().getGameManager().getRemainingTime() && active){
            task.deactivate();
            active = false;
            if(!(task instanceof NPCTask))
                getParent().getGameManager().removeTaskFromActiveList();
        }

        return 0;
    }

    /**
     * @return true if player is within the {@code radius} of the task, else false
     */
    private boolean isInProximity() {
        if (playerPos == null || playerDim == null)
            return false;
        Vector2D distanceToPlayer = new Vector2D(
                new Point(pos.x + dimension.width / 2, pos.y + dimension.height / 2),
                new Point((playerPos.x + playerDim.width / 2), (playerPos.y + playerDim.height / 2) + 24));

        return distanceToPlayer.euclideanDistance() < radius;
    }

    /**
     * Implement how to response when {@code TaskHolder} is getting called. <br>
     *  1. The current {@code palyerPos} and {@code playerDim} are transmitted to {@code TaskHolder}. <br>
     *  2. Forward the call to the {@code task}. <br>
     *  3. Another {@code GameObject} requests the active task.
     *  4. Forward the inputs to the {@code task}. <br>
     *
     * @param input Array of Objects
     * @return exit code - similar to program exit codes in Java/C
     */
    @Override
    public char call(Object[] input) {
        super.call(input);

        if (input == null || input.length < 1) {
            return 0;
        } else if (input[0] instanceof String) {
            String cmd = (String) input[0];
            if (cmd.contentEquals("playerPos")) {
                playerPos = (Point) input[1];
                playerDim = (Dimension) input[2];
            } else if (cmd.contentEquals("toTask")) {
                task.call(input);
                return 0;
            } else if (cmd.contentEquals("getTask") && active){
                getParent().dispatch(ChannelID.INPUT, new Object[]{"activeTask", task.getName(), (canEnd ? initTime - DURATION : 0)});
            }
            if (((String) input[0]).contentEquals("input")) {
                task.call(input);
            }
        }
        return 0;
    }

    /**
     * Paint the Task if active.
     * Paint an Arrow showing the direction to the task.
     *
     * @param g           Graphics for drawing
     * @param elapsedTime delta time between frames
     * @param camera      selected Camera
     * @param currStage   current active ChannelID
     */
    private void paintOverlay(Graphics g, float elapsedTime, Camera camera, ChannelID currStage){
        if (task != null) {
            if (task.isActive()) {
                if(!task.isLoaded())
                    task.loadTextures();
                task.paint(g, elapsedTime, camera, currStage);
                return;
            }
            boolean inProximity = isInProximity();
            Graphics2D g2d = (Graphics2D) g;
            Stroke s = null;
            if (inProximity) {
                s = g2d.getStroke();
                g2d.setStroke(new BasicStroke(3));
            }
            if(showBox) {
                g2d.setColor(Color.RED);
                g2d.drawRect(camera.applyXTransform(pos.x),
                        camera.applyYTransform(pos.y),
                        camera.applyZoom(dimension.width),
                        camera.applyZoom(dimension.height));
                if (inProximity && s != null)
                    g2d.setStroke(s);
            }
        }

        if (Main.DEBUG) {
            if (inProximity && showBox) {
                g.setColor(Color.CYAN);
                g.fillRect(camera.applyXTransform(pos.x), camera.applyYTransform(pos.y),
                        camera.applyZoom(dimension.width), camera.applyZoom(dimension.height));
            }
        }
    }

    private void paintUI(Graphics g, float elapsedTime, Camera camera, ChannelID currStage){
        if(task != null && !inProximity){
            //Arrow Drawing:
            if (playerPos != null && playerDim != null) {
                Graphics2D g2d = (Graphics2D) g;
                //Defining points in gamespace
                Point playerCenter = new Point((playerPos.x + playerDim.width / 2), (playerPos.y + playerDim.height / 2) + 24);
                Point taskHolderCenter = new Point(pos.x + dimension.width / 2, pos.y + dimension.height / 2);
                //convert to screenspace
                playerCenter.setLocation(camera.applyXTransform(playerCenter.x),camera.applyYTransform(playerCenter.y));
                taskHolderCenter.setLocation(camera.applyXTransform(taskHolderCenter.x),camera.applyYTransform(taskHolderCenter.y));
                //get vec player -> Task (screenspace)
                Vector2D distanceToPlayer = new Vector2D( playerCenter, taskHolderCenter );
                Point middle = new Point(playerCenter), destination = new Point(playerCenter);
                distanceToPlayer.updatePos(destination);

                if(distanceToPlayer.euclideanDistance() > camera.getHeight()) {

                    Point rect1 = new Point(20,20);
                    int rec1Width = camera.getWidth() - 40, rec1Height = camera.getHeight() - 40;
                    Point coll1 = Collision.findLineRectIntersection(playerCenter,taskHolderCenter,rect1,rec1Width,rec1Height);

                    Point rect2 = new Point(100,100);
                    int rec2Width = camera.getWidth() - 200, rec2Height = camera.getHeight() - 200;
                    Point coll2 = Collision.findLineRectIntersection(playerCenter,taskHolderCenter,rect2,rec2Width,rec2Height);


                    PaintingUtil.paintArrow(g2d, coll2, coll1, Color.BLACK, Color.RED,100);
                }
                if(Main.DEBUG){
                    g.setColor(Color.CYAN);
                    g.drawLine(playerCenter.x, playerCenter.y,
                            taskHolderCenter.x, taskHolderCenter.y);
                }

            }
        }
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        if(!active) {
            return;
        }

        super.paint(g, elapsedTime, camera, currStage);

        if(currStage.valueOf() == ChannelID.OVERLAY.valueOf()){
            paintOverlay(g, elapsedTime, camera, currStage);
        } else if(currStage.valueOf() == ChannelID.UI.valueOf()){
            paintUI(g, elapsedTime, camera, currStage);
        }
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public Point getPos() {
        return pos;
    }

    public Task getTask() {
        return task;
    }
}
