package de.jspll.data.objects;

import de.jspll.graphics.Camera;
import de.jspll.graphics.ResourceHandler;
import de.jspll.util.json.JSONObject;
import de.jspll.util.json.JSONValue;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by reclinarka on 28-Oct-20.
 */
public class Texture {

    protected Point pos;
    protected String textureKey;
    protected BufferedImage image;
    protected Dimension dimension;
    protected GameObject parent;

    public Texture(String textureKey, Point pos, Dimension dimension, GameObject parent){
        this.parent = parent;
        this.textureKey = textureKey;
        this.pos = pos;
        this.dimension = dimension;
        this.parent = parent;
    }

    public Texture(BufferedImage image, Point pos, Dimension dimension, GameObject parent){
        this.parent = parent;
        this.image = image;
        this.pos = pos;
        this.dimension = dimension;
        this.parent = parent;
    }

    public Texture(){

    }

    protected void loadTextures(){
        if(getParent().getParent().getResourceHandler().isAvailable(textureKey))
            image = parent.getParent().getResourceHandler().getTexture(textureKey, ResourceHandler.FileType.PNG);
    }

    protected void requestTextures(){
        getParent().getParent().getResourceHandler().requestTexture(textureKey, ResourceHandler.FileType.PNG);
    }

    public void draw(Graphics2D g2d, float elapsedTime, Camera camera){
        if(image == null){
            loadTextures();
            if(image == null){
                return;
            }
        }
        g2d.drawImage(image,camera.applyXTransform(pos.x),camera.applyYTransform(pos.y),camera.applyZoom(dimension.width),camera.applyZoom(dimension.height),null);

    }

    public void draw(Graphics2D g2d, float elapsedTime, Camera camera, int[] offset){
        if(image == null){
            loadTextures();
            if(image == null){
                return;
            }
        }
        g2d.drawImage(image,camera.applyXTransform(pos.x + offset[0]),camera.applyYTransform(pos.y + offset[1]),camera.applyZoom(dimension.width),camera.applyZoom(dimension.height),null);

    }

    public String getTextureKey() {
        return textureKey;
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

    public GameObject getParent() {
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

    public static Texture parseJsonObject(JSONObject jsonObject){
        HashMap<String, JSONValue> object = jsonObject.getObject();
        if(object.containsKey("type")){
            JSONValue type = object.get("type");
            if(((String)type.getValue()).contentEquals("Texture")){
                //JSON definitely of type Texture

            }
        }
        return null;
    }

    public JSONObject toJson(){
        HashMap<String, JSONValue> jsonBuilder = new HashMap<>();

        return new JSONObject().setObject(jsonBuilder);
    }
}
