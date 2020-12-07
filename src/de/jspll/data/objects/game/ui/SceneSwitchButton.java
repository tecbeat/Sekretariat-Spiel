package de.jspll.data.objects.game.ui;

import de.jspll.data.ChannelID;
import java.awt.*;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Lukas Becker
 *
 * @version 1.0
 */
public class SceneSwitchButton extends ButtonObject {
    ChannelID scene;
    private String file;

    public SceneSwitchButton(String ID, String objectID, int x, int y, Dimension dimension, String text, boolean border, ChannelID scene, String file) {
        super(ID, objectID, x, y, dimension, text, border);
        this.file = file;
        this.scene = scene;
    }

    @Override
    public char call(Object[] input) {
        super.call(input);

        if(checkClick()) {
            if (this.scene == ChannelID.SCENE_GAME) {
                getParent().loadNextLevel();
            } else {
                getParent().clearScene(scene);
                getParent().clearInteractionLayers();
                getParent().loadScene(scene, file);
            }
        }
        return 0;
    }
}
