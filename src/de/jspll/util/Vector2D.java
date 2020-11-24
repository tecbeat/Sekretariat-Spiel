package de.jspll.util;

import java.awt.*;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Jonas Sperling, Philipp Polland
 *
 * @version 1.0
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
        this.x = ((Integer) p2.x).doubleValue() - ((Integer) p1.x).doubleValue();
        this.y = ((Integer) p2.y).doubleValue() - ((Integer) p1.y).doubleValue();
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

    /**
     * Rotate the vector by the given angle
     *
     * @param rotation an angle, in radians
     * @return vector with the rotated coordinates
     */
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

    /**
     * Override the toString methode form java.lang.object
     *
     * @return String with the {@code x} and {@code y} coordinates
     */
    @Override
    public String toString() {
        return "[x=" + x + ", y=" + y + "]";
    }

    /**
     * Override the equals method from java.lang.object
     * An Vector is equal to another one if:
     *    - they share the same euclidean Distance
     *    - they point in the same direction
     *
     * @param o vector which will be checked against the current
     * @return {@code true} if all of the above criteria match,
     *         {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2D vec = (Vector2D) o;
        if (vec.x == this.x && vec.y == this.y) return true;

        // TODO needed check for direction of the vector
        return vec.euclideanDistance() == euclideanDistance();
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

    /**
     * Calculates the euclidean Distance by using the pythagorean theorem (c^2 = a^2 + b^2)
     * @return euclidean Distance as a double
     */
    public double euclideanDistance(){
        return Math.sqrt( x*x + y*y );
    }

    public void updatePos(Point pos){
        if(x == 0 && y == 0){
            return;
        }
        Double x = pos.getX(), y = pos.getY();
        x += this.x;
        y += this.y;

        pos.move((int)Math.round(x), (int)Math.round(y));
    }

    public void addToX(int amount){
        this.x += amount;
    }

    public void addToY(int amount){
        this.y += amount;
    }

    public void addToX(double amount){
        this.x += amount;
    }

    public void addToY(double amount){
        this.y += amount;
    }

    /**
     * Normalizes to Vector so that it has a euclidean distance of 1
     * The {@code x} and {@code y} Coordinates are divided by the current euclidean distance
     */

    public void normalize(){
        if(x == 0 && y == 0){
            return;
        }
        double abs = euclideanDistance();
        this.x /= abs;
        this.y /= abs;
    }

    public Vector2D[] splitIntoXY(){
        return new Vector2D[]{ new Vector2D(x,0), new Vector2D(0,y) };
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }


    /**
     * calculates the Angle of the Vector from the position (0,0)
     * to calculate it the hyperbolic tangent needs to be calculated by the division of the {@code x} and {@code y} Coordinates
     * @return Angle in Degrees
     */
    public Double calculateAngle(){
        return Math.toDegrees(Math.tanh(x / y));
    }


}
