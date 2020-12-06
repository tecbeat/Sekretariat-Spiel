package de.jspll.data.objects.game.player;

import java.awt.*;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Lukas Becker
 * @version 1.0 Samuel Assmann
 */
public enum NPCSpawnPosition {
    POS_1(0,new Point(1100, 1120)),
    POS_2(1,new Point(1351, 565)),
    POS_3(2,new Point(466, 343)),
    POS_4(3,new Point(3000, 458)),
    POS_5(4,new Point(3121, 1265)),
    POS_6(5,new Point(2912, 2481)),
    POS_7(6,new Point(603, 1530)),
    ;

    private final int id;
    private final Point point;

    NPCSpawnPosition(int id, Point point) {
        this.id = id;
        this.point = point;
    }

    public static NPCSpawnPosition getById(int id) {
        for (NPCSpawnPosition v : values()) {
            if (v.id == id) {
                return v;
            }
        }
        return null;
    }

    public static Point getPointById(int id){
        for (NPCSpawnPosition v : values()) {
            if (v.id == id) {
                return v.point;
            }
        }
        return null;
    }

    public static int length(){
        return values().length;
    }
}
