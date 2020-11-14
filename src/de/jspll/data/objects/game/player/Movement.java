package de.jspll.data.objects.game.player;

import de.jspll.data.objects.Animation;
import de.jspll.data.objects.TexturedObject;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Movement extends TexturedObject {

    private Animation animation;
    private String direction;
    private boolean start = true;

    public Movement(String ID, int x, int y, Dimension dimension, Animation animation, String direction) {
        super(ID, "g.tst.PlayerAnimation", x, y, dimension, animation);
        texture.setParent(this);
        ((Animation) texture).setLooping(true);
        this.direction = direction;
        this.animation = animation;
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
                    if (keyMap.get(direction).get()) {
                        ((Animation) texture).startAnimation(start);
                        start = false;
                    } else {
                        ((Animation) texture).stopAnimation();
                    }
                }

            }
        }
        return 0;
    }
}
