package de.jspll.data.objects.examples;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
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
@Deprecated
public class MouseFollower extends GameObject {

    public MouseFollower(String ID){
        super(ID,"g.tst.MouseFollower");
        channels = new ChannelID[]{ChannelID.INPUT,ChannelID.OVERLAY};
    }

    private boolean mousedown;
    private int[] mousePos = new int[]{0,0};
    private String[] keys;
    private HashMap<String,AtomicBoolean> keyMap;

    @Override
    public char call(Object[] input) {
        super.call(input);
        if(input == null || input.length < 1){
            return 0;
        } else if(input[0] instanceof String){
            if( ((String) input[0]).contentEquals("input")){
                if((boolean) input[1]){
                    mousedown = true;
                } else {
                    mousedown = false;
                }
                int[] pos = (int[]) input[5];
                mousePos[0] = pos[0];
                mousePos[1] = pos[1];
                keys = (String[]) input[6];
                keyMap = (HashMap<String, AtomicBoolean>) input[4];
            }
        }
        return 0;
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        if (mousedown) {
            g.setColor(Color.red);
        } else {
            g.setColor(Color.PINK);
        }
        //updateMousePos();
        g.drawOval(mousePos[0]-camera.applyZoom(8),mousePos[1]-camera.applyZoom(8),camera.applyZoom(16),camera.applyZoom(16));
        if(keyMap != null && null != keys){
            String toolTip = "x:" + mousePos[0] + " | y:" + mousePos[1] + "; keys: ";
            for(String key :keys){
                if(keyMap.get(key).get()){
                    toolTip += key + " ";
                }
            }
            g.drawString(toolTip,mousePos[0],mousePos[1]);
        }
        g.setColor(Color.CYAN);
        g.drawOval(camera.applyXTransform(camera.revertXTransform(mousePos[0]))-20,camera.applyYTransform(camera.revertYTransform(mousePos[1]))-20,40,40);
    }
}
