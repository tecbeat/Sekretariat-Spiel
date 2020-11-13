package de.jspll.data.objects.loading;

import de.jspll.data.ChannelID;

/**
 * Created by reclinarka on 05-Nov-20.
 */
public interface ProgressReporter {
    float getProgress();
    ChannelID getNextScene();
}