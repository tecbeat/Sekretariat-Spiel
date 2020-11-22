package de.jspll.data.objects.game.tasks;

import de.jspll.Main;
import de.jspll.data.*;
import de.jspll.data.objects.GameObject;
import de.jspll.graphics.Camera;
import de.jspll.util.Vector2D;

import java.awt.*;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by reclinarka on 21-Nov-20.
 */
public class TaskHolder extends GameObject {

    private Point playerPos;
    private Dimension playerDim;
    private Point pos;
    private Task task;
    private boolean inProximity = false;
    private HashMap<String,AtomicBoolean> keyMap;

    public TaskHolder(String ID, String objectID, Point pos, Dimension dimension, Task task) {
        super(ID, objectID, pos.x, pos.y, dimension);
        this.channels = new ChannelID[]{ChannelID.UI, ChannelID.LOGIC};
        this.pos = pos;
        this.task = task;
        if (task != null)
            task.setHolder(this);
    }

    @Override
    public char update(float elapsedTime) {
        super.update(elapsedTime);

        if(task != null && task.isActive()){
            task.update(elapsedTime);
            return 0;
        }

        if (playerPos == null || playerDim == null) {
            requestPlayerPosAndSize();
        }
        if(keyMap == null && getParent() != null){
            keyMap = getParent().getLogicHandler().getInputHandler().getKeyMap();
        }
        inProximity = isInProximity();

        if(inProximity){
            if(keyMap.get("e").get() && task != null){
                task.activate();
            }
        }
        return 0;
    }

    private boolean isInProximity() {
        if(playerPos == null || playerDim == null)
            return false;
        Vector2D distanceToPlayer = new Vector2D(
                new Point(pos.x + dimension.width / 2, pos.y + dimension.height / 2),
                new Point(playerPos.x + playerDim.width / 2, playerPos.y + dimension.height / 2));

        if (distanceToPlayer.euclideanDistance() < 24) {
            return true;
        }
        return false;
    }

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
            } else if(cmd.contentEquals("toTask")){
                task.call(input);
                return 0;
            }
        }
        return 0;
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        super.paint(g, elapsedTime, camera, currStage);

        if (task != null) {
            if (currStage == ChannelID.UI && task.isActive()) {
                task.paint(g, elapsedTime, camera, currStage);
                return;
            }
        }

        if (Main.DEBUG) {
            if (inProximity) {
                g.setColor(Color.CYAN);
                g.fillRect(camera.applyXTransform(pos.x), camera.applyYTransform(pos.y),
                        camera.applyZoom(dimension.width), camera.applyZoom(dimension.height));
            }
        }
    }
}
