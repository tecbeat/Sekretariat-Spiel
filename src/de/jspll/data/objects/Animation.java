package de.jspll.data.objects;

import de.jspll.graphics.Camera;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import static de.jspll.graphics.ResourceHandler.FileType.PNG;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Jonas Sperling, Philipp Polland
 *
 * @version 1.0
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
    public void requestTextures(GameObject gO) {
        getParent(gO).getParent().getResourceHandler().requestTextureGroup(baseFile, cLength, frames, PNG);
    }

    /**
     * Draws every animation-frame one by one
     *
     * @param g2d         Graphics2D for drawing
     * @param elapsedTime delta time between frames
     * @param camera      selected Camera
     */
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

    /**
     * Restart the animation. This is achieved by setting {@code active} to true.
     * Then on the next frame the {@code textures} will be painted.
     */
    public void restartAnimation() {
        active = true;
    }

    /**
     * Start the animation. This is achieved by setting {@code active} to true.
     * Then on the next frame the {@code textures} will be painted.
     * Reset the {@code current_time} and {@code selectedTexture} to 0.
     */
    public void startAnimation() {
        active = true;
        current_time = 0;
        selectedTexture = 0;
    }

    /**
     * Stop the animation. This is achieved by setting {@code active} to false.
     * {@code textures} will not be painted while {@code active} is false.
     */
    public void stopAnimation() {
        active = false;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    /**
     * Update the time the animation takes to cycle through all {@code textures}.
     * @param duration float how long the animation takes
     */
    public void updateDuration(float duration) {
        current_time *= duration/ this.duration ;
        setDuration(duration);
    }
}
