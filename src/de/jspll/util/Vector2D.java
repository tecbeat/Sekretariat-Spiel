package de.jspll.util;

import java.awt.*;

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

    public Vector2D instanceScale(double d){
        x *= d;
        y *= d;
        return this;
    }

    public Vector2D rotate(double rotation){

        double rx = (x * Math.cos(rotation)) - (y * Math.sin(rotation));
        double ry = (x * Math.sin(rotation)) + (y * Math.cos(rotation));
        return new Vector2D(rx,ry);

    }

    public Vector2D instanceRotate(double rotation){

        double rx = (x * Math.cos(rotation)) - (y * Math.sin(rotation));
        double ry = (x * Math.sin(rotation)) + (y * Math.cos(rotation));
        this.x = rx;
        this.y = ry;
        return this;

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

    public void updatePos(Point pos){
        Double x = pos.getX(), y = pos.getY();
        x += this.x;
        y += this.y;
        pos.move(x.intValue(),y.intValue());
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
