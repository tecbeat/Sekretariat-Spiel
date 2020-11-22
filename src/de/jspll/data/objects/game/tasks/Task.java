package de.jspll.data.objects.game.tasks;

import de.jspll.data.ChannelID;
import de.jspll.graphics.Camera;

import java.awt.*;

public interface Task {

    void activate();

    boolean isActive();

    char update(float elapsedTime);

    char call(Object[] input);

    void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage);

    void setHolder(TaskHolder holder);

    TaskHolder getHolder();
}
