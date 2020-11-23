package de.jspll.data.objects.game.tasks;

import de.jspll.handlers.GameObjectHandler;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author
 *
 * @version 1.0
 */

public interface iTaskReaction {

    int goodSelection(GameObjectHandler gOH);
    int badSelection(GameObjectHandler gOH);
}
