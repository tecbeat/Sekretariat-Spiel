package de.jspll.data.objects.game.player;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.Animation;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.Texture;
import de.jspll.data.objects.TexturedObject;
import de.jspll.handlers.GameObjectHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by reclinarka on 23-Oct-20.
 */
public class Player extends TexturedObject {


    private int colorScheme;   //Possible


    private Animation[] animations = {new Animation("assets\\player_animation\\forward0_", 6, new Point(0, 0), new Dimension(32, 64), this, 1F),
            new Animation("assets\\player_animation\\backward0_", 6, new Point(0, 0), new Dimension(32, 64), this, 1F),
            new Animation("assets\\player_animation\\left0_", 6, new Point(0, 0), new Dimension(32, 64), this, 1F),
            new Animation("assets\\player_animation\\right0_", 6, new Point(0, 0), new Dimension(32, 64), this, 1F)};
    //[0] = Forward (W), [1] = Backwards (S), [2] = Left(L), [3] = Right(R)

    Movement[] MovementAnimations = {
            new Movement("MovementForward",  0, 0, new Dimension(1600, 900), new Animation("assets\\player_animation\\forward0_",  6, new Point(0, 0), new Dimension(32, 64), null, 1F), "w"),
            new Movement("MovementBackward", 0, 0, new Dimension(1600, 900), new Animation("assets\\player_animation\\backward0_", 6, new Point(0, 0), new Dimension(32, 64), null, 1F), "s"),
            new Movement("MovementLeft",     0, 0, new Dimension(1600, 900), new Animation("assets\\player_animation\\left0_",     6, new Point(0, 0), new Dimension(32, 64), null, 1F), "a"),
            new Movement("MovementRight",    0, 0, new Dimension(1600, 900), new Animation("assets\\player_animation\\right0_",    6, new Point(0, 0), new Dimension(32, 64), null, 1F), "d"),
            new Movement("MovementIdle",     0, 0, new Dimension(1600, 900), new Animation("assets\\player_animation\\idle0_",     1, new Point(0, 0), new Dimension(32, 64), null, 1F), "")
    };
    private ArrayList<GameObject> movementArrayList = new ArrayList<>();

    private Animation idleAnimation;
    private String ID;
    private Texture parent;

    public Player(String ID, int x, int y, Dimension dimension, Animation animation) {
        super(ID, "g.ntt.Player", x, y, dimension, animation);
        texture.setParent(this);
        ((Animation) texture).setLooping(true);
        channels = new ChannelID[]{ChannelID.INPUT, ChannelID.PLAYER};


        movementArrayList.add(new Movement("MovementForward",  0, 0, new Dimension(1600, 900), new Animation("assets\\player_animation\\forward0_",  6, new Point(0, 0), new Dimension(32, 64), null, 1F), "w"));
        movementArrayList.add(new Movement("MovementBackward", 0, 0, new Dimension(1600, 900), new Animation("assets\\player_animation\\backward0_", 6, new Point(0, 0), new Dimension(32, 64), null, 1F), "s"));
        movementArrayList.add(new Movement("MovementLeft",     0, 0, new Dimension(1600, 900), new Animation("assets\\player_animation\\left0_",     6, new Point(0, 0), new Dimension(32, 64), null, 1F), "a"));
        movementArrayList.add(new Movement("MovementIdle",     0, 0, new Dimension(1600, 900), new Animation("assets\\player_animation\\idle0_",     1, new Point(0, 0), new Dimension(32, 64), null, 1F), ""));




    }

    public Player() {
    }

    @Override
    public GameObjectHandler getParent() {
        return super.getParent();
    }

    public int getColorScheme() {
        return colorScheme;
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
                    if (keyMap.get("w").get()) {
                        ((Animation) texture).startAnimation();
//                        ((Animation) texture).stopAnimation();
//                        ((Animation) test.getTexture()).setLooping(true);
//                        ((Animation) test.getTexture()).startAnimation();
//                        (movementArrayList.get(0)).call(input);

                    } else if (keyMap.get("s").get()) {
                        ((Animation) texture).stopAnimation();
//                        animations[1].loadTextures();
//                        animations[1].startAnimation();


                    } else if (keyMap.get("a").get()) {
                        ((Animation) texture).stopAnimation();
//                        animations[2].loadTextures();
//                        animations[2].startAnimation();

                    } else if (keyMap.get("d").get()) {
                        ((Animation) texture).stopAnimation();
//                        animations[3].loadTextures();
//                        animations[3].startAnimation();

                    }


                }

            }
        }
        return 0;
    }
    //BufferedImage[] images = this.getParent().getResourceHandler().getTextureGroup();
    // Animation PlayerAnimation = new Animation();


}
