package de.jspll.graphics;

public class Camera {
    public Camera(int x, int y, int width, int height, float zoom){
        this.xCenter = x;
        this.yCenter = y;
        this.zoom = zoom;
        this.width = width;
        this.height = height;
        updateXY();
    }
    private double x;
    private double y;
    private double xCenter;
    private double yCenter;
    private int width;
    private int height;
    private float zoom;

    private void updateXY(){
        x = xCenter - (width/2f);
        y = yCenter - (height / 2f);
    }

    public void increase_x(int amount){
        xCenter += amount;
        updateXY();
    }
    public void increase_y(int amount){
        yCenter += amount;
        updateXY();
    }

    public void increase_zoom(float amount){
        xCenter = (xCenter/zoom) * (amount+zoom);
        yCenter = (yCenter/zoom) * (amount+zoom);
        zoom += amount;
        updateXY();
    }

    public int applyZoom(int in){
        return (int)((float)in * zoom);
    }
    public int applyXTransform(int in){
        return (int) ((in * zoom) - x);
    }
    public int applyYTransform(int in){
        return (int) ((in * zoom) - y);
    }
}
