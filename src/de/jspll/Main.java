package de.jspll;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.Animation;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.GameTrie;
import de.jspll.data.objects.examples.AnimatedGameObject;
import de.jspll.data.objects.examples.Counter;
import de.jspll.data.objects.examples.DisplayMover;
import de.jspll.data.objects.examples.MouseFollower;
import de.jspll.data.objects.game.map.TileMap;
import de.jspll.data.objects.game.ui.PaperList;
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



        ArrayList<GameObject> objects = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 5; y++) {
                //objects.add(new GameObject(x + "_" + y, "g.dflt.GameObject", x * 16, y * 32 + (16 * (x % 2)),new Dimension(16,16)));
            }
        }
        objects.add(new TileMap("tileMap", "g.dflt.TileMap", null, 0, 0, new Dimension(500, 500), 20, 20));
        //objects.add(new PaperList("test",new Dimension(600,600),new Point(0,0)));
        //objects.add(new AnimatedGameObject("test",0,0,new Dimension(1600,900),new Animation("assets\\chair_animation\\Streming_", 198, new Point(0, 0), new Dimension(1600,900), null, 12)));
        objects.add(new MouseFollower("test1"));
        objects.add(new DisplayMover("test1"));
        objects.add(new Counter("test"));
        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_0,objects);
        frameHandler.getGameObjectHandler().loadObjects(objects);
        EditorHandler test = new EditorHandler("dev1","devtools",new Dimension(200,700));
        frameHandler.getGameObjectHandler().loadObject(test);
        frameHandler.run();



    }
}
