package de.jspll.data.objects.game.player;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.Animation;
import de.jspll.data.objects.TexturedObject;
import de.jspll.graphics.Camera;
import de.jspll.util.Logger;
import de.jspll.util.Vector2D;

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

    private String lastPressedKey = "s";
    private boolean start = true;
    private Point pos;
    private float continuous_walking_time = 0f;
    private boolean sprinted_last = false;

    Point halfResolution;

    private Vector2D velocity = new Vector2D(0,0);

    HashMap<String, AtomicBoolean> keyMap;

    public Player(String ID, Point pos, Dimension dimension, int colorScheme) {
        super(ID, "g.ntt.OwnPlayer", pos.x, pos.y, dimension);
        this.pos = pos;
        this.colorScheme = colorScheme;
        channels = new ChannelID[]{ ChannelID.PLAYER, ChannelID.LOGIC};

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
        for(Animation an: movementAnimationList){
            if(!an.isLoaded()){
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
    public void paint(Graphics g, float elapsedTime, Camera camera) {
        super.paint(g, elapsedTime, camera);
    }

    @Override
    public char update(float elapsedTime) {
        super.update(elapsedTime);

        if(sprinted_last){
            velocity.instanceScale(0.9d);
            if(velocity.euclideanDistance() < 1){
                sprinted_last = false;
            }
        }else {
            velocity.instanceScale(0d);
        }



        if (keyMap != null) {
            double speed = 95d;

            boolean w = keyMap.get("w").get();
            boolean a = keyMap.get("a").get();
            boolean s = keyMap.get("s").get();
            boolean d = keyMap.get("d").get();
            boolean shift = keyMap.get("SHIFT").get();

            if(shift){
                sprinted_last = true;
                speed *= 1.7;
                for(Animation animation: movementAnimationList){
                    animation.updateDuration(0.5f);
                }
            } else {
                for(Animation animation: movementAnimationList){
                    animation.updateDuration(1f);
                }
            }

            if (w || a || s || d) {
                if(!shift){
                    sprinted_last = false;
                }
                velocity.setY(0);
                velocity.setX(1);
                velocity.instanceScale(speed);
            }

            if(w && s || a && d ){
                velocity.setX(0);
                idleAnimation();
            } else if (w && a) {
                velocity.instanceRotate(1.25 * Math.PI);
                moveLeft();
            } else if (w && d) {
                velocity.instanceRotate(1.75 * Math.PI);
                moveRight();
            } else if (s && a) {
                velocity.instanceRotate(0.75 * Math.PI);
                moveBackward();
            } else if (s && d) {
                velocity.instanceRotate(0.25 * Math.PI);
                moveBackward();
            } else if (w) {
                velocity.instanceRotate(1.5 * Math.PI);
                moveForward();
            } else if (a) {
                velocity.instanceRotate(Math.PI);
                moveLeft();
            } else if (s) {
                velocity.instanceRotate(0.5 * Math.PI);
                moveBackward();
            } else if(d){
                moveRight();
            } else {
                idleAnimation();
            }
        }

        Vector2D scaledVelocity = velocity.scale(elapsedTime);
        scaledVelocity.updatePos(pos);

        Camera c = getParent().getSelectedCamera();
        if (halfResolution == null){
            halfResolution = new Point(c.getWidth()/2, c.getHeight()/2);
            Logger.d.add("Res / 2: " + halfResolution);
        }
        int[] transform = c.transform(new int[]{pos.x, pos.y});
        Point transformedPos = new Point(transform[0], transform[1]);

        Vector2D vec = new Vector2D(transformedPos, halfResolution);

        c.increase_y((float) - vec.y);
        c.increase_x((float) - vec.x);



        return super.update(elapsedTime);
    }

    @Override
    protected void drawFrame(Graphics g, float elapsedTime, Camera camera) {
        for (Animation animation : movementAnimationList) {
            animation.draw((Graphics2D) g, elapsedTime, camera);
        }
        Point t = new Point(pos);
        velocity.updatePos(t);
        g.setColor(Color.MAGENTA);
        g.drawLine(camera.applyXTransform(pos.x),camera.applyYTransform(pos.y),camera.applyXTransform(t.x),camera.applyYTransform(t.y));
    }

    @Override
    public char call(Object[] input) {
        super.call(input);
        if (input == null || input.length < 1) {
            return 0;
        } else if (input[0] instanceof String) {
            if (((String) input[0]).contentEquals("input")) {

                if (input[4] instanceof HashMap) {
                    keyMap = (HashMap<String, AtomicBoolean>) input[4];


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
                //movementAnimationList.get(4).setPos(pos);
                movementAnimationList.get(4).startAnimation(start);
                break;
            case "s":
                //movementAnimationList.get(5).setPos(pos);
                movementAnimationList.get(5).startAnimation(start);
                break;
            case "a":
                //movementAnimationList.get(6).setPos(pos);
                movementAnimationList.get(6).startAnimation(start);
                break;
            case "d":
                //movementAnimationList.get(7).setPos(pos);
                movementAnimationList.get(7).startAnimation(start);
                break;
        }
    }

    public void moveForward() {
        stopAllAnimation();
        //movementAnimationList.get(0).setPos(pos);
        movementAnimationList.get(0).startAnimation(start);
        start = false;
        lastPressedKey = "w";
    }

    public void moveBackward() {
        stopAllAnimation();
        //movementAnimationList.get(1).setPos(pos);
        movementAnimationList.get(1).startAnimation(start);
        start = false;
        lastPressedKey = "s";
    }

    public void moveLeft() {
        stopAllAnimation();
        //movementAnimationList.get(2).setPos(pos);
        movementAnimationList.get(2).startAnimation(start);
        start = false;
        lastPressedKey = "a";
    }

    public void moveRight() {
        stopAllAnimation();
        //movementAnimationList.get(3).setPos(pos);
        movementAnimationList.get(3).startAnimation(start);
        start = false;
        lastPressedKey = "d";
    }

    @Override
    public void updateReferences() {
        super.updateReferences();
        keyMap = getParent().getLogicHandler().getInputHandler().getKeyMap();

        for(Animation a:movementAnimationList)
            a.setPos(pos);
    }
}
