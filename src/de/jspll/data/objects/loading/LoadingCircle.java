package de.jspll.data.objects.loading;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.graphics.Camera;
import de.jspll.util.PaintingUtil;
import de.jspll.util.Vector2D;

import java.awt.*;

/**
 * Created by reclinarka on 06-Nov-20.
 */
public class LoadingCircle extends GameObject {

    public LoadingCircle(String ID, String objectID, int x, int y, double radius, double distance, Dimension dimension){
        super( ID,  objectID,  x,  y,  dimension);
        this.radius = radius;
        this.distance = distance;
    }

    private double radius, distance;

    RotatingCircle circles[];


    private void init(){
        circles = new RotatingCircle[8];
        for(int i = 0; i < circles.length - 1; i++){
            double quarterPi = Math.PI / 4d;
            circles[i] = new RotatingCircle((float) quarterPi * i, x, y,distance,radius);
        }
    }


    @Override
    public char update(float elapsedTime) {
        return 0;
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        super.paint(g, elapsedTime, camera, currStage);
        if(circles == null || circles[0] == null){
            init();
        }

        for (RotatingCircle circle : circles){
            circle.paint(g,elapsedTime,camera);
        }


    }
}

class RotatingCircle {

    double offset;
    double rotation = 0;
    double radius;
    double x;
    double y;
    float length = 20f;

    Vector2D vec;

    public RotatingCircle(double offset,double x, double y, double distance, double radius){
        this.offset =  -1 * offset;
        this.x = x;
        this.y = y;
        this.radius = radius;
        vec = (new Vector2D(1,0).scale(distance));
    }





    public void paint(Graphics g, float elapsedTime, Camera camera) {
        Vector2D circle = new Vector2D(0,1).scale(20);

        if(offset > 360 || offset < 0){
            if(offset > 360){
                offset = 0;

            }

        } else {
            offset += 05;
            double progress = offset / 360;
            g.setColor(Color.MAGENTA);
        }

        PaintingUtil.paintCircleFromCenter(circle.x,  circle.y, radius, (Graphics2D) g);


    }
}
