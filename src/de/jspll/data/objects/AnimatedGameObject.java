package de.jspll.data.objects;

import de.jspll.graphics.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by reclinarka on 27-Oct-20.
 */
public class AnimatedGameObject extends GameObject{



    protected int selectedTexture;
    protected String[] resources;
    protected BufferedImage[] textures;
    protected ArrayList<KeyPoint> path;

    private void loadTextures(){
        textures = getParent().getTextureHandler().getTextures(resources);
    }

    @Override
    public char update(float elapsedTime) {
        return super.update(elapsedTime);
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera) {
        super.paint(g, elapsedTime, camera);
        drawFrame(g,elapsedTime,camera, new Point(x,y));
    }

    protected void drawFrame(Graphics g, float elapsedTime, Camera camera,Point point){
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(textures[selectedTexture],camera.applyXTransform(point.x),camera.applyYTransform(point.y),dimension.width,dimension.height,null);


    }

    @Override
    public char call(Object[] input) {
        return super.call(input);
    }
    protected class KeyPoint {

        public KeyPoint(double timestamp, int selectedImage) {
            this.timestamp = timestamp;
            this.selectedImage = selectedImage;
            this.transformations = false;
        }

        public KeyPoint(double timestamp, int selectedImage, Dimension dimension){
            this.timestamp = timestamp;
            this.selectedImage = selectedImage;
            this.transformations = true;
            this.dimension = dimension;
        }
        protected double timestamp;
        protected int selectedImage;
        protected boolean transformations;
        protected Dimension dimension;
    }

}
