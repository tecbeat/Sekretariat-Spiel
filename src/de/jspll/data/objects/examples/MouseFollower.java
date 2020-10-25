package de.jspll.data.objects.examples;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.graphics.Camera;

import java.awt.*;

/**
 * Created by reclinarka on 23-Oct-20.
 */
public class MouseFollower extends GameObject {

    public MouseFollower(String ID){
        super(ID,"g.tst.MouseFollower");
    }

    private boolean mousedown;
    private int[] mousePos = new int[]{0,0};

    @Override
    public char call(Object[] input) {
        super.call(input);
        if(input == null || input.length < 1){
            return 0;
        } else if(input[0].getClass() == String.class){
            if( ((String) input[0]).contentEquals("input")){
                if((boolean) input[1]){
                    mousedown = true;
                } else {
                    mousedown = false;
                }
                int[] pos = (int[]) input[5];
                mousePos[0] = pos[0];
                mousePos[1] = pos[1];
            }
        }

        return 0;
    }

    @Override
    public ChannelID[] getChannels() {
        return super.getChannels();
    }

    private void updateMousePos(){
        Point pos = getParent().getMousePos();
        if(pos != null){
            mousePos[0] = pos.x;
            mousePos[1] = pos.y;
        }
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera) {
        if (mousedown) {
            g.setColor(Color.red);
        } else {
            g.setColor(Color.PINK);
        }
        //updateMousePos();
        g.drawOval(mousePos[0]-camera.applyZoom(8),mousePos[1]-camera.applyZoom(8),camera.applyZoom(16),camera.applyZoom(16));

    }
}
