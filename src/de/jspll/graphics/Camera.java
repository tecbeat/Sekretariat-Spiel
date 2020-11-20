package de.jspll.graphics;


import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.game.map.TileMap;
import de.jspll.handlers.GameObjectHandler;
import de.jspll.util.Logger;
import de.jspll.util.Vector2D;

import java.awt.*;

public class Camera {
    private final int width;
    private final int height;
    private double x;
    private double y;
    private double xCenter;
    private double yCenter;
    private float zoom;


    public Camera(int x, int y, int width, int height, float zoom) {
        this.xCenter = x;
        this.yCenter = y;
        this.zoom = zoom;
        this.width = width;
        this.height = height;
        updateXY();
    }

    private void updateXY() {
        x = xCenter - (width / 2f);
        y = yCenter - (height / 2f);
    }

    public void increase_x(float amount) {
        xCenter += amount;
        updateXY();
    }

    public void increase_y(float amount) {
        yCenter += amount;
        updateXY();
    }

    public void increase_zoom(float amount) {
        double xCenter = (this.xCenter / this.zoom) * (amount + this.zoom);
        double yCenter = (this.yCenter / this.zoom) * (amount + this.zoom);
        float zoom = this.zoom + amount;
        Logger.d.add("curr zoom: " + zoom);
        if (zoom < 0.01f) {
            return;
        }
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.zoom = zoom;
        updateXY();
    }

    public int[] revertTransform(int[] in) {
        return new int[]{revertXTransform(in[0]), revertYTransform(in[1])};
    }

    public int[] transform(int[] in) {
        return new int[]{applyXTransform(in[0]), applyYTransform(in[1])};
    }


    public int[] applyZoom(int[] in) {
        int[] out = new int[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = applyZoom(in[i]);
        }
        return out;
    }

    public int revertXTransform(int in) {
        return (int) ((in + x) / zoom);
    }

    public int revertYTransform(int in) {
        return (int) ((in + y) / zoom);
    }

    public int applyZoom(int in) {
        return (int) ((float) in * zoom);
    }

    public int applyXTransform(int in) {
        return (int) ((in * zoom) - x);
    }

    public int applyYTransform(int in) {
        return (int) ((in * zoom) - y);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void centerToPos(Point objectPos, Dimension dimension) {
        Point halfResolution = new Point(width / 2, height / 2);
        boolean[] OutSideCheck = checkIfOutside(objectPos);
        int[] transform = transform(new int[]{objectPos.x + dimension.width/2, objectPos.y + dimension.height/2});

        moveToBound();
        Point transformedPos = new Point(transform[0], transform[1]);

        Vector2D vec = new Vector2D(transformedPos, halfResolution);
        if (!OutSideCheck[1]) increase_y((float) -vec.y);
        if (!OutSideCheck[0]) increase_x((float) -vec.x);

    }

    public void centerToObject(GameObject object) {
        centerToPos(new Point(object.getX(), object.getY()), object.getDimension());
    }

    private void moveToBound() {
        while (x < 0) increase_x(1);
        while (y < 0) increase_y(1);

        //TODO Use with precaution
        while (x > 3552 + width) increase_x(-1);
        while (y > 3136 + width + 128) increase_y(-1);

        //Breite 3552
        //Höhe 3136
    }

    private boolean[] checkIfOutside(Point pos) {
        // TODO remove fixed values: values are realative to zoom
        return new boolean[]{pos.x < 480 || pos.x > 3072, pos.y < 272 || pos.y > 2864};
    }


    public int[] getRevertedBounds() {
        int[] out = new int[4];

        int[] origin = new int[]{0, 0};
        int[] maxCoords = new int[]{width, height};

        origin = revertTransform(origin);
        maxCoords = revertTransform(maxCoords);

        out[0] = origin[0] - 10;
        out[1] = origin[1] - 10;
        out[2] = maxCoords[0] + 10;
        out[3] = maxCoords[1] + 10;


        return out;
    }


}
