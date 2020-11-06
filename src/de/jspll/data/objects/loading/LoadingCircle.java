package de.jspll.data.objects.loading;

import de.jspll.data.objects.GameObject;
import de.jspll.graphics.Camera;

import java.awt.*;

/**
 * Created by reclinarka on 06-Nov-20.
 */
public class LoadingCircle extends GameObject {

    @Override
    public char update(float elapsedTime) {
        return 0;
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera) {
        super.paint(g, elapsedTime, camera);

    }
}
