package de.jspll.data.objects.game.tasks;

import de.jspll.data.objects.GameObject;
import de.jspll.handlers.GameObjectHandler;

public class MailReaction implements iTaskReaction {
    @Override
    public int goodSelection(GameObjectHandler gOH) {
        return 10;
    }

    @Override
    public int badSelection(GameObjectHandler gOH) {
        return 5;
    }
}
