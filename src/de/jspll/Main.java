package de.jspll;

import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.GameTrie;
import de.jspll.data.objects.examples.DisplayMover;
import de.jspll.data.objects.examples.MouseFollower;
import de.jspll.dev.EditorHandler;
import de.jspll.frames.FrameHandler;

import java.awt.*;
import java.util.ArrayList;

public class Main {

    //handles Frame Drawing and game logic
    private static FrameHandler frameHandler = new FrameHandler("Sekreteriat Spiel", new Dimension(1920, 1080));

    public static void main(String[] args) {

        ArrayList<GameObject> objects = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 5; y++) {
                objects.add(new GameObject(x + "_" + y, "g.dflt.GameObject", x * 16, y * 32 + (16 * (x % 2))));
            }
        }
        objects.add(new MouseFollower("test1"));
        objects.add(new DisplayMover("test1"));
        frameHandler.getGameObjectHandler().loadObjects(objects);
        EditorHandler test = new EditorHandler("dev1");
        frameHandler.getGameObjectHandler().loadObject(test);

        ArrayList<GameObject> objects2 = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 5; y++) {
                objects2.add(new GameObject(x + "_" + y, "g.dflt.GameObject", x * 16, y * 32 + (16 * (x % 2))));
            }
        }
        objects2.add(new MouseFollower("test2"));
        objects2.add(new DisplayMover("test2"));
        test.getFrameHandler().getGameObjectHandler().loadObjects(objects2);

        frameHandler.run();
        test.getFrameHandler().run();
    }
}
