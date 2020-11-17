package de.jspll.data.objects.game.player;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.Animation;
import de.jspll.data.objects.TexturedObject;
import de.jspll.graphics.Camera;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by reclinarka on 23-Oct-20.
 */
public class Player extends TexturedObject {


    private int colorScheme;   //Possible 0 - 4

    //[0] = Forward (W), [1] = Backwards (S), [2] = Left(L), [3] = Right(R)
    private final ArrayList<Animation> movementAnimationList = new ArrayList<>();

    private String lastPressedKey = "";
    private boolean start = true;

    public Player(String ID, int x, int y, Dimension dimension, int colorScheme) {
        super(ID, "g.ntt.OwnPlayer", x, y, dimension);
        this.colorScheme = colorScheme;
        channels = new ChannelID[]{ChannelID.INPUT, ChannelID.PLAYER};

        movementAnimationList.add(new Animation("assets\\player_animation\\" + colorScheme + "\\forward_", 6, new Point(32 * colorScheme, 0), new Dimension(32, 64), this, 1F));
        movementAnimationList.add(new Animation("assets\\player_animation\\" + colorScheme + "\\backward_", 6, new Point(32 * colorScheme, 0), new Dimension(32, 64), this, 1F));
        movementAnimationList.add(new Animation("assets\\player_animation\\" + colorScheme + "\\left_", 6, new Point(32 * colorScheme, 0), new Dimension(32, 64), this, 1F));
        movementAnimationList.add(new Animation("assets\\player_animation\\" + colorScheme + "\\right_", 6, new Point(32 * colorScheme, 0), new Dimension(32, 64), this, 1F));
        movementAnimationList.add(new Animation("assets\\player_animation\\" + colorScheme + "\\idle0_", 1, new Point(32 * colorScheme, 0), new Dimension(32, 64), this, 1F));
        movementAnimationList.add(new Animation("assets\\player_animation\\" + colorScheme + "\\idle1_", 1, new Point(32 * colorScheme, 0), new Dimension(32, 64), this, 1F));
        movementAnimationList.add(new Animation("assets\\player_animation\\" + colorScheme + "\\idle2_", 1, new Point(32 * colorScheme, 0), new Dimension(32, 64), this, 1F));
        movementAnimationList.add(new Animation("assets\\player_animation\\" + colorScheme + "\\idle3_", 1, new Point(32 * colorScheme, 0), new Dimension(32, 64), this, 1F));


    }

    public Player() {
    }


    public int getColorScheme() {
        return colorScheme;
    }

    @Override
    public boolean isTextureLoaded() {
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
    public void paint(Graphics g, float elapsedTime, Camera camera) {
        super.paint(g, elapsedTime, camera);
    }

    @Override
    public char update(float elapsedTime) {
        return super.update(elapsedTime);
    }

    @Override
    protected void drawFrame(Graphics g, float elapsedTime, Camera camera) {
        for (Animation animation : movementAnimationList) {
            animation.draw((Graphics2D) g, elapsedTime, camera);
        }
    }

    @Override
    public char call(Object[] input) {
        HashMap<String, AtomicBoolean> keyMap;
        super.call(input);
        if (input == null || input.length < 1) {
            return 0;
        } else if (input[0] instanceof String) {
            if (((String) input[0]).contentEquals("input")) {

                if (input[4] instanceof HashMap) {
                    keyMap = (HashMap<String, AtomicBoolean>) input[4];
                    if (keyMap.get("w").get() && keyMap.get("s").get() || keyMap.get("a").get() && keyMap.get("d").get()) {
                        idleAnimation();
                        return 0;
                    }

                    if (keyMap.get("w").get()) {
                        moveForward();
                    } else if (keyMap.get("s").get()) {
                        moveBackward();
                    } else if (keyMap.get("a").get()) {
                        moveLeft();
                    } else if (keyMap.get("d").get()) {
                        moveRight();
                    } else {
                        idleAnimation();
                    }
                }

            }
        }
        return 0;
    }
    //BufferedImage[] images = this.getParent().getResourceHandler().getTextureGroup();
    // Animation PlayerAnimation = new Animation();

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


}
