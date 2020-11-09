package de.jspll.util.json;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by reclinarka on 04-Nov-20.
 */
public class JSONArray extends JSONObject {
    protected ArrayList<JSONValue> values;

    @Override
    public HashMap<String, JSONValue> getObject() {
        HashMap<String , JSONValue> object = new HashMap<>(values.size());
        for (int i = 0; i < values.size(); i++){
            object.put("" + i, values.get(i));
        }
        return object;
    }

    public JSONArray setValues(ArrayList<JSONValue> values) {
        this.values = values;
        return this;
    }

    public ArrayList<JSONValue> getValues() {
        return values;
    }
}
