package de.jspll;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.examples.MouseFollower;
import de.jspll.data.objects.game.map.TileMap;
import de.jspll.data.objects.game.player.Player;
import de.jspll.frames.FrameHandler;
import de.jspll.handlers.JSONSupport;
import de.jspll.util.Logger;
import java.awt.*;
import java.util.ArrayList;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author
 *
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

        /*assasa
        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_1, "/scenes/MainMenu");

         */
        ArrayList<Object> objects = new ArrayList<>();
        Player p = new Player("OwnPlayer", new Point(1280,1120), new Dimension(32, 64), 1);
        objects.add(p);
        MouseFollower mf = new MouseFollower("Follow");
        objects.add(mf);
        TileMap[] tms = frameHandler.getGameObjectHandler().loadMap("/assets/map/Sekretariat-Spiel-Plan_v3.json");
        for(TileMap tm : tms)
            objects.add(tm);

        String jsonStr = JSONSupport.convertObjectsToJson(objects);
        JsonArray jsonArray = new JsonParser().parse(jsonStr).getAsJsonArray();


        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_2, jsonArray);
        frameHandler.getGameObjectHandler().switchScene(ChannelID.SCENE_2);

        frameHandler.run();

    }
}