package de.jspll.util;

import com.sun.javafx.scene.paint.GradientUtils;

import java.awt.*;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author
 *
 * @version 1.0
 */

public class Collision {


    public static Point findLineRectIntersection(Point lineOrigin, Point lineEnd,Point rectPos, int width, int height){
        Point intersection = null;
        Point r1 = new Point(rectPos);
        Point r2 = new Point(rectPos.x + width, rectPos.y);
        Point r3 = new Point(rectPos.x + width, rectPos.y + height);
        Point r4 = new Point(rectPos.x, rectPos.y + height);
        Vector2D vec = new Vector2D(lineOrigin,lineEnd);



        if(intersection == null && vec.y < 0 )
            intersection = findLineLineIntersection(lineOrigin,lineEnd,r1,r2);
        if( vec.x > 0 && (intersection == null || intersection.x < r1.x || intersection.x > r2.x) )
            intersection = findLineLineIntersection(lineOrigin,lineEnd,r2,r3);
        if( vec.y > 0 && (intersection == null || intersection.y < r2.y || intersection.y > r3.y) )
            intersection = findLineLineIntersection(lineOrigin,lineEnd,r3,r4);
        if( vec.x < 0 && (intersection == null || intersection.x < r4.x || intersection.x > r3.x))
            intersection = findLineLineIntersection(lineOrigin,lineEnd,r4,r1);
        return intersection;
    }

    public static Point findLineLineIntersection(Point line1start, Point line1end, Point line2start, Point line2end)
    {

        //Line a, Line b
        float x1 = line1start.x;
        float y1 = line1start.y;
        float x2 = line1end.x;
        float y2 = line1end.y;

        float x3 = line2start.x;
        float y3 = line2start.y;
        float x4 = line2end.x;
        float y4 = line2end.y;

        float denominator = (x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4);
        if (denominator == 0)
            return null;

        float xNominator = (x1*y2 - y1*x2)*(x3 - x4) - (x1 - x2)*(x3*y4 - y3*x4);
        float yNominator = (x1*y2 - y1*x2)*(y3 - y4) - (y1 - y2)*(x3*y4 - y3*x4);

        float px = xNominator / denominator;
        float py = yNominator / denominator;

        return new Point(Math.round(px), Math.round(py));


//        // Get Gradient of first line - points : line1start to line1end
//        float dy1 = line1end.y-line1start.y;
//        float dx1 = line1start.x-line1end.x;
//        // Get Gradient of second line - points : line2start to line2end
//        float dy2 = line2end.y-line2start.y;
//        float dx2 = line2start.x-line2end.x;
//
//        // Get delta and check if the lines are parallel
//        float delta = dy1*dx2 - dy2*dx1;
//        if(delta == 0) return null;
//
//        // Get C of first and second lines
//        float c2 = dy2*(float)line2start.x+dx2*(float)line2start.y;
//        float c1 = dy1*(float)line1start.x+dx1*(float)line1start.y;
//        //invert delta to make division cheaper
//        float invdelta = 1/delta;
//        // now return the Vector2 intersection point
//        return new Point(  Math.round((dx2*c1 - dx1*c2)*invdelta),  Math.round((dy1*c2 - dy2*c1)*invdelta) );
    }

    //rec in form [ x, y, width, height ]
    public static boolean doesRectCollide(int[] rec1, int[] rec2 ){
        if(rec1[0] < rec2[0] + rec2[2] && rec1[0] + rec1[2] > rec2[0] &&
                rec1[1] + rec1[3] > rec2[1] && rec1[1] < rec2[1] + rec2[3])
            return true;
        return false;
    }

    public static boolean doesCollisionOccur(int[][] collisionMap ,int[] mapPos_and_metaData,Point newPos, Dimension dimension) {
        return checkForCollisionOnInt(collisionMap,mapPos_and_metaData,newPos,dimension,0,0);
    }

    public static boolean doesOverlapOccur(int[][] collisionMap ,int[] mapPos_and_metaData,Point newPos, Dimension dimension) {
        return checkForCollisionOnInt(collisionMap,mapPos_and_metaData,newPos,dimension,1 , 3);
    }

    public static boolean doesWallOverlap(int[][] collisionMap ,int[] mapPos_and_metaData,Point newPos, Dimension dimension) {
        return checkForCollisionOnInt(collisionMap,mapPos_and_metaData,newPos,dimension,2 , 2);
    }

    public static boolean doesDoorOverlap(int[][] collisionMap ,int[] mapPos_and_metaData,Point newPos, Dimension dimension) {
        return checkForCollisionOnInt(collisionMap,mapPos_and_metaData,newPos,dimension,3 , 3);
    }

    private static boolean checkForCollisionOnInt(int[][] collisionMap ,int[] mapPos_and_metaData,Point newPos, Dimension dimension, int lowBound, int highBound){
        int mapX = mapPos_and_metaData[0],
                mapY = mapPos_and_metaData[1],
                mapWidth = mapPos_and_metaData[2],
                mapHeight = mapPos_and_metaData[3],
                collidableWidth = dimension.width,
                collidableHeight = dimension.height;
        if (!(  newPos.x + collidableWidth < mapX || newPos.x > mapWidth + mapX ||
                newPos.y + collidableHeight < mapY || newPos.y > mapY + mapHeight
        )) {
            int tileWidth = mapPos_and_metaData[4],
                    tileHeight = mapPos_and_metaData[5],
                    relX = newPos.x - mapX,
                    relY = newPos.y - mapY,
                    leftTile = relX / tileWidth,
                    rightTile = (relX + collidableWidth) / tileWidth,
                    topTile = relY / tileHeight,
                    bottomTile = (relY + collidableHeight) / tileHeight;
            //collisionMap[x][y] == 0 -> not passable;

            int[] newPlayerPos = new int[]{relX,
                    relY,
                    collidableWidth,
                    collidableHeight};
            for (int x = leftTile; x <= rightTile; x++) {
                for (int y = topTile; y <= bottomTile; y++) {
                    if (collisionMap[x][y] >= lowBound && collisionMap[x][y] <= highBound) {
                        if (Collision.doesRectCollide(newPlayerPos,
                                new int[]{mapX + x * tileWidth,
                                        mapY + y * tileHeight,
                                        tileWidth,
                                        tileHeight}))
                            return true;
                    }
                }
            }


            //Logger.d.add("playerTile x=" + playerTileX + " y=" + playerTileY);


        }
        return false;
    }

    public static boolean posInRect(Point pos, Rectangle rectangle){
        return pos.x > rectangle.x && pos.y > rectangle.y && pos.y < rectangle.y+rectangle.height && pos.x < rectangle.x + rectangle.width;
    }
}
