package de.jspll;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.examples.Counter;
import de.jspll.data.objects.examples.DisplayMover;
import de.jspll.data.objects.examples.MouseFollower;
import de.jspll.data.objects.game.tasks.Task;
import de.jspll.data.objects.game.tasks.TaskHolder;
import de.jspll.data.objects.loading.LoadingBar;
import de.jspll.data.objects.loading.Report;
import de.jspll.dev.EditorHandler;
import de.jspll.frames.FrameHandler;
import de.jspll.graphics.ResourceHandler;
import de.jspll.handlers.JSONSupport;
import de.jspll.util.json.JSONArray;
import de.jspll.util.json.JSONObject;
import de.jspll.util.json.JSONValue;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.Context;
import de.jspll.data.objects.game.map.TileMap;
import de.jspll.data.objects.game.ui.PaperList;
import de.jspll.data.objects.game.player.Player;
import de.jspll.dev.EditorHandler;
import de.jspll.frames.FrameHandler;
import de.jspll.util.Logger;
import de.jspll.util.json.JSONObject;
import de.jspll.util.json.JSONUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static boolean DEBUG = false;

    //handles Frame Drawing and game logic
    private static FrameHandler frameHandler = new FrameHandler("Sekreteriat Spiel", new Dimension(1920, 1080));

    public static void main(String[] args) {
        for(String s: args){
            if(s.contentEquals("-Debug") || s.contentEquals("-D"))
                DEBUG = true;
        }

        /**if(true){
            JSONUtils jsonReader = JSONUtils.singleton;
            JSONObject obj = jsonReader.readJSON("test.json");
            System.out.printf("finished");
            frameHandler.getGraphicsHandler().getWindow().setVisible(false);
            return;
        }**/


        Logger logger = new Logger();
        logger.start();




        ArrayList<Object> objects = new ArrayList<>();

//        TileMap tm = new TileMap("tileMap", "g.dflt.TileMap", null, 0, 0,
//                new Dimension(1000, 1000), 40, 40,
//                new String[]{"assets\\map\\Floors_only_32x32"});
        //objects.add(tm);

        //objects.add(new PaperList("test",new Dimension(600,600),new Point(0,0)));
        //objects.add(new AnimatedGameObject("test",0,0,new Dimension(1600,900),new Animation("assets\\player_animation\\forward0_", 6, new Point(0, 0), new Dimension(32,64), null, .1F)));


        Player p = new Player("OwnPlayer", new Point(1280,1120), new Dimension(32, 64), 1);
        objects.add(p);

        //System.out.println(JSONSupport.convertObjectToJson(p));


//        TaskHolder th1 = new TaskHolder("test", "g.dflt.TaskHolder",
//                new Point(1280,1088),
//                new Dimension(32,16),
//                new Task());
//        objects.add(th1);
        MouseFollower m = new MouseFollower("test1");
        //DisplayMover d = new DisplayMover("test1");
        objects.add(m);
        //objects.add(d);


        //objects.add(new Counter("test"));
        //frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_0, objects);
        //frameHandler.getGameObjectHandler().loadObjects(objects);
        //EditorHandler test = new EditorHandler("dev1", "devtools", new Dimension(200, 700));
        //frameHandler.getGameObjectHandler().loadObject(test);

        for(TileMap tileMap: frameHandler.getGameObjectHandler().loadMap("assets\\map\\Sekretariat-Spiel-Plan_v2.json"))
            objects.add(tileMap);
        
        //String strJSON = "[" + JSONSupport.convertObjectToJson(m) + "," + JSONSupport.convertObjectToJson(d) + "," +  JSONSupport.convertObjectToJson(p) + "," +  JSONSupport.convertObjectToJson(tm) + "]";
        String strJSON = JSONSupport.convertObjectsToJson(objects);
        //String strJSON = JSONSupport.convertObjectsToJson(new ArrayList<Object>(Arrays.asList(frameHandler.getGameObjectHandler().loadMap("assets\\map\\Sekretariat-Spiel-Plan_v2.json"))));


        ResourceHandler resH = frameHandler.getGameObjectHandler().getResourceHandler();
        System.out.println("Map: " + resH.fileToJson("assets\\map\\Main-Map.json"));

        JsonArray jo = new JsonParser().parse(strJSON).getAsJsonArray();

        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_2, jo);
        frameHandler.run();


        /*for(TileMap ttm : frameHandler.getGameObjectHandler().loadMap("assets\\map\\Sekretariat-Spiel-Plan_v2.json")){
            frameHandler.getGameObjectHandler().loadObject(ttm);
        }
*/


    }
}