package de.jspll.data.objects.examples;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.jspll.data.ChannelID.INPUT;

public class DisplayMover extends GameObject {
    public DisplayMover() {
        super("0", "g.tst.DisplayMover");
    }

    private int framesOn = 0;
    private boolean mousedown;
    private Point previousMousePos;
    private int continuousPlus = 0;
    private int continuousMinus = 0;
    private boolean plus = false;
    private boolean minus = false;

    @Override
    public char call(Object[] input) {
        super.call(input);
        if (input == null || input.length < 1) {
            return 0;
        } else if (input[0].getClass() == String.class) {
            if (((String) input[0]).contentEquals("input")) {
                if ((boolean) input[1]) {
                    mousedown = true;
                } else {
                    framesOn = 0;
                    mousedown = false;
                }
                if(input[4].getClass() == HashMap.class) {
                    HashMap<String, AtomicBoolean> keyMap = (HashMap<String, AtomicBoolean>) input[4];
                    if (keyMap.get("+").get()) {
                        plus = true;
                    } else {
                        continuousPlus = 0;
                        plus = false;
                    }
                    if (keyMap.get("-").get()) {
                        minus = true;
                    } else {
                        continuousMinus = 0;
                        minus = false;
                    }
                }

            }
        }
        if (plus) {
            continuousPlus++;
            if (continuousPlus % 2 == 0)
                getParent().getSelectedCamera().zoom *= 1.002f;
        }

        if (minus) {
            continuousMinus++;
            if (continuousMinus % 2 == 0)
                getParent().getSelectedCamera().zoom *= 0.998f;
        }

        Point mousePos = getParent().getMousePos();
        if (mousedown) {
            framesOn++;
            if (framesOn > 10) {
                if (mousePos != null && previousMousePos != null) {
                    getParent().getSelectedCamera().x -= mousePos.x - previousMousePos.x;
                    getParent().getSelectedCamera().y -= mousePos.y - previousMousePos.y;
                }
            }
        }
        previousMousePos = mousePos;

        return 0;
    }

    @Override
    public ChannelID[] getChannels() {
        return new ChannelID[]{INPUT};
    }
}
