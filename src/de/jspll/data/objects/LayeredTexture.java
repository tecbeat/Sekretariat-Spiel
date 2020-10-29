package de.jspll.data.objects;

import de.jspll.data.objects.Texture;
import de.jspll.data.objects.TexturedObject;
import de.jspll.graphics.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

import static de.jspll.graphics.ResourceHandler.FileType.PNG;

/**
 * Created by reclinarka on 28-Oct-20.
 */
public class LayeredTexture extends Texture{


    protected String baseFile;
    protected int cLength = 1;
    protected BufferedImage[] textures;
    protected boolean[] layerSelection;

    public LayeredTexture(String baseFile, int layers, Point p, Dimension dimension, TexturedObject parent) {
        super(baseFile, p, dimension, parent);
        textures = new BufferedImage[layers];
        layerSelection = new boolean[layers];
        init();
        this.baseFile = baseFile;

    }

    private void init(){
        for(int i = 0; i < layerSelection.length; i++){
            layerSelection[i] = true;
        }
    }

    public LayeredTexture(String baseFile,int cLength,int layers,Point p, Dimension dimension, TexturedObject parent) {
        super(baseFile, p, dimension, parent);
        textures = new BufferedImage[layers];
        layerSelection = new boolean[layers];
        init();
        this.cLength = cLength;
        this.baseFile = baseFile;
    }

    public void toggleLayer(int layer){
        layerSelection[layer] = !layerSelection[layer];
    }

    @Override
    protected void loadTextures() {
        textures = getParent().getParent().getResourceHandler().getImageGroup(baseFile,cLength,textures.length, PNG);
    }

    @Override
    public void draw(Graphics2D g2d, float elapsedTime, Camera camera) {
        if(textures == null || textures.length == 0){
            return;
        }
        if(textures[textures.length-1] == null){
            loadTextures();
        }
        for(int i = 0; i < textures.length; i++){
            if(layerSelection[i]) {
                g2d.drawImage(textures[i], camera.applyXTransform(pos.x), camera.applyYTransform(pos.y),
                        camera.applyZoom(dimension.width), camera.applyZoom(dimension.height), null);
            }
        }
    }
}
