package de.jspll.handlers;

import com.google.gson.*;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.Texture;
import de.jspll.util.json.JSONObject;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Lukas Becker
 *
 * @version 1.0
 */
public class JSONSupport {
    private static Gson gson = new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy()).create();;

    public static Class getClassByType(String type){
        int SUBSTRING_START;
        int SUBSTRING_END = type.length();

        if(type.startsWith("\"class ")){
            SUBSTRING_START = 7; // remove "class " from type
        } else  {
            SUBSTRING_START = 0;
        }

        if(type.endsWith("\"")){
            SUBSTRING_END = type.length()-1;
        }

        try {
            return Class.forName(type.substring(SUBSTRING_START,SUBSTRING_END));
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR:");
            e.printStackTrace();
            return null;
        }
    }

    public static String convertObjectToJson(Object o){
        String type = o.getClass().toString();
        Gson gson = new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy()).create();
        String object = gson.toJson(o);

        String res = "{\"type\": \""+type+"\", \"object\": "+object+"}";

        return res;
    }

    public static String convertObjectsToJson(ArrayList<Object> oL){
        int count = oL.size();
        StringBuilder res = new StringBuilder();
        res.append("[");
        for(int i = 0; i < count; i++){
            res.append(convertObjectToJson(oL.get(i)));
            if(i != count - 1)
                res.append(",");
        }
        res.append("]");
        return res.toString();
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
        return "{\"type\": \""+type+"\", \"object\": "+object+"}";
    }

    /**
     * Converts JSONObject to GameObject
     * @param jsonElement
     * @return Game Object
     */
    public static GameObject fromJsonToGameObject(JsonElement jsonElement){
        String type = jsonElement.getAsJsonObject().get("type").toString();
        Class<? extends GameObject> cl = getClassByType(type);
        GameObject obj = cl.cast(gson.fromJson(jsonElement.getAsJsonObject().get("object"), cl)); // this returns null
        return obj;
    }

    public static Texture fromJsonToTexture(JSONObject jsonObject){
        String type = jsonObject.getObject().get("type").toString();
        Class<? extends Texture> cl = getClassByType(type);
        Texture obj = cl.cast(gson.fromJson(jsonObject.getObject().get("object").toString(), cl));
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



