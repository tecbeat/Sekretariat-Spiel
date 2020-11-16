package de.jspll;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.examples.MouseFollower;
import de.jspll.data.objects.game.player.Player;
import de.jspll.dev.EditorHandler;
import de.jspll.frames.FrameHandler;

import java.awt.*;
import java.util.ArrayList;

public class Main {

    //handles Frame Drawing and game logic
    private static FrameHandler frameHandler = new FrameHandler("Sekreteriat Spiel", new Dimension(1920, 1080));

    public static void main(String[] args) {

        ArrayList<GameObject> objects = new ArrayList<>();
        /*for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 5; y++) {
                objects.add(new GameObject(x + "_" + y, "g.dflt.GameObject", x * 16, y * 32 + (16 * (x % 2)), new Dimension(16, 16)));
            }
        }

         */
        //objects.add(new PaperList("test",new Dimension(600,600),new Point(0,0)));
        //objects.add(new AnimatedGameObject("test",0,0,new Dimension(1600,900),new Animation("assets\\player_animation\\forward0_", 6, new Point(0, 0), new Dimension(32,64), null, .1F)));

        /** funktioniert nicht
         *  aktuelles Primärproblem:
         *  - getParent return null -> komme nicht an den GameObjectHandler ran umd neue Objekte hinzuzufügen um keine hard-coded animationen in der Main erstellen zu müssen
         */
        objects.add(new Player("OwnPlayer", 0,0, new Dimension(1600,900), 1));
        objects.add(new Player("testColor0", 0,0, new Dimension(1600,900), 2));
        objects.add(new Player("testColor1", 0,0, new Dimension(1600,900), 3));
        objects.add(new Player("testColor2", 0,0, new Dimension(1600,900), 4));
        objects.add(new Player("testColor3", 0,0, new Dimension(1600,900), 5));





        objects.add(new MouseFollower("test1"));
        //objects.add(new DisplayMover("test1"));
        //objects.add(new Counter("test"));
        frameHandler.getGameObjectHandler().loadScene(ChannelID.SCENE_0, objects);
        frameHandler.getGameObjectHandler().loadObjects(objects);
        EditorHandler test = new EditorHandler("dev1", "devtools", new Dimension(200, 700));
        frameHandler.getGameObjectHandler().loadObject(test);
        frameHandler.run();
    }
}
