package de.jspll.graphics;

import de.jspll.data.objects.GameObject;
import de.jspll.handlers.GameObjectHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by reclinarka on 27-Oct-20.
 */
public class TextureHandler {
    private GameObjectHandler parent;
    public TextureHandler(GameObjectHandler parent){
        this.parent = parent;
    }

    public BufferedImage getTexture(String texture){
        try {
            BufferedImage image = ImageIO.read(this.getClass().getResource(texture));
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public BufferedImage[] getTextures(String[] textures){
        BufferedImage[] out = new BufferedImage[textures.length];
        for(int i = 0; i < textures.length; i++) {
            out[i] = getTexture(textures[i]);
        }
        return out;


    }
}
