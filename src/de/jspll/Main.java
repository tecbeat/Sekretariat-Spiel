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
import de.jspll.data.objects.loading.LoadingBar;
import de.jspll.data.objects.loading.Report;
import de.jspll.dev.EditorHandler;
import de.jspll.frames.FrameHandler;
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

public class Main {

    //handles Frame Drawing and game logic
    private static FrameHandler frameHandler = new FrameHandler("Sekreteriat Spiel", new Dimension(1920, 1080));

    public static void main(String[] args) {

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
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 5; y++) {
                //objects.add(new GameObject(x + "_" + y, "g.dflt.GameObject", x * 16, y * 32 + (16 * (x % 2)),new Dimension(16,16)));
            }
        }

        TileMap tm = new TileMap("tileMap", "g.dflt.TileMap", null, 0, 0,
                new Dimension(500, 500), 20, 20,
                new String[]{"assets\\map\\Floors_only_32x32"});
        objects.add(tm);

        //objects.add(new PaperList("test",new Dimension(600,600),new Point(0,0)));
        //objects.add(new AnimatedGameObject("test",0,0,new Dimension(1600,900),new Animation("assets\\player_animation\\forward0_", 6, new Point(0, 0), new Dimension(32,64), null, .1F)));

        /** funktioniert nicht
         *  aktuelles Primärproblem:
         *  - getParent return null -> komme nicht an den GameObjectHandler ran umd neue Objekte hinzuzufügen um keine hard-coded animationen in der Main erstellen zu müssen
         */
        Player p = new Player("OwnPlayer", 0,0, new Dimension(1600,900), 1);
        objects.add(p);
        objects.add(new Player("testColor0", 0,0, new Dimension(1600,900), 2));
        objects.add(new Player("testColor1", 0,0, new Dimension(1600,900), 3));
        objects.add(new Player("testColor2", 0,0, new Dimension(1600,900), 4));
        objects.add(new Player("testColor3", 0,0, new Dimension(1600,900), 5));

        //System.out.println(JSONSupport.convertObjectToJson(p));

        MouseFollower m = new MouseFollower("test1");
        DisplayMover d = new DisplayMover("test1");
        objects.add(m);
        objects.add(d);

        /*System.out.println(JSONSupport.convertObjectToJson(m));
        System.out.println(JSONSupport.convertObjectToJson(d));*/

        //objects.add(new Counter("test"));
        //frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_0, objects);
        //frameHandler.getGameObjectHandler().loadObjects(objects);
        EditorHandler test = new EditorHandler("dev1", "devtools", new Dimension(200, 700));
        frameHandler.getGameObjectHandler().loadObject(test);
        
        //String strJSON = "[" + JSONSupport.convertObjectToJson(m) + "," + JSONSupport.convertObjectToJson(d) + "," +  JSONSupport.convertObjectToJson(p) + "," +  JSONSupport.convertObjectToJson(tm) + "]";
        String strJSON = JSONSupport.convertObjectsToJson(objects);
        System.out.println(strJSON);
        JsonArray jo = new JsonParser().parse(strJSON).getAsJsonArray();

        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_2, jo);

        frameHandler.run();



    }
}