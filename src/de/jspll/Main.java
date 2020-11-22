package de.jspll;

import de.jspll.data.ChannelID;
import de.jspll.frames.FrameHandler;
import de.jspll.util.Logger;
import java.awt.*;

public class Main {

    public static boolean DEBUG = false;

    //handles Frame Drawing and game logic
    private static FrameHandler frameHandler = new FrameHandler("Sekreteriat Spiel", new Dimension(1920, 1080));

    public static void main(String[] args) {
        for(String s: args){
            if(s.contentEquals("-Debug") || s.contentEquals("-D"))
                DEBUG = true;
        }

        Logger logger = new Logger();
        logger.start();

        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_1, "scenes\\MainMenu");
        frameHandler.run();
    }
}