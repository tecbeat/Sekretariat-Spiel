package de.jspll.data.objects.game.tasks.reactions;

import de.jspll.data.objects.game.tasks.iTaskReaction;
import de.jspll.handlers.GameObjectHandler;

public class CoursesReaction implements iTaskReaction {
    @Override
    public int goodSelection(GameObjectHandler gOH) {
        return 10;
    }

    @Override
    public int badSelection(GameObjectHandler gOH) {
        return 5;
    }
}