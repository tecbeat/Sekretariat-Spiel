package de.jspll.data.objects.loading;

/**
 * Created by reclinarka on 05-Nov-20.
 */
public interface ProgressReporter {
    float getProgress();
    void update();
    void setCount(int count);
}
