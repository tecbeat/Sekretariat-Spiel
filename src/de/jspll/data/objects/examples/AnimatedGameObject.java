package de.jspll.data.objects.examples;

import de.jspll.data.objects.Animation;
import de.jspll.data.objects.TexturedObject;
import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Philipp Polland
 *
 * @version 1.0
 */
@Deprecated
public class AnimatedGameObject extends TexturedObject {
    public AnimatedGameObject(String ID, int x, int y, Dimension dimension, Animation animation) {
        super(ID, "g.tst.Animated", x, y, dimension, animation);
        texture.setParent(this);
        ((Animation)texture).setLooping(true);
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
                    if (keyMap.get("ENTER").get()){
                        ((Animation) texture).startAnimation();
                    }
                }
            }
        }
        return 0;
    }
}
