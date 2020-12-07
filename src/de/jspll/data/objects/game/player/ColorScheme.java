package de.jspll.data.objects.game.player;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Jonas Sperling
 *
 * @version 1.0
 */
public enum ColorScheme {
    RED_YELLOW(1),
    BLUE(2),
    PURPLE_MAN(3),
    RED_BROWN(4),
    PURPLE_WOMAN(5);

    int id;
    ColorScheme(int id){
        this.id = id;
    }

    @Override
    public String toString() {
        return ""+id;
    }

    public static ColorScheme getById(int id){
        for(ColorScheme cs : ColorScheme.values()){
            if(cs.id == id)
                return cs;
        }
        return PURPLE_MAN;
    }
}
