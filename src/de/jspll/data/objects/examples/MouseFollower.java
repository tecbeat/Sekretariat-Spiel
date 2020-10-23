package de.jspll.data.objects.examples;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;

import java.awt.*;

/**
 * Created by reclinarka on 23-Oct-20.
 */
public class MouseFollower extends GameObject {

    public MouseFollower(){
        super("0","g.tst.MouseFollower");
    }

    private boolean mousedown;
    private int[] mpos = new int[]{0,0};

    @Override
    public char call(Object[] input) {
        super.call(input);
        if(input == null || input.length < 1){
            return 0;
        } else if(input[0].getClass() == String.class){
            if( ((String) input[0]).contentEquals("mouse")){
                if((boolean) input[1]){
                    mousedown = true;
                } else {
                    mousedown = false;
                }
                mpos[0] = (int) input[4];
                mpos[1] = (int) input[5];

            }
        }

        return 0;
    }

    @Override
    public ChannelID[] getChannels() {
        return super.getChannels();
    }

    @Override
    public void paint(Graphics g, float elapsedTime, float zoom) {
        if (mousedown) {
            g.setColor(Color.red);
        } else {
            g.setColor(Color.PINK);
        }
        g.drawOval(mpos[0]-8,mpos[1]-8,16,16);

    }
}
