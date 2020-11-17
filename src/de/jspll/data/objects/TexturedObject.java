package de.jspll.data.objects;

import de.jspll.data.ChannelID;
import de.jspll.graphics.Camera;

import java.awt.*;

/**
 * Created by reclinarka on 28-Oct-20.
 */
public class TexturedObject extends GameObject {

    public TexturedObject() {
        super();
    }

    public TexturedObject(String ID, String objectID, int x, int y, Dimension dimension ) {
        super(ID, objectID, x, y, dimension);
    }

    public TexturedObject(String ID, String objectID, int x, int y, Dimension dimension, Texture texture) {
        super(ID, objectID, x, y, dimension);
        this.texture = texture;
    }

    public TexturedObject(String ID, String objectID, int x, int y, Dimension dimension, ChannelID channels, Texture texture) {
        super(ID, objectID, x, y, dimension, channels);
        this.texture = texture;
    }


    protected Texture texture;
    private boolean textureLoaded;

    public boolean isTextureLoaded() {
        return textureLoaded;
    }

    public void setTextureLoaded(boolean textureLoaded) {
        this.textureLoaded = textureLoaded;
    }

    public void requestTexture(){
        texture.requestTextures();
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    protected void loadTexture(){
        texture.loadTextures();
        if(texture.isLoaded())
            textureLoaded = true;
    }

    @Override
    public char update(float elapsedTime) {
        return super.update(elapsedTime);
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera) {
        super.paint(g, elapsedTime, camera);
        if(!textureLoaded){
            loadTexture();
        }
        drawFrame(g,elapsedTime,camera);
    }

    protected void drawFrame(Graphics g, float elapsedTime, Camera camera){
        Graphics2D g2d = (Graphics2D) g;
        texture.draw(g2d,elapsedTime,camera);
    }

    @Override
    public char call(Object[] input) {
        super.call(input);
        if(input == null || input.length < 1 ){
            if(input[0] instanceof String){
                if( ((String) input[0]).contentEquals("getTexture")){
                    texture.requestTextures();
                }
            }
        }
        return 0;
    }




}
