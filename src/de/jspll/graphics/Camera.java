package de.jspll.graphics;

import de.jspll.data.objects.GameObject;
import de.jspll.util.Logger;
import de.jspll.util.Vector2D;

import java.awt.*;

public class Camera {
    private final int width;
    private final int height;
    private final double smoothness = 1.5;
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
        return (int) Math.round((in + x) / zoom);
    }

    public int revertYTransform(int in) {
        return (int) Math.round((in + y) / zoom);
    }

    public int applyZoom(int in) {
        return Math.round((float) in * zoom);
    }

    public int applyXTransform(int in) {
        return (int) Math.round((in * zoom) - x);
    }

    public int applyYTransform(int in) {
        return (int) Math.round((in * zoom) - y);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * This Function centers the camera to the given Object.
     *
     * @param objectPos   Point of the Object
     * @param dimension   Width/Height of the Object
     * @param elapsedTime Time that has elapsed since the last time this function was called
     */

    public void centerToPos(Point objectPos, Dimension dimension, float elapsedTime) {
        Point halfResolution = new Point(width / 2, height / 2);
        boolean[] OutSideCheck = checkIfCameraStopMovement(objectPos);
        int[] transform = transform(new int[]{objectPos.x + dimension.width / 2, objectPos.y + dimension.height / 2});


        //moveToBound();
        Point transformedPos = new Point(transform[0], transform[1]);

        Vector2D vec = new Vector2D(transformedPos, halfResolution);

        //most reliable method
        if (!OutSideCheck[0]) {
            increase_x((float) (-(vec.x) * elapsedTime * smoothness));
        }
        if (!OutSideCheck[1]) {
            increase_y((float) (-(vec.y) * elapsedTime * smoothness));
        }


    }

    /**
     * This Function gives the opportunity to use a GameObject as the reference
     *
     * @param object      GameObject which will be centred in the Screen Middle
     * @param elapsedTime Time that has elapsed since the last time this function was called
     */

    public void centerToObject(GameObject object, float elapsedTime) {
        centerToPos(new Point(object.getX(), object.getY()), object.getDimension(), elapsedTime);
    }


    private void moveToBound() {
        while (x < 0) increase_x(1);
        while (y < 0) increase_y(1);

        //Use with precaution
//        while (x > 3552 + width) increase_x(-1);
//        while (y > 3136 + height*2) increase_y(-1);

        //Breite 3552
        //Höhe 3136
    }

    /**
     * This function is used to determine if the camera should stop moving
     *
     * @param pos Point of the
     * @return two-dimensional Boolean Array [0] = X-Axis, [1] = Y-Axis
     */

    private boolean[] checkIfCameraStopMovement(Point pos) {
        // TODO remove fixed values: values are relative to zoom

        //it should be a lg function, but idk how :D
        //https://imgur.com/a/d48dVB1

        int x_right = (int) (Math.log(zoom) + 2576);
        int y_down = (int) (Math.log(zoom) + 2564);
        int x_left = (int) (Math.log(zoom) + 944);
        int y_up = (int) (Math.log(zoom) + 509);

        return new boolean[]{pos.x < 370 || pos.x > 3150,
                pos.y < 184 || pos.y > 2890};
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
