package de.jspll;

import de.jspll.audio.AudioHandler;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.examples.MouseFollower;
import de.jspll.frames.FrameHandler;
import de.jspll.util.Logger;
import java.awt.*;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Lukas Becker, Philipp Polland
 * @version 1.0
 */
public class Main {

    public static boolean DEBUG = false;

    //handles Frame Drawing and game logic
    private static FrameHandler frameHandler = new FrameHandler("Sekretariat Spiel", new Dimension(1920, 1080));

    public static void main(String[] args) {
        for(String s: args){
            if(s.contentEquals("-Debug") || s.contentEquals("-D"))
                DEBUG = true;
        }

        Logger logger = new Logger();
        logger.start();

        new AudioHandler().playMusic(frameHandler.getGameObjectHandler());

        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_1, "/scenes/MainMenu.json");

        frameHandler.run();
    }
}