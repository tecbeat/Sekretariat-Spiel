package de.jspll.util;

import java.awt.*;
import java.util.Objects;

/**
 * Created by reclinarka on 15-Nov-20.
 */
public class Vector2D {

    public Vector2D() {
        super();
        x = 0.0; y = 0.0;
    }

    public Vector2D(double x, double y) {
        super();
        this.x = x; this.y = y;
    }

    public Vector2D(Point p1, Point p2) {
        super();
        this.x = p2.x-p1.x; this.y = p2.y - p1.y;
    }

    public double x, y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector2D scale(double d){
        return new Vector2D(x * d, y * d);
    }

    public static Vector2D scale(double d, Vector2D vec){
        return new Vector2D(vec.x * d, vec.y * d);
    }


    public Vector2D rotate(double rotation){

        double rx = (x * Math.cos(rotation)) - (y * Math.sin(rotation));
        double ry = (x * Math.sin(rotation)) + (y * Math.cos(rotation));
        return new Vector2D(rx,ry);

    }

    public Double abs(){
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y,2));
    }

    public static Double abs(Vector2D vec){
        return Math.sqrt(Math.pow(vec.x, 2) + Math.pow(vec.y,2));

    }

    @Override
    public String toString() {
        return "[x=" + x + ",y=" + y + "]";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2D vec = (Vector2D) o;

        if (vec.x == this.x && vec.y == this.y) return true;

        return vec.abs().equals(abs());
    }


    private static double bellCurveConstant = 1 / (0.399d * Math.sqrt(2* Math.PI));

    /**
     * @param d1 value between 0 and 1
     *    d1 -> value between 0 and 1
     *
     * @return value between 0 and 1
     * |#########################################################ooooooo
     * |#####################################################OOOOO######
     * |##################################################OOO###########
     * |###############################################OOO##############
     * |##########################################OOOOO#################
     * |####################################OOOOOO######################
     * |##########################OOOOOOOOOO############################
     * |##############OOOOOOOOOOOO######################################
     * |OOOOOOOOOOOOO###################################################
     * |################################################################
     *
     * **/
    public static double mapToBellCurve(double d1){
        double exponent =  -0.5 * Math.pow(d1 * 4d ,2);

        return bellCurveConstant * Math.exp(exponent);
    }

}
