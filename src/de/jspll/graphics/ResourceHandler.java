package de.jspll.graphics;

import com.google.gson.Gson;
import de.jspll.handlers.GameObjectHandler;
import de.jspll.handlers.JSONSupport;
import de.jspll.util.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Lukas Becker, Philipp Polland
 *
 * @version 1.0
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
        if(key == null)return false;
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
        requestTexture(textureKey);
        return null;
    }

    public BufferedImage getTexture(String textureKey, FileType type){

        if(textures.containsKey(textureKey + type.fileEnding)){
            return textures.get(textureKey + type.fileEnding);
        }
        requestTexture(textureKey, type);
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
            System.out.println("Attempting: " + texture);
            Logger.d.add("loading: " + texture);
            BufferedImage image = ImageIO.read(this.getClass().getResource(texture));
            System.out.println("loaded: " + texture);
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
        if(key == null)
            return;
        if(!textures.containsKey(key) && !loadingQueue.contains(key))
            try {
                loadingQueue.put(key);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }

    public void requestTexture(String key, FileType type){
        if(key == null)
            return;
        if(!textures.containsKey(key) && !loadingQueue.contains(key + type.fileEnding))
            try {
                loadingQueue.put(key + type.fileEnding);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }

    public Map<?,?> readJsonFromFile(String file){
        Gson gson = new Gson();

        // create a reader
        Reader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(file)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // convert JSON file to map
        Map<?, ?> map = gson.fromJson(reader, Map.class);
        return map;

    }

    public void requestTextureGroup(String baseFile, int cLength, int count, FileType fileType) {
        for (int i = 0; i < count; i++){
            String key = baseFile + String.format("%0" + cLength + "d",i) + fileType.valueOf();
            if(!this.textures.containsKey(key))
                requestTexture(key);
        }
    }

    public void jsonStrToFile(String json, String filepath){
        boolean created = false;

        if(filepath.endsWith(".json"))
            filepath = filepath.substring(0,filepath.length()-5);

        filepath = filepath + ".json";

        try {
            File jsonFile = new File(filepath);
            if (jsonFile.createNewFile()) {
                created = true;
            } else {
                //How to handle existing files
                //Overwrite?
            }

            if(created) {
                FileWriter jsonWriter = new FileWriter(jsonFile);
                jsonWriter.write(json);
                jsonWriter.close();
                System.out.println("written!");
            }

        } catch (IOException e) {
            System.out.println("Error in jsonToFile:");
            e.printStackTrace();
        }
    }

    public void objectToFile(Object o, String filepath){
        String jsonStr = JSONSupport.convertObjectToJson(o);
        jsonStrToFile(jsonStr, filepath);
    }

    public void objectsToFile(ArrayList<Object> oArr, String filepath){
        String jsonStr = JSONSupport.convertObjectsToJson(oArr);
        jsonStrToFile(jsonStr, filepath);
    }

    public String fileToJson(String filepath){
        System.out.println(filepath);
        StringBuilder result = new StringBuilder();
        if(!filepath.endsWith(".json"))
            filepath = filepath + ".json";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filepath)));
            String line = br.readLine();
            while (line != null) {
                result.append(line);
                line = br.readLine();
            }
            br.close();
            return result.toString();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    public Clip getAudioByName(String filepath) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        URL url = getClass().getResource(filepath);
        AudioInputStream ais = AudioSystem.getAudioInputStream(url);
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        return clip;
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
