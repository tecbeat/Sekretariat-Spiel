package de.jspll.data.objects.examples;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.Animation;
import de.jspll.data.objects.Texture;
import de.jspll.data.objects.TexturedObject;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by reclinarka on 29-Oct-20.
 */
public class AnimatedGameObject extends TexturedObject {
    public AnimatedGameObject(String ID, int x, int y, Dimension dimension, Animation animation) {
        super(ID, "g.tst.Animated", x, y, dimension, animation);
        texture.setParent(this);
        ((Animation) texture).setLooping(true);
        channels = new ChannelID[]{ChannelID.INPUT,ChannelID.UI};
    }

    @Override
    public char call(Object[] input) {
        HashMap<String,AtomicBoolean> keyMap;
        super.call(input);
        if (input == null || input.length < 1) {
            return 0;
        } else if (input[0] instanceof String) {
            if (((String) input[0]).contentEquals("input")) {

                if (input[4] instanceof HashMap) {
                    keyMap = (HashMap<String, AtomicBoolean>) input[4];
                    if(keyMap.get("ENTER").get()){
                        ((Animation) texture).startAnimation();
                    }
                }

            }
        }
        return 0;
    }
}
