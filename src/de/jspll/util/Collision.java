package de.jspll.util;

/**
 * Created by reclinarka on 21-Nov-20.
 */
public class Collision {
    //rec in form [ x, y, width, height ]
    public static boolean doesRectCollide(int[] rec1, int[] rec2 ){
        if(rec1[0] < rec2[0] + rec2[2] && rec1[0] + rec1[2] > rec2[0] &&
                rec1[1] + rec1[3] > rec2[1] && rec1[1] < rec2[1] + rec2[3])
            return true;
        return false;
    }
}
