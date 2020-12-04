package de.jspll.data.objects.game.tasks;

import de.jspll.data.objects.game.stats.StatManager;
import de.jspll.handlers.GameObjectHandler;

/**
 * Interface for task reactions.
 *
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Lukas Becker, Laura Schmidt, Samuel Assmann
 *
 * @version 1.0
 */

public interface iTaskReaction {

    /**
     * Determines the duration of the task if the player chooses to do the task in a positive way.
     *
     * @param gOH {@link GameObjectHandler}
     * @return duration of the task
     */
    int goodSelection(GameObjectHandler gOH);

    /**
     * Determines the duration of the task if the player chooses to do the task in a negative way.
     *
     * @param gOH {@link GameObjectHandler}
     * @return duration of the task
     */
    int badSelection(GameObjectHandler gOH);


    /**
     * Determines how many points should be added to the score.
     *
     * @param statManager {@link StatManager}
     * @param goodTask was the task done in positive or negative way
     */
    void taskFinished(StatManager statManager, boolean goodTask);

    /**
     * Determines how many points should be added to the score.
     *
     * @param statManager {@link StatManager}
     */
    void taskFailed(StatManager statManager);
}
