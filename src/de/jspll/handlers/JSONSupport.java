package de.jspll.handlers;

import com.google.gson.*;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.examples.DisplayMover;
import de.jspll.data.objects.examples.MouseFollower;
import de.jspll.util.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;

public class JSONSupport {
    private static Gson gson = new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy()).create();;

    public static Class getClassByType(String type){
        int SUBSTRING_START = 6; // remove "class " from type

        try {
            return Class.forName(type.substring(SUBSTRING_START));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String convertObjectToJson(Object o){
        String type = o.getClass().toString();
        Gson gson = new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy()).create();
        String object = gson.toJson(o);

        String res = "{\"type\": \""+type+"\", \"object:\": "+object+"}";

        return res;
    }

    public static String convertToCommonJson(Object o){
        return convertToCommonJson(o.getClass());
    }

    public static String convertToCommonJson(Class c){
        String type = c.getClass().toString();
        Field[] f =  c.getClass().getDeclaredFields();
        int countMax = f.length;

        StringBuilder object = new StringBuilder();

        object.append("{");

        for(int i = 0; i< countMax; i++){
            object.append("\"");
            object.append(f[i].getName());
            object.append("\":\"");
            object.append(f[i].getGenericType());
            object.append("\"");
            if(i != countMax -1){
                object.append(", ");
            }
        }

        object.append("}");
        return "{\"type\": \""+type+"\", \"object:\": "+object+"}";
    }

    /**
     * Converts JSONObject to GameObject
     * @param jsonObject
     * @return Game Object
     */
    public static GameObject fromJsonToGameObject(JSONObject jsonObject){
        String type = jsonObject.getObject().get("type").toString();
        Class<? extends GameObject> cl = getClassByType(type);
        GameObject obj = cl.cast(gson.fromJson(jsonObject.getObject().get("object").toString(), cl));
        return obj;
    }


}

class GsonExclusionStrategy implements ExclusionStrategy {
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

