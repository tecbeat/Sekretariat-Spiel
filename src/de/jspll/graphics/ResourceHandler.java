package de.jspll.graphics;

import de.jspll.handlers.GameObjectHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by reclinarka on 27-Oct-20.
 */
public class ResourceHandler extends Thread {
    private GameObjectHandler parent;
    private AtomicBoolean running = new AtomicBoolean(true);
    private LinkedBlockingQueue<String> loadingQueue = new LinkedBlockingQueue<>(Integer.MAX_VALUE);
    private ConcurrentHashMap<String, BufferedImage> textures = new ConcurrentHashMap<>();

    public LinkedBlockingQueue<String> getLoadingQueue() {
        return loadingQueue;
    }

    public ResourceHandler(GameObjectHandler parent){
        this.parent = parent;
    }

    public boolean isAvailable(String key){
        return textures.containsKey(key);
    }

    public boolean isAvailable(String key, FileType type){
        return textures.containsKey(key + type.fileEnding);
    }

    public boolean isAvailable(String baseFile, int cLength, int count, FileType fileType){
        for (int i = 0; i < count; i++){
            String key = baseFile + String.format("%0" + cLength + "d",i) + fileType.valueOf();
            if(!this.textures.containsKey(key))
                return false;
        }

        return true;
    }

    public BufferedImage getTexture(String textureKey){
        if(textures.containsKey(textureKey)){
            return textures.get(textureKey);
        }
        try {
            loadingQueue.put(textureKey);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BufferedImage getTexture(String textureKey, FileType type){

        if(textures.containsKey(textureKey + type.fileEnding)){
            return textures.get(textureKey + type.fileEnding);
        }
        try {
            loadingQueue.put(textureKey + type.fileEnding);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BufferedImage[] getTextureGroup(String baseFile, int cLength, int count, FileType fileType) {
        BufferedImage[] textures = new BufferedImage[count];
        for (int i = 0; i < count; i++){
            String key = baseFile + String.format("%0" + cLength + "d",i) + fileType.valueOf();
            if(this.textures.containsKey(key))
                textures[i] = this.textures.get(key);
        }

        return textures;
    }


    public BufferedImage loadImage(String texture){
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
            out[i] = loadImage(textures[i]);
        }
        return out;


    }

    //cLength represents the length of the longest number in the group
    //i.e for chair0001.jpg the cLength would be 4 (because 1 number + 3 zeros = 4 total)
     public BufferedImage[] loadImageGroup(String baseFile, int cLength, int count, FileType fileType) {
        BufferedImage[] textures = new BufferedImage[count];
        for (int i = 0; i < count; i++){
            textures[i] = loadImage(baseFile + String.format("%0" + cLength + "d",i) + fileType.valueOf());
        }

        return textures;
     }


    @Override
    public void run() {
        super.run();
        while(running.get()){
            Stack<String> loadingQueue = new Stack<>();
            while(this.loadingQueue.size() > 0 && loadingQueue.size() < 50){
                try {
                    loadingQueue.push(this.loadingQueue.take());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(loadingQueue.size() > 0){
                while(loadingQueue.size() > 0){
                    String key = loadingQueue.pop();
                    textures.put(key,loadImage(key));
                }
            }

        }
    }

    public void requestTexture(String key){
        if(!textures.containsKey(key))
            try {
                loadingQueue.put(key);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }

    public void requestTexture(String key, FileType type){
        if(!textures.containsKey(key))
            try {
                loadingQueue.put(key + type.fileEnding);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }

    public void requestTextureGroup(String baseFile, int cLength, int count, FileType fileType) {
        for (int i = 0; i < count; i++){
            String key = baseFile + String.format("%0" + cLength + "d",i) + fileType.valueOf();
            if(!this.textures.containsKey(key))
                requestTexture(key);
        }
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

        public String getFileEnding() {
            return fileEnding;
        }
    }
}
