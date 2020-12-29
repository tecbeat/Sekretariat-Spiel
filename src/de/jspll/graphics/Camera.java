package de.jspll.graphics;

import de.jspll.data.objects.GameObject;
import de.jspll.util.Logger;
import de.jspll.util.Vector2D;

import java.awt.*;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Jonas Sperling, Lukas Becker (geringe Beteiligung), Philipp Polland
 *
 * @version 1.0
 */
public class Camera {
    private final double smoothness = 1.5;
    private int width;
    private int height;
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
        x = xCenter - (width / 2);
        y = yCenter - (height / 2);
    }

    /**
     * Increase the position of the camera in x-direction by {@code amount}.
     * @param amount float
     */
    public void increase_x(float amount) {
        xCenter += amount;
        updateXY();
    }

    /**
     * Increase the position of the camera in y-direction by {@code amount}.
     * @param amount float
     */
    public void increase_y(float amount) {
        yCenter += amount;
        updateXY();
    }

    /**
     * Increase the zoom of the camera by {@code amount}.
     * @param amount float
     */
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

    /**
     * The Camera and Player use different forms of coordinate-systems. <br>
     * Camera: (0,0) is in the centre. <br>
     * Player: (0,0) is on the top left corner. <br>
     *
     * @param in 2-dimensional int array in the Camera-Coordinate-System
     * @return 2-dimensional int array of reverted values
     */
    public int[] revertTransform(int[] in) {
        return new int[]{revertXTransform(in[0]), revertYTransform(in[1])};
    }

    /**
     * The Camera and Player use different forms of coordinate-systems. <br>
     * Camera: (0,0) is in the centre. <br>
     * Player: (0,0) is on the top left corner. <br>
     *
     * @param in 2-dimensional int array in the Player-Coordinate-System
     * @return 2-dimensional int array of transformed values
     */
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

    /**
     * The Camera and Player use different forms of coordinate-systems.<br>
     * Camera: (0,0) is in the centre.<br>
     * Player: (0,0) is on the top left corner.<br>
     *
     * @param in x coordinate in Camera-Coordinate-System
     * @return transformed value
     */
    public int revertXTransform(int in) {
        return (int) Math.round((in + x) / zoom);
    }

    /**
     * The Camera and Player use different forms of coordinate-systems.<br>
     * Camera: (0,0) is in the centre.<br>
     * Player: (0,0) is on the top left corner.<br>
     *
     * @param in y coordinate in Camera-Coordinate-System
     * @return transformed value
     */
    public int revertYTransform(int in) {
        return (int) Math.round((in + y) / zoom);
    }

    public int applyZoom(int in) {
        return Math.round((float) in * zoom);
    }

    /**
     * The Camera and Player use different forms of coordinate-systems.<br>
     * Camera: (0,0) is in the centre.<br>
     * Player: (0,0) is on the top left corner.<br>
     *
     * @param in y coordinate in Player-Coordinate-System
     * @return transformed value
     */
    public int applyXTransform(int in) {
        return (int) Math.round((in * zoom) - x);
    }

    /**
     * The Camera and Player use different forms of coordinate-systems.<br>
     * Camera: (0,0) is in the centre.<br>
     * Player: (0,0) is on the top left corner.<br>
     *
     * @param in y coordinate in Player-Coordinate-System
     * @return transformed value
     */
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
     * Center the camera to the given {@code GameObject}s position.
     *
     * @param objectPos   Point of the Object
     * @param dimension   Width/Height of the Object
     * @param elapsedTime delta time between frames
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
     * Center the camera to the given x- and y- coordinates.
     * The camera gets centred to it instantly.
     * For a smooth transition to a {@code GameObject} use {@code centreToPos()}
     *
     * @param x coordinate
     * @param y coordinate
     */
    public void instantlyCenterToPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void instantlyZoom(float level) {
        this.zoom = level;
    }

    /**
     * This Function gives the opportunity to use a GameObject as the reference
     *
     * @param object      GameObject which will be centred in the Screen Middle
     * @param elapsedTime delta time between frames
     */
    public void centerToObject(GameObject object, float elapsedTime) {
        centerToPos(new Point(object.getX(), object.getY()), object.getDimension(), elapsedTime);
    }


    /**
     * This function is used to determine if the camera should stop moving.
     *
     * @param pos Point of the
     * @return two-dimensional Boolean Array [0] = X-Axis, [1] = Y-Axis
     */
    private boolean[] checkIfCameraStopMovement(Point pos) {
        //it should be a lg function
        //https://imgur.com/a/d48dVB1
        return new boolean[]{pos.x < 465 || pos.x > 3056,
                pos.y < 240 || pos.y > 2833};
    }

    public int[] getRevertedBounds() {
        int[] out = new int[4];

        int[] origin = new int[]{0, 0};
        int[] maxCoords = new int[]{width + 20, height + 20};

        origin = revertTransform(origin);
        maxCoords = revertTransform(maxCoords);

        out[0] = origin[0] - 10;
        out[1] = origin[1] - 10;
        out[2] = maxCoords[0] + 10;
        out[3] = maxCoords[1] + 10;

        return out;
    }

    public void updateWindowSize(Graphics2D g) {
        width = (int) Math.round(g.getClipBounds().getWidth());
        height = (int) Math.round(g.getClipBounds().getHeight());
        updateXY();

    }
}
