package de.jspll.data.objects.loading;

import de.jspll.data.objects.GameObject;
import de.jspll.graphics.Camera;

import java.awt.*;

/**
 * Created by reclinarka on 05-Nov-20.
 */
public class LoadingBar extends GameObject {

    public LoadingBar(ProgressReporter progressReporter){
        this.progressReporter = progressReporter;
    }

    protected ProgressReporter progressReporter;

    @Override
    public char update(float elapsedTime) {

        return 0;
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera) {
        super.paint(g, elapsedTime, camera);
        Graphics2D g2d = (Graphics2D) g;
        int screenWidth = (int) g2d.getClipBounds().getWidth();
        int screenHeight = (int) g2d.getClipBounds().getHeight();
        int x = screenWidth / 4;
        int y = screenHeight / 2 - screenHeight / 10;
        int height = 50;
        int fullWidth = screenWidth / 2;
        int width = (int) ((fullWidth - 20) * progressReporter.getProgress());
        g2d.setColor(Color.gray);
        g2d.fillRoundRect(x,y,fullWidth,height,height/2,height/2);
        x += 10;
        if(width + 5 > fullWidth - 20) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRoundRect(x,y,width + 5,height,height / 2, height / 2);
        }
        g2d.setColor(Color.BLACK);
        g2d.fillRoundRect(x,y,width,height,height / 2, height / 2);

    }
}
