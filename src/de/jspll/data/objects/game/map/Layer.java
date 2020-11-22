package de.jspll.data.objects.game.map;

import java.util.ArrayList;

public class Layer {
    String id;
    String[] textures = new String[1];
    int width;
    int height;
    ArrayList<GridTiles> gT = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<GridTiles> getgT() {
        return gT;
    }

    public void setgT(ArrayList<GridTiles> gT) {
        this.gT = gT;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String[] getTextures() {
        return textures;
    }

    public void setTextures(String[] textures) {
        this.textures = textures;
    }

    @Override
    public String toString() {
        return "layers{" +
                "id='" + id + '\'' +
                ", gT=" + gT +
                '}';
    }
}