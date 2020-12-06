package de.jspll.data.objects.examples;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.TexturedObject;
import de.jspll.data.objects.LayeredTexture;
import de.jspll.graphics.Camera;
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

public class PaperList extends TexturedObject {
    public PaperList(String ID, Dimension size, Point pos) {
        super(ID, "g.ui.PaperList", pos.x, pos.y, size,new LayeredTexture("assets\\clipboard\\clipboard_",3,10,pos,size,null));
        this.texture.setParent(this);
        channels = new ChannelID[]{ChannelID.UI, ChannelID.INPUT};
    }

    private HashMap<String, AtomicBoolean> keyMap;
    private int framesOn = 0;
    private int[] number_cooldowns = new int[10];
    private boolean mousedown;
    private int[] currMousePos = new int[]{0, 0};
    private int[] previousMousePos = new int[]{0, 0};


    @Override
    public char update(float elapsedTime) {
        return super.update(elapsedTime);

    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        super.paint(g, elapsedTime, camera, currStage);

    }

    @Override
    public char call(Object[] input) {
        super.call(input);
        if (input == null || input.length < 1) {
            return 0;
        } else if (input[0] instanceof String) {
            if (((String) input[0]).contentEquals("input")) {
                if ((boolean) input[3]) {
                    mousedown = true;
                } else {
                    framesOn = 0;
                    mousedown = false;
                }
                int[] pos = (int[]) input[5];
                currMousePos[0] = pos[0];
                currMousePos[1] = pos[1];

                if (input[4] instanceof HashMap) {
                    this.keyMap = (HashMap<String, AtomicBoolean>) input[4];
                }

            }
        }

        if (mousedown) {
            framesOn++;
            if (framesOn > 10) {
                if (currMousePos != null && previousMousePos != null) {
                    x -= - getParent().getSelectedCamera().revertXTransform(currMousePos[0]) + getParent().getSelectedCamera().revertXTransform(previousMousePos[0]);
                    y -= - getParent().getSelectedCamera().revertXTransform(currMousePos[1]) + getParent().getSelectedCamera().revertXTransform(previousMousePos[1]);
                    texture.getPos().x = x;
                    texture.getPos().y = y;
                    }
            }
        }
        if(mouseOver(currMousePos)){
            for(int i = 0; i < 10; i++ ){
                if(keyMap.get(String.format("%d",i)).get() ) {
                    if(number_cooldowns[i] < 1){

                        ((LayeredTexture) texture).toggleLayer(i);
                    } else {
                        if(number_cooldowns[i] > 30){
                            number_cooldowns[i] = 0;
                        }
                    }
                    number_cooldowns[i]++;

                } else {
                    number_cooldowns[i] = 0;
                }

            }
        }
        previousMousePos[0] = currMousePos[0];
        previousMousePos[1] = currMousePos[1];


        return 0;

    }

}
