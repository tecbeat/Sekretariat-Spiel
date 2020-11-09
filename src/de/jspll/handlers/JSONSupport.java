package de.jspll.handlers;

import com.google.gson.*;
import de.jspll.data.objects.GameObject;
import de.jspll.util.json.JSONObject;

import java.util.HashMap;

public class JSONSupport {
    private static HashMap<String, Class> types = new HashMap<>();

    String test;

    public JSONSupport(){
        init();
    }

    public static HashMap<String,Class> getTypes(){
        if (types.isEmpty()){
            init();
        }

        return types;
    }

    public static Class getObjectByType(String type){
        //if (types.isEmpty()){
            init();
        //}

        return types.get(type);

    }

    private static void init(){
        types.put("GameObject", GameObject.class);
    }

    public static String convertObjectToJson(Object o){
        String type = o.getClass().toString();
        Gson gson = new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy()).create();
        String object = gson.toJson(o);

        String res = "{\"type\": \""+type+"\", \"object:\" : "+object+"}";

        return res;
    }


}

