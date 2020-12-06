package de.jspll.data.objects.game.player;

public enum ColorScheme {
    RED_YELLOW(1),
    BLUE(2),
    PURPLE_MAN(3),
    RED_BROWN(4),
    PURPLE_WOMAN(6);

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
