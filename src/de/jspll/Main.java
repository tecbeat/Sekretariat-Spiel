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






        ArrayList<GameObject> objects = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 5; y++) {
                objects.add(new GameObject(x + "_" + y, "g.dflt.GameObject", x * 16, y * 32 + (16 * (x % 2)),new Dimension(16,16)));
            }
        }
        //objects.add(new PaperList("test",new Dimension(600,600),new Point(0,0)));
        //objects.add(new AnimatedGameObject("test",0,0,new Dimension(1600,900),new Animation("assets\\chair_animation\\Streming_", 198, new Point(0, 0), new Dimension(1600,900), null, 12)));
        objects.add(new MouseFollower("test1"));
        objects.add(new DisplayMover("test1"));
        objects.add(new Counter("test"));
        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_0,objects);
        frameHandler.getGameObjectHandler().loadObjects(objects);
        EditorHandler test = new EditorHandler("dev1","devtools",new Dimension(200,700));
        frameHandler.getGameObjectHandler().loadObject(test);

        String strJSON = "[{\"type\": \"class de.jspll.data.objects.examples.MouseFollower\", \"object\": {\"mousedown\":false,\"mousePos\":[0,0],\"ID\":\"1234\",\"objectID\":\"g.tst.MouseFollower\",\"active\":true,\"x\":0,\"y\":0,\"channels\":[\"INPUT\",\"OVERLAY\"],\"keyPressedMap\":{},\"serializationReferences\":{}}},{\"type\": \"class de.jspll.data.objects.examples.MouseFollower\", \"object\": {\"mousedown\":false,\"mousePos\":[0,0],\"ID\":\"12345\",\"objectID\":\"g.tst.MouseFollower\",\"active\":true,\"x\":0,\"y\":0,\"channels\":[\"INPUT\",\"OVERLAY\"],\"keyPressedMap\":{},\"serializationReferences\":{}}},{\"type\": \"class de.jspll.data.objects.examples.MouseFollower\", \"object\": {\"mousedown\":false,\"mousePos\":[0,0],\"ID\":\"123456\",\"objectID\":\"g.tst.MouseFollower\",\"active\":true,\"x\":0,\"y\":0,\"channels\":[\"INPUT\",\"OVERLAY\"],\"keyPressedMap\":{},\"serializationReferences\":{}}}]";

        JsonArray jo = new JsonParser().parse(strJSON).getAsJsonArray();

        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_2, jo);

        frameHandler.run();



    }
}