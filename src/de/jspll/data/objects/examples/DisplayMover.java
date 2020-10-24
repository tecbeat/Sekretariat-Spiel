package de.jspll.data.objects.examples;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;

import java.awt.*;

import static de.jspll.data.ChannelID.BACKGROUND;
import static de.jspll.data.ChannelID.MOUSEUPDATES;

public class DisplayMover extends GameObject {
    public DisplayMover(){
        super("0","g.tst.DisplayMover");
    }

    private int framesOn;
    private boolean mousedown;
    private Point previousMousePos;

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
                    framesOn = 0;
                    mousedown = false;
                }

            }
        }

        Point mousePos = getParent().getMousePos();
        if(mousedown){
            framesOn++;
            if(framesOn > 10){
                if(mousePos != null && previousMousePos != null){
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
        return new ChannelID[]{ MOUSEUPDATES};
    }
}
