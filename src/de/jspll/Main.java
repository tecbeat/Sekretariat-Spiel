package de.jspll;

import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.GameTrie;
import de.jspll.frames.FrameHandler;

import java.util.ArrayList;

public class Main {

    //handles Frame Drawing and game logic
    private static FrameHandler frameHandler = new FrameHandler();

    public static void main(String[] args) {


        frameHandler.run();
    }
}
