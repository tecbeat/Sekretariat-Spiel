package de.jspll.data.objects.game.map;

import java.awt.*;
import java.util.ArrayList;

public class GridTiles {
    ArrayList<Double> px = new ArrayList<>();
    ArrayList<Double> src = new ArrayList<>();
    Double f;
    ArrayList<Double> d = new ArrayList<>();

    public ArrayList<Double> getPx() {
        return px;
    }

    public int[] getPxArr(){
        int[] res = new int[px.size()];
        for(int i = 0; i< px.size(); i++){
            res[i] = px.get(i).intValue();
        }

        return res;
    }

    public int[] getSrcArr(Dimension d){
        int[] res = new int[src.size()+3];
        int i;
        for(i = 0; i< src.size(); i++){
            res[i] = src.get(i).intValue();
        }
        res[i] = d.width; //Tile height
        res[i+1] = d.height; //Tile width
        res[i+2] = 0; //0, because there is only one texture per layer
        return res;
    }

    public int[] getDArr(){
        int[] res = new int[d.size()];
        for(int i = 0; i< d.size(); i++){
            res[i] = d.get(i).intValue();
        }
        return res;
    }

    public int getFInt(){
        return f.intValue();
    }

    public void setPx(ArrayList<Double> px) {
        this.px = px;
    }

    public ArrayList<Double> getSrc() {
        return src;
    }

    public void setSrc(ArrayList<Double> src) {
        this.src = src;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public ArrayList<Double> getD() {
        return d;
    }

    public void setD(ArrayList<Double> d) {
        this.d = d;
    }

    @Override
    public String toString() {
        return "gridTiles{" +
                "px=" + px +
                ", src=" + src +
                ", f=" + f +
                ", d=" + d +
                '}';
    }
}

