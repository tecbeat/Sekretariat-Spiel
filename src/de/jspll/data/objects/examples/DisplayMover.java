package de.jspll.data.objects.examples;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.jspll.data.ChannelID.INPUT;

public class DisplayMover extends GameObject {
    public DisplayMover(String ID) {
        super(ID, "g.tst.DisplayMover");
    }

    private int framesOn = 0;
    private boolean mousedown;
    private int[] currMousePos = new int[]{0,0};
    private int[] previousMousePos = new int[]{0,0};
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
                int[] pos = (int[]) input[5];
                currMousePos[0] = pos[0];
                currMousePos[1] = pos[1];

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
                getParent().getSelectedCamera().increase_zoom(0.002f);
        }

        if (minus) {
            continuousMinus++;
            if (continuousMinus % 2 == 0) {
                getParent().getSelectedCamera().increase_zoom(-0.002f);
            }
        }

        //if(true)return 0;
        if (mousedown) {
            framesOn++;
            if (framesOn > 10) {
                if (currMousePos != null && previousMousePos != null) {
                    getParent().getSelectedCamera().increase_x(-currMousePos[0] + previousMousePos[0]);
                    getParent().getSelectedCamera().increase_y(-currMousePos[1] + previousMousePos[1]);
                }
            }
        }
        previousMousePos[0] = currMousePos[0];
        previousMousePos[1] = currMousePos[1];

        return 0;
    }

    @Override
    public ChannelID[] getChannels() {
        return new ChannelID[]{INPUT};
    }
}
