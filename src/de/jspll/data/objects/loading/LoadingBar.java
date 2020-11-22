package de.jspll.data.objects.loading;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.graphics.Camera;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by reclinarka on 05-Nov-20.
 */
public class LoadingBar extends GameObject {

    public LoadingBar(ProgressReporter progressReporter){
        this.progressReporter = progressReporter;
    }

    protected ProgressReporter progressReporter;
    private AtomicReference<String> message = new AtomicReference<>(null);

    float progress = 0;

    @Override
    public char update(float elapsedTime) {
        if(progress == -1){
            getParent().delete(this);

            progressReporter = null;
        }
        return 0;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        if(progress == -1){
            return;
        }
        super.paint(g, elapsedTime, camera, currStage);
        Graphics2D g2d = (Graphics2D) g;
        int screenWidth = (int) g2d.getClipBounds().getWidth();
        int screenHeight = (int) g2d.getClipBounds().getHeight();
        int x = screenWidth / 4;
        //int y = screenHeight / 2 - screenHeight / 10;
        int y = screenHeight / 10 * 8;
        int height = 50;
        int fullWidth = screenWidth / 2;
        if(progressReporter != null)
            progress = progressReporter.getProgress();
        int width = (int) ((fullWidth - 20) * progress);
        g2d.setColor(Color.gray);
        g2d.fillRoundRect(x,y ,fullWidth,height,height/2,height/2);
        x += 10;
        if(width + 5 > fullWidth - 20) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRoundRect(x,y,width + 5,height,height / 2, height / 2);
        }
        g2d.setColor(Color.BLACK);
        g2d.fillRoundRect(x,y + 2,width,height - 4,height / 2, height / 2);
        if(message.get() != null){
            g2d.setColor(Color.CYAN);
            g2d.drawString(message.get(),x + 5,y+(height * 3/5));
        }

    }
}
