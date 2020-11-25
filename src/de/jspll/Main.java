package de.jspll;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.examples.DisplayMover;
import de.jspll.data.objects.examples.MouseFollower;
import de.jspll.data.objects.game.map.TileMap;
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
 * @author Lukas Becker, Samuel Assmann
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

        MouseFollower mf = new MouseFollower("Follow");

        /*
        //debugging start
        ArrayList<Object> objects = new ArrayList<>();
        Player p = new Player("OwnPlayer", new Point(1280,1120), new Dimension(32, 64), 1);
        objects.add(p);

        TileMap[] tms = frameHandler.getGameObjectHandler().loadMap("/assets/map/Home_Screen.json");
        for(TileMap tm : tms)
            objects.add(tm);

        String jsonStr = JSONSupport.convertObjectsToJson(objects);

        ResourceHandler rh = frameHandler.getGameObjectHandler().getResourceHandler();
        rh.jsonStrToFile(jsonStr, "C:\\Users\\Lukas\\Desktop\\Temp\\Game.json");


        JsonArray jsonArray = new JsonParser().parse(jsonStr).getAsJsonArray();


        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_2, jsonArray);
        frameHandler.getGameObjectHandler().switchScene(ChannelID.SCENE_2);
        debugging end
        */

        /*ArrayList<Object> objects = new ArrayList<>();
        GameObjectHandler goh = frameHandler.getGameObjectHandler();
        Camera c = goh.getSelectedCamera();
        int width = 0;
        int height = 0;

        TileMap[] tms = goh.loadMap("/assets/map/Home_Screen.json");
        for(TileMap tm : tms){
            objects.add(tm);
            width = Math.max(width,tm.getDimension().width);
            height = Math.max(height,tm.getDimension().height);
        }
        HomeCameraAssist hca = new HomeCameraAssist(width, height, 2f);
        objects.add(hca);

        DisplayMover dm = new DisplayMover("");
        objects.add(dm);

        MenuObject mo = new MenuObject("Headline", "de.jspll.data.objects.game.ui.MenuObject", width/2,height/3,new Dimension(120,20), "Untitled Secretary Game", false);
        objects.add(mo);
        mo = new SceneSwitchButton("Start", "de.jspll.data.objects.game.ui.MenuObject", width/2,height/3+25,new Dimension(120,20), "Start Game", true, ChannelID.SCENE_2, "/scenes/Game.json");
        objects.add(mo);
        mo = new SceneSwitchButton("Credits", "de.jspll.data.objects.game.ui.MenuObject", width/2,height/3+50,new Dimension(120,20), "Credits", true, ChannelID.SCENE_3, "/scenes/Credits.json");
        objects.add(mo);
        semiTransparentBackground stb = new semiTransparentBackground("BG", "de.jspll.data.objects.game.ui.semiTransparentBackground", width/2+100, height/3 -40, new Dimension(450, 200));
        objects.add(stb);


        String jsonStr = JSONSupport.convertObjectsToJson(objects);
        JsonArray jsonArray = new JsonParser().parse(jsonStr).getAsJsonArray();
        goh.loadScene(ChannelID.SCENE_1, jsonArray);

        ResourceHandler rh = frameHandler.getGameObjectHandler().getResourceHandler();
        rh.jsonStrToFile(jsonStr, "C:\\Users\\Lukas\\Desktop\\Temp\\MainMenu.json");

        frameHandler.getGameObjectHandler().switchScene(ChannelID.SCENE_1);*/



        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_1, "/scenes/MainMenu.json");

        frameHandler.run();

    }
}