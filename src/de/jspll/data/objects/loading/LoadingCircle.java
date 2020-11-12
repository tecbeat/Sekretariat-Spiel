package de.jspll.data.objects.loading;

import de.jspll.data.objects.GameObject;
import de.jspll.graphics.Camera;

import java.awt.*;

/**
 * Created by reclinarka on 06-Nov-20.
 */
public class LoadingCircle extends GameObject {


    RotatingCircle circles[] = new RotatingCircle[8];


    private void init(){
        for(int i = 0; i < circles.length - 1; i++){
            double quarterPi = Math.PI / 4d;
            circles[i] = new RotatingCircle((float) quarterPi * i);
        }
    }


    @Override
    public char update(float elapsedTime) {
        return 0;
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera) {
        super.paint(g, elapsedTime, camera);


    }
}

class RotatingCircle {

    float offset;
    float rotation = 0;
    int x;
    int y;
    float length = 20f;

    float[] vec = new float[]{0f,1f};

    public RotatingCircle(float offset){
        this.offset = offset;
    }

    private void rotate(){

        double rx = (vec[0] * Math.cos(rotation)) - (vec[1] * Math.sin(rotation));
        double ry = (vec[0] * Math.sin(rotation)) + (vec[1] * Math.cos(rotation));
        vec[0] = (float) rx;
        vec[1] = (float) ry;
    }

    public void paint(Graphics g, float elapsedTime, Camera camera) {

        if(offset > 360 || offset < 0){
            rotation = 0;

        }
        g.setColor(Color.MAGENTA);

    }
}
