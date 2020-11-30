package de.jspll.data.objects.game.tasks;

import de.jspll.data.objects.game.stats.StatManager;
import de.jspll.handlers.GameObjectHandler;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Lukas Becker, Laura Schmidt, Samuel Assmann
 *
 * @version 1.0
 */

public interface iTaskReaction {

    int goodSelection(GameObjectHandler gOH);
    int badSelection(GameObjectHandler gOH);

    void taskFinished(StatManager statManager, boolean goodTask);
    void taskFailed(StatManager statManager);
}
