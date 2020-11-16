package de.jspll.util;

import java.awt.*;

/**
 * Created by reclinarka on 15-Nov-20.
 */
public class PaintingUtil {
    public static void paintCircleFromCenter(int x, int y, double radius, Graphics2D g){
        g.fillOval( (int)  (x - radius),(int)(y - radius),(int)(radius * 2),(int)(radius * 2));
    }

    public static void paintCircleFromCenter(double x, double y, double radius, Graphics2D g){
        g.fillOval( (int)  (x - radius),(int)(y - radius),(int)(radius * 2),(int)(radius * 2));
    }
}
