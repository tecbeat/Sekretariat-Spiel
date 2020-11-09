package de.jspll.handlers;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class GsonExclusionStrategy implements ExclusionStrategy {
    public boolean shouldSkipClass(Class<?> type) {
        if(type == GameObjectHandler.class){
            return true;
        }

        return false;
    }

    public boolean shouldSkipField(FieldAttributes field) {
        return false;
    }
}
