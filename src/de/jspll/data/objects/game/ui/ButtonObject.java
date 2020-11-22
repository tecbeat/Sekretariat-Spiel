package de.jspll.data.objects.game.ui;

import de.jspll.data.ChannelID;
import de.jspll.graphics.Camera;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ButtonObject extends MenuObject {
    private boolean mousedown;
    private int[] mousePos = new int[2];
    private boolean lockButton = false;

    public ButtonObject(){

    }


    public ButtonObject(String ID, String objectID, int x, int y, Dimension dimension, String text, boolean border) {
        super(ID, objectID, x, y, dimension, text, border);
    }

    @Override
    public char call(Object[] input) {

        super.call(input);
        if(input == null || input.length < 1){
            return 0;
        } else if(input[0] instanceof String) {
            if (((String) input[0]).contentEquals("input")) {
                if ((boolean) input[1]) {
                    mousedown = true;
                } else {
                    mousedown = false;
                }
                int[] pos = (int[]) input[5];
                mousePos[0] = pos[0];
                mousePos[1] = pos[1];
            }
        }

        return 0;
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        //super.paint(g, elapsedTime, camera, currStage);

        g.setColor(Color.BLUE);
        if(border){
            if(checkHover()){
                g.fillRect(camera.applyXTransform(x), camera.applyYTransform(y), camera.applyZoom((int) dimension.getWidth()),
                        camera.applyZoom((int) dimension.getHeight()));
            } else {
                g.drawRect(camera.applyXTransform(x), camera.applyYTransform(y), camera.applyZoom((int) dimension.getWidth()),
                        camera.applyZoom((int) dimension.getHeight()));
            }
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.PLAIN, 28));
        g.drawString(text, camera.applyXTransform(x+5), camera.applyYTransform(y+13));


    }

    protected boolean checkHover(){
        int xMin = getParent().getSelectedCamera().applyXTransform(x);
        int xMax = getParent().getSelectedCamera().applyXTransform(x+dimension.width);
        int yMin = getParent().getSelectedCamera().applyYTransform(y);
        int yMax = getParent().getSelectedCamera().applyYTransform(y + dimension.height);

        return mousePos[0] > xMin && mousePos[0] < xMax && mousePos[1] > yMin && mousePos[1] < yMax && !lockButton;
    }

    protected boolean checkClick(){
        return mousedown && checkHover();

    }

}