package de.jspll.util;
import de.jspll.Main;

import java.awt.*;


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

    public static boolean doesCollisionOccur(int[][] collisionMap ,int[] mapPos_and_metaData,Point newPos, Dimension dimension) {


        return checkForCollisionOnInt(collisionMap,mapPos_and_metaData,newPos,dimension,0);
    }

    public static boolean doesOverlapOccur(int[][] collisionMap ,int[] mapPos_and_metaData,Point newPos, Dimension dimension) {
        return checkForCollisionOnInt(collisionMap,mapPos_and_metaData,newPos,dimension,1);


    }

    private static boolean checkForCollisionOnInt(int[][] collisionMap ,int[] mapPos_and_metaData,Point newPos, Dimension dimension, int s){
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
                    if (collisionMap[x][y] == s) {
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
}
