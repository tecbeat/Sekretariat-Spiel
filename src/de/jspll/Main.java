package de.jspll;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import de.jspll.audio.AudioHandler;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.examples.DisplayMover;
import de.jspll.data.objects.examples.MouseFollower;
import de.jspll.data.objects.game.map.TileMap;
import de.jspll.data.objects.game.player.ColorScheme;
import de.jspll.data.objects.game.player.Player;
import de.jspll.data.objects.game.ui.HomeCameraAssist;
import de.jspll.data.objects.game.ui.MenuObject;
import de.jspll.data.objects.game.ui.SceneSwitchButton;
import de.jspll.data.objects.game.ui.semiTransparentBackground;
import de.jspll.frames.FrameHandler;
import de.jspll.graphics.Camera;
import de.jspll.graphics.ResourceHandler;
import de.jspll.handlers.GameObjectHandler;
import de.jspll.handlers.JSONSupport;
import de.jspll.util.Logger;
import java.awt.*;
import java.util.ArrayList;

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
    private static FrameHandler frameHandler = new FrameHandler("Sekreteriat Spiel", new Dimension(1920, 1080));

    public static void main(String[] args) {
        for(String s: args){
            if(s.contentEquals("-Debug") || s.contentEquals("-D"))
                DEBUG = true;
        }

        Logger logger = new Logger();
        logger.start();

        MouseFollower mf = new MouseFollower("Follow");






        new AudioHandler().playMusic(frameHandler.getGameObjectHandler());


        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_1, "/scenes/MainMenu.json");

        frameHandler.run();



    }
}