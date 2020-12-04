package de.jspll.data.objects.game.tasks;

import com.google.gson.annotations.Expose;
import de.jspll.data.ChannelID;
import de.jspll.graphics.Camera;
import java.awt.*;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Samuel Assmann, Philipp Polland
 *
 * @version 1.0
 */
@Deprecated
public class ExampleTask implements Task{
    @Override
    public void requestTexture() {

    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public void loadTextures() {

    }

    private Color maskColor = new Color(0, 0, 0, 172);
    @Expose(deserialize = false, serialize = false)
    private TaskHolder holder;
    private boolean active = false;

    //for testing
    private float countDown = 10;

    public boolean isActive() {
        return active;
    }

    public void activate(){
        countDown = 10;
        this.active = true;
    }

    public void setHolder(TaskHolder holder) {
        this.holder = holder;
    }

    public TaskHolder getHolder() {
        return holder;
    }

    public Color getMaskColor() {
        return maskColor;
    }

    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {

        g.setColor(maskColor);
        g.fillRect(0, 0, camera.getWidth(), camera.getHeight());
        g.setColor(Color.WHITE);
        g.drawString("t-" + countDown,camera.getWidth()/2,camera.getHeight()/2);

        Point mousePos = getMousePos();
        if(mousePos != null)
            g.fillRect(mousePos.x-10,mousePos.y-10,20,20);

        //for testing, remove when developing Tasks
        countDown -= elapsedTime;
        if(countDown < 0){
            active = false;
            return;
        }
    }

    private Point getMousePos(){
        return getHolder().getParent().getMousePos();
    }

    public char update(float elapsedTime) {
        return 0;
    }

    public char call( Object[] input){ // input[0] always = "toTask"
        return 0;
    }


}
