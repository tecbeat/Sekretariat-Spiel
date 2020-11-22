package de.jspll;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.examples.MouseFollower;
import de.jspll.data.objects.game.ui.ButtonObject;
import de.jspll.data.objects.game.ui.MenuObject;
import de.jspll.data.objects.game.ui.SceneSwitchButton;
import de.jspll.frames.FrameHandler;
import de.jspll.graphics.ResourceHandler;
import de.jspll.handlers.JSONSupport;
import de.jspll.util.Logger;
import javafx.beans.binding.ObjectExpression;

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

        ArrayList<Object> objects = new ArrayList<>();

        /*Player p = new Player("OwnPlayer", new Point(1280,1120), new Dimension(32, 64), 1);
        objects.add(p);

//        TaskHolder th1 = new TaskHolder("test", "g.dflt.TaskHolder",
//                new Point(1280,1088),
//                new Dimension(32,16),
//                new Task());
//        objects.add(th1);
        MouseFollower m = new MouseFollower("test1");
        objects.add(m);*/

        //EditorHandler test = new EditorHandler("dev1", "devtools", new Dimension(200, 700));
        //frameHandler.getGameObjectHandler().loadObject(test);

        /**
         * Tile map to Array List
         */
        /*for(TileMap tileMap: frameHandler.getGameObjectHandler().loadMap("assets\\map\\Sekretariat-Spiel-Plan_v2.json")) {
            objects.add(tileMap);
        }

        String strJSON = JSONSupport.convertObjectsToJson(objects);

        ResourceHandler rh = frameHandler.getGameObjectHandler().getResourceHandler();
        rh.jsonStrToFile(strJSON, "C:\\Users\\Lukas\\Desktop\\Temp\\Main.json");*/

//        ResourceHandler rh = frameHandler.getGameObjectHandler().getResourceHandler();

        /*ArrayList<Object> menu = new ArrayList<>();
        MenuObject mo = new MenuObject("Headline", "de.jspll.data.objects.game.ui.MenuObject", 0,0,new Dimension(120,20), "Untitled Secretary Game", false);
        menu.add(mo);
        mo = new SceneSwitchButton("Start", "de.jspll.data.objects.game.ui.MenuObject", 0,25,new Dimension(120,20), "Start Game", true, ChannelID.SCENE_2, "scenes\\Game.json");
        menu.add(mo);
        mo = new SceneSwitchButton("Credits", "de.jspll.data.objects.game.ui.MenuObject", 0,50,new Dimension(120,20), "Credits", true, ChannelID.SCENE_3, "scenes\\Credits.json");
        menu.add(mo);
        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_1, menu);
        frameHandler.getGameObjectHandler().switchScene(ChannelID.SCENE_1);*/

        /*ArrayList<Object> credits = new ArrayList<>();
        MenuObject co = new MenuObject("Headline", "de.jspll.data.objects.game.ui.MenuObject", 0,0,new Dimension(120,20), "Untitled Secretary Game", false);
        credits.add(co);
        co = new MenuObject("Tanks", "de.jspll.data.objects.game.ui.MenuObject", 0,25,new Dimension(120,20), "Special Thanks to:", false);
        credits.add(co);
        co = new MenuObject("Gson", "de.jspll.data.objects.game.ui.MenuObject", 0,50,new Dimension(120,20), "Gson by Google", false);
        credits.add(co);
        co = new MenuObject("Graphics", "de.jspll.data.objects.game.ui.MenuObject", 0,75,new Dimension(120,20), "Graphics by LimeZu", false);
        credits.add(co);
        co = new SceneSwitchButton("Home", "de.jspll.data.objects.game.ui.MenuObject", 0,100,new Dimension(120,20), "Start Game", true, ChannelID.SCENE_2, "scenes\\MainMenu");
        credits.add(co);
        String strJSON = JSONSupport.convertObjectsToJson(credits);
        rh.jsonStrToFile(strJSON, "C:\\Users\\Lukas\\Desktop\\Temp\\Credits.json");*/


        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_1, "scenes\\MainMenu");

        //frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_2, "scenes\\Main.json");



        //JsonArray jo = new JsonParser().parse(strJSON).getAsJsonArray();

        //frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_2, jo);
        frameHandler.run();
    }
}