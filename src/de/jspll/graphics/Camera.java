package de.jspll.graphics;

public class Camera {
    public Camera(int x, int y, float zoom){
        this.x = x;
        this.y = y;
        this.zoom = zoom;
    }
    public int x;
    public int y;
    public float zoom;
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
