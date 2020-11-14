package de.jspll.data.objects.game.player;



import de.jspll.data.objects.GameObject;



import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class newPlayer extends GameObject {

    Movement movements;



    public newPlayer(String ID, int x, int y, Dimension dimension, Movement movements) {
        super(ID, "g.tst.PlayerAnimation", x,y, dimension);
        this.movements = movements;
    }


    @Override
    public char call(Object[] input) {
        HashMap<String, AtomicBoolean> keyMap;
        super.call(input);
        if (input == null || input.length < 1) {
            return 0;
        } else if (input[0] instanceof String) {
            if (((String) input[0]).contentEquals("input")) {
                if (input[4] instanceof HashMap) {
                    keyMap = (HashMap<String, AtomicBoolean>) input[4];
                    if (keyMap.get("w").get()) {
                        movements.call(input);
                    }/*else if (keyMap.get("s").get()){
                        movements[1].startAnimation();
                    } else if (keyMap.get("a").get()){
                        movements[2].startAnimation();
                    } else if (keyMap.get("d").get()){
                        movements[3].startAnimation();
                    }*/
                }
            }
        }
        return 0;
    }
}