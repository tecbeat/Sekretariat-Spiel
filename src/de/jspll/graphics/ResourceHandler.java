package de.jspll.graphics;

import de.jspll.data.objects.TexturedObject;
import de.jspll.handlers.GameObjectHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Stack;

/**
 * Created by reclinarka on 27-Oct-20.
 */
public class ResourceHandler {
    private GameObjectHandler parent;
    private Stack<TexturedObject> loadingQueue = new Stack<>();

    public Stack<TexturedObject> getLoadingQueue() {
        return loadingQueue;
    }

    public ResourceHandler(GameObjectHandler parent){
        this.parent = parent;
    }

    public BufferedImage getImage(String texture){
        try {
            BufferedImage image = ImageIO.read(this.getClass().getResource(texture));
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public BufferedImage[] getImages(String[] textures){
        BufferedImage[] out = new BufferedImage[textures.length];
        for(int i = 0; i < textures.length; i++) {
            out[i] = getImage(textures[i]);
        }
        return out;


    }
     public BufferedImage[] getImageGroup(String baseFile, int cLength, int count, FileType fileType) {
        BufferedImage[] textures = new BufferedImage[count];
        for (int i = 0; i < count; i++){
            textures[i] = getImage(baseFile + String.format("%0" + cLength + "d",i) + fileType.valueOf());
        }

        return textures;
     }

     public enum FileType{
         PNG(".png");
         FileType(String fileEnding){
             this.fileEnding = fileEnding;
         }
         private String fileEnding;
         public String valueOf(){
             return fileEnding;
         }
     }
}
