package de.jspll.util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by reclinarka on 15-Nov-20.
 */
public class PaintingUtil {
    public static void paintCircleFromCenter(int x, int y, double radius, Graphics2D g, boolean fill){
        if(fill)
            g.fillOval( (int)  (x - radius),(int)(y - radius),(int)(radius * 2),(int)(radius * 2));
        else
            g.drawOval( (int)  (x - radius),(int)(y - radius),(int)(radius * 2),(int)(radius * 2));
    }
    public static void paintCircleFromCenter(int x, int y, double radius, Graphics2D g){
        g.fillOval( (int)  (x - radius),(int)(y - radius),(int)(radius * 2),(int)(radius * 2));
    }

    public static void paintCircleFromCenter(double x, double y, double radius, Graphics2D g){
        g.fillOval( (int)  (x - radius),(int)(y - radius),(int)(radius * 2),(int)(radius * 2));
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_FAST);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
