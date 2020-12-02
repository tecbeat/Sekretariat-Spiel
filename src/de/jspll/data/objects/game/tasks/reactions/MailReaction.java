package de.jspll.data.objects.game.tasks.reactions;

import de.jspll.data.objects.game.stats.StatManager;
import de.jspll.data.objects.game.tasks.iTaskReaction;
import de.jspll.handlers.GameObjectHandler;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Lukas Becker, Laura Schmidt, Samuel Assmann
 *
 * @version 1.0
 */

public class MailReaction implements iTaskReaction {
    // TODO: add logic
    @Override
    public int goodSelection(GameObjectHandler gOH) {
        return 10;
    }

    @Override
    public int badSelection(GameObjectHandler gOH) {
        return 5;
    }

    @Override
    public void taskFinished(StatManager statManager, boolean goodTask) {
        if(goodTask) {
            statManager.updateKarmaScore(30);
            statManager.updateRoundScore(20);
        } else {
            statManager.updateKarmaScore(-30);
            statManager.updateRoundScore(20);
        }
    }

    @Override
    public void taskFailed(StatManager statManager) {
        statManager.updateKarmaScore(-30);
        statManager.updateRoundScore(-20);
    }
}
