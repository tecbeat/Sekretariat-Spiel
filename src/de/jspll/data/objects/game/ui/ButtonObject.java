package de.jspll.data.objects.game.ui;

import de.jspll.data.ChannelID;
import de.jspll.graphics.Camera;
import java.awt.*;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Lukas Becker
 *
 * @version 1.0
 */
public class ButtonObject extends MenuObject {
    //mouse variables
    private boolean mousedown;
    private int[] mousePos = new int[2];
    private boolean lockButton = false;


    public ButtonObject(){

    }

    public ButtonObject(String ID, String objectID, int x, int y, Dimension dimension, String text, boolean border) {
        super(ID, objectID, x, y, dimension, text, border);
    }

    //Reads mouse position and clicks
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

    //paints the buttons
    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        int xCenter = camera.getCenter()[0];
        int yCenter = camera.getCenter()[1];

        x = xCenter - (camera.applyZoom(dimension.width) / 2);
        y = yCenter - camera.applyZoom(40) - ((camera.applyZoom(dimension.height) - offset * camera.applyZoom(50)) / 2);

        g.setColor(Color.BLUE);
        if(border){
            if(checkHover()){
                g.fillRect(x, y, camera.applyZoom((int) dimension.getWidth()),
                        camera.applyZoom((int) dimension.getHeight()));
            } else {
                g.drawRect(x,y, camera.applyZoom((int) dimension.getWidth()),
                        camera.applyZoom((int) dimension.getHeight()));
            }
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Kristen ITC", Font.PLAIN, camera.applyZoom(14)));
        g.drawString(text, x+camera.applyZoom(5), y+camera.applyZoom(14));
    }

    protected boolean checkHover(){
        int xMin = x;
        int xMax = x + getParent().getSelectedCamera().applyZoom(x);
        int yMin = y;
        int yMax = y +
                getParent().getSelectedCamera().applyZoom( dimension.height);

        return mousePos[0] > xMin && mousePos[0] < xMax && mousePos[1] > yMin && mousePos[1] < yMax && !lockButton;
    }

    protected boolean checkClick(){
        return mousedown && checkHover();
    }
}
