package de.jspll.data.objects;

import de.jspll.graphics.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by reclinarka on 28-Oct-20.
 */
public class Texture {

    protected Point pos;
    protected String file;
    protected BufferedImage image;
    protected Dimension dimension;
    protected TexturedObject parent;

    public Texture(String file,Point pos, Dimension dimension, TexturedObject parent){
        this.parent = parent;
        this.file = file;
        this.pos = pos;
        this.dimension = dimension;
        this.parent = parent;
    }

    public Texture(BufferedImage image,Point pos, Dimension dimension, TexturedObject parent){
        this.parent = parent;
        this.image = image;
        this.pos = pos;
        this.dimension = dimension;
        this.parent = parent;
    }

    public Texture(){

    }

    protected void loadTextures(){
        parent.getParent().getResourceHandler().getImage(file);
    }

    public void draw(Graphics2D g2d,float elapsedTime,Camera camera){
        g2d.drawImage(image,camera.applyXTransform(pos.x),camera.applyYTransform(pos.y),camera.applyZoom(dimension.width),camera.applyZoom(dimension.height),null);

    }

    public Point getPos() {
        return pos;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public BufferedImage getImage() {
        return image;
    }

    public TexturedObject getParent() {
        return parent;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setParent(TexturedObject parent) {
        this.parent = parent;
    }
}
