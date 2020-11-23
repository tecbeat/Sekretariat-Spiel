package de.jspll;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.examples.MouseFollower;
import de.jspll.data.objects.game.map.TileMap;
import de.jspll.data.objects.game.player.Player;
import de.jspll.frames.FrameHandler;
import de.jspll.graphics.ResourceHandler;
import de.jspll.handlers.JSONSupport;
import de.jspll.util.Logger;
import java.awt.*;
import java.util.ArrayList;

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

/*        //debugging start
        ArrayList<Object> objects = new ArrayList<>();
        //Player p = new Player("OwnPlayer", new Point(1280,1120), new Dimension(32, 64), 1);
        //objects.add(p);

        TileMap[] tms = frameHandler.getGameObjectHandler().loadMap("/assets/map/Home_Screen.json");
        for(TileMap tm : tms)
            objects.add(tm);

        String jsonStr = JSONSupport.convertObjectsToJson(objects);

        //ResourceHandler rh = frameHandler.getGameObjectHandler().getResourceHandler();
        //rh.jsonStrToFile(jsonStr, "C:\\Users\\Lukas\\Desktop\\Temp\\Game.json");


        JsonArray jsonArray = new JsonParser().parse(jsonStr).getAsJsonArray();


        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_2, jsonArray);
        frameHandler.getGameObjectHandler().switchScene(ChannelID.SCENE_2);
        //debugging end*/


        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_1, "/scenes/MainMenu");
        frameHandler.run();
    }
}