package de.jspll.data.objects.examples;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.graphics.Camera;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.jspll.data.ChannelID.INPUT;
import static de.jspll.data.ChannelID.LOGIC;

public class DisplayMover extends GameObject {
    public DisplayMover(String ID) {
        super(ID, "g.tst.DisplayMover");
    }

    private int framesOn = 0;
    private boolean mousedown;
    private int[] currMousePos = new int[]{0, 0};
    private int[] previousMousePos = new int[]{0, 0};
    private int continuousPlus = 0;
    private int continuousMinus = 0;
    private boolean plus = false;
    private boolean minus = false;
    private float mwMovement = 0;
    HashMap<String, AtomicBoolean> keyMap;

    @Override
    public char update(float elapsedTime) {
        super.update(elapsedTime);
        if (keyMap != null) {
            float displacement = 160f;

            if((keyMap.get("w").get() && keyMap.get("a").get()) || (keyMap.get("w").get() && keyMap.get("d").get()) || (keyMap.get("s").get() && keyMap.get("a").get()) || (keyMap.get("s").get() && keyMap.get("d").get()))
                displacement *= 0.5;

            Camera cam = getParent().getSelectedCamera();
            if (keyMap.get("w").get()) {
                cam.increase_y(-displacement * elapsedTime);
            }
            if (keyMap.get("a").get()) {
                cam.increase_x(-displacement * elapsedTime);
            }
            if (keyMap.get("s").get()) {
                cam.increase_y(displacement * elapsedTime);
            }
            if (keyMap.get("d").get()) {
                cam.increase_x(displacement * elapsedTime);
            }
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
        float zoomIncrement = 0.5f;
        if (plus) {
            continuousPlus++;
            if (continuousPlus % 2 == 0)
                getParent().getSelectedCamera().increase_zoom(zoomIncrement * elapsedTime);
        }

        if (minus) {
            continuousMinus++;
            if (continuousMinus % 2 == 0) {
                getParent().getSelectedCamera().increase_zoom(-zoomIncrement * elapsedTime);
            }
        }

        if(mwMovement != 0) {

            getParent().getSelectedCamera().increase_zoom(mwMovement * 0.1f );
            mwMovement = 0;
        }

        return 0;
    }

    @Override
    public char call(Object[] input) {
        super.call(input);
        if (input == null || input.length < 1) {
            return 0;
        } else if (input[0] instanceof String) {
            if (((String) input[0]).contentEquals("input")) {
                if (input[4] instanceof HashMap) {
                    this.keyMap = (HashMap<String, AtomicBoolean>) input[4];
                }
                if (input[7] instanceof Number) {
                    mwMovement = ((Number) input[7]).floatValue();
                }

            }
        }
        return 0;
    }

    @Override
    public ChannelID[] getChannels() {
        return new ChannelID[]{INPUT, LOGIC};
    }
}
