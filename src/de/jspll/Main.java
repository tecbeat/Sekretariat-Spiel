package de.jspll;

import de.jspll.frames.FrameHandler;

public class Main {

    //handles Frame Drawing and game logic
    private static FrameHandler frameHandler = new FrameHandler();

    public static void main(String[] args) {
        frameHandler.run();
    }
}
