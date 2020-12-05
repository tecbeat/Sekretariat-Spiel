package de.jspll.util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author
 *
 * @version 1.0
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

    public static void drawPictureFromCenter(int x, int y, BufferedImage img,Graphics2D g2d, Dimension dimension){
        x = x - dimension.width/2;
        y = y - dimension.height/2;
        g2d.drawImage(img,x,y,dimension.width,dimension.height,null);

    }

    public static void drawPictureFromCenter(Point pos, BufferedImage img,Graphics2D g2d, Dimension dimension){
        int x = pos.x - dimension.width/2;
        int y = pos.y - dimension.height/2;
        g2d.drawImage(img,x,y,dimension.width,dimension.height,null);

    }

    public static void drawRectFromCenter(Point pos,Graphics2D g2d, Dimension dimension){
        int x = pos.x - dimension.width/2;
        int y = pos.y - dimension.height/2;
        g2d.drawRect(x,y,dimension.width,dimension.height);

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
