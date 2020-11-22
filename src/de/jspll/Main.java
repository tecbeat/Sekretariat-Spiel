package de.jspll;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.examples.MouseFollower;
import de.jspll.frames.FrameHandler;
import de.jspll.handlers.JSONSupport;
import de.jspll.data.objects.game.map.TileMap;
import de.jspll.data.objects.game.player.Player;
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

        ArrayList<Object> objects = new ArrayList<>();

        Player p = new Player("OwnPlayer", new Point(1280,1120), new Dimension(32, 64), 1);
        objects.add(p);

//        TaskHolder th1 = new TaskHolder("test", "g.dflt.TaskHolder",
//                new Point(1280,1088),
//                new Dimension(32,16),
//                new Task());
//        objects.add(th1);
        MouseFollower m = new MouseFollower("test1");
        objects.add(m);

        //EditorHandler test = new EditorHandler("dev1", "devtools", new Dimension(200, 700));
        //frameHandler.getGameObjectHandler().loadObject(test);

        for(TileMap tileMap: frameHandler.getGameObjectHandler().loadMap("assets\\map\\Sekretariat-Spiel-Plan_v2.json")) {
            objects.add(tileMap);
        }

        String strJSON = JSONSupport.convertObjectsToJson(objects);


        JsonArray jo = new JsonParser().parse(strJSON).getAsJsonArray();

        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_2, jo);
        frameHandler.run();
    }
}