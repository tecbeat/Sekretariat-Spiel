package de.jspll.data.objects.game.tasks.reactions;

import de.jspll.data.objects.game.stats.StatManager;
import de.jspll.data.objects.game.tasks.iTaskReaction;
import de.jspll.handlers.GameObjectHandler;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Laura Schmidt
 *
 * @version 1.0
 */
public class InternetReaction implements iTaskReaction {

    private GameObjectHandler goh;

    @Override
    public int goodSelection(GameObjectHandler gOH) {
        this.goh = gOH;
        return 5;
    }

    @Override
    public int badSelection(GameObjectHandler gOH) {
        return 2;
    }

    @Override
    public void taskFinished(StatManager statManager, boolean goodTask) {
        if(goodTask) {
            statManager.updateKarmaScore(-30);
            statManager.updateRoundScore(-20);
            goh.setInternetTaskDone(true);

        }
    }

    @Override
    public void taskFailed(StatManager statManager) {
        statManager.updateKarmaScore(-30);
        statManager.updateRoundScore(-20);
    }
}
