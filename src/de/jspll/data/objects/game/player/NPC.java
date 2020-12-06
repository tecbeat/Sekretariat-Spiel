package de.jspll.data.objects.game.player;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.game.tasks.TaskHolder;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Jonas Sperling, Lukas Becker
 * @version 1.0
 */

public class NPC extends Player {


    private final String[] keyList = new String[]{"w", "a", "s", "d", "q", "SHIFT"};
    double sumTime = 0;
    Random randomGenerator = new Random(System.currentTimeMillis());
    int nextMovement = randomGenerator.nextInt(4);
    boolean firstTime = true;
    boolean taskActive = false;
    private TaskHolder task;
    private boolean posBroadcasted = false;

    public NPC(String ID, String ObjectID, Point NPCStartPos, ColorScheme colorScheme) {
        this(ID, ObjectID, NPCStartPos, colorScheme, null);
    }

    public NPC(String ID, String ObjectID, Point NPCStartPos, ColorScheme colorScheme, TaskHolder task) {
        super(ID, ObjectID, NPCStartPos, new Dimension(32, 64), colorScheme);
        referenceSpeed = 80f;
        resetKeyMap();
        this.task = task;
    }

    /**
     * Reset the KeyMap.
     * Default: all values are false
     */
    private void resetKeyMap() {
        super.keyMap = new HashMap<>();
        for (String s : keyList) {
            keyMap.put(s, new AtomicBoolean(false));
        }
    }

    @Override
    public char update(float elapsedTime) {

        if (firstTime) {
            getParent().loadTask(ChannelID.SCENE_GAME, task);
            firstTime = false;
        }

        task.setPos(new Point(super.pos.x, super.pos.y + 20));

        if (task != null) taskActive = task.getTask().isActive();

        if (sumTime > 2) {
            nextMovement = randomGenerator.nextInt(5);
            sumTime = 0;
            resetKeyMap();
        }
        if (taskActive) nextMovement = 4;

        switch (nextMovement) {
            case 0:
                keyMap.get("s").set(true);
                break;
            case 1:
                keyMap.get("w").set(true);
                break;
            case 2:
                keyMap.get("a").set(true);
                break;
            case 3:
                keyMap.get("d").set(true);
                break;
            default:
                break;
        }

        sumTime += elapsedTime;

        decayVelocity();

        updateVelocity();

        handleCollision(elapsedTime);

        assimilateXY();

        return 0;
    }


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
                    getParent().dispatch(targetChannel, scope, new Object[]{"npcPos" + getID(), pos, dimension});
                    posBroadcasted = true;
                    //sends ["npcPos", pos, dimension] to scope
                }
            }
        }


        return 0;

    }
}
