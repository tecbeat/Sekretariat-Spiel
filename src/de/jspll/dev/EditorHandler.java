package de.jspll.dev;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.Repeater;
import de.jspll.frames.FrameHandler;
import de.jspll.handlers.GameObjectHandler;

import java.awt.*;

import static de.jspll.data.ChannelID.COMM1;

/**
 * Created by reclinarka on 25-Oct-20.
 */
public class EditorHandler extends GameObject{

    private boolean test = true;

    private FrameHandler frameHandler = new FrameHandler("editor window",new Dimension(200, 600));

    public EditorHandler(String ID) {
        super(ID,"g.dev.EditorHandler");

    }

    public void init(){
        getParent().loadObject(new Repeater("0",frameHandler.getGameObjectHandler()));

    }

    @Override
    public ChannelID[] getChannels() {
        return new ChannelID[]{COMM1};
    }

    @Override
    public char update(float elapsedTime) {
        super.update(elapsedTime);
        if(test){
            init();
            test = false;
        }
        return  0;
    }

    @Override
    public char call(Object[] input) {
        return super.call(input);
    }

    public FrameHandler getFrameHandler() {
        return frameHandler;
    }
}
