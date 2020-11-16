package de.jspll.data.objects;

import de.jspll.graphics.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.jspll.graphics.ResourceHandler.FileType.PNG;

/**
 * Created by reclinarka on 27-Oct-20.
 */
public class Animation extends Texture {


    protected AtomicBoolean textureInitialized = new AtomicBoolean(false);
    protected boolean looping = false;
    protected boolean active = false;
    protected float duration = 0;
    protected float current_time = 0f;
    protected float inc = 1;
    protected int cLength = 5;
    protected String baseFile;
    protected int frames;
    protected int selectedTexture = 0;
    protected Texture[] textures;
    protected ArrayList<KeyPoint> path;
    private boolean loaded = false;

    public Animation(String baseFile, int frames, Point pos, Dimension dimension, TexturedObject parent, float duration) {
        super(baseFile, pos, dimension, parent);
        this.duration = duration;
        this.frames = frames;
        this.baseFile = baseFile;
    }

    public double getCurrentTimePos() {
        return (double) current_time / (double) duration;
    }

    public void loadTextures() {
        if (!getParent().getParent().getResourceHandler().isAvailable(baseFile, cLength, frames, PNG))
            return;
        BufferedImage[] images = parent.getParent().getResourceHandler().getTextureGroup(baseFile, cLength, frames, PNG);
        if (images == null) {
            return;
        }
        textures = new Texture[frames];
        for (int i = 0; i < textures.length; i++) {
            textures[i] = new Texture(images[i], pos, dimension, parent);
        }
        loaded = true;
    }

    @Override
    public void requestTextures() {
        getParent().getParent().getResourceHandler().requestTextureGroup(baseFile, cLength, frames, PNG);
    }

    @Override
    public void draw(Graphics2D g2d, float elapsedTime, Camera camera) {
        super.draw(g2d, elapsedTime, camera);
        if (!loaded) {
            loadTextures();
        }
        if (!active) {
            return;
        }
        if (textures == null)
            return;
        if (!looping && current_time > duration) {
            textures[textures.length - 1].draw(g2d, elapsedTime, camera);
            return;
        }
        float increment = duration / frames;
        current_time += elapsedTime;
        if (increment * inc < current_time) {
            inc++;
            selectedTexture++;

        }

        textures[selectedTexture % textures.length].draw(g2d, elapsedTime, camera);
    }

    public void drawFrame(Graphics g, float elapsedTime, Camera camera, Point point) {
        Graphics2D g2d = (Graphics2D) g;

    }

    public void startAnimation(Boolean start) {
        active = true;
        if (start) {
            current_time = 0;
            selectedTexture = 0;
        }
    }

    public void startAnimation() {
        active = true;
        current_time = 0;
        selectedTexture = 0;
    }

    public void stopAnimation() {
        active = false;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    protected class KeyPoint {

        protected double timestamp;
        protected int selectedImage;
        protected boolean transformations;
        protected Dimension dimension;

        public KeyPoint(double timestamp, int selectedImage) {
            this.timestamp = timestamp;
            this.selectedImage = selectedImage;
            this.transformations = false;
        }

        public KeyPoint(double timestamp, int selectedImage, Dimension dimension) {
            this.timestamp = timestamp;
            this.selectedImage = selectedImage;
            this.transformations = true;
            this.dimension = dimension;
        }
    }

}
