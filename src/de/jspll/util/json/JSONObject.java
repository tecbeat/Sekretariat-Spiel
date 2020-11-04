package de.jspll.util.json;

import java.util.HashMap;

public class JSONObject {
    protected HashMap<String,JSONValue> object = new HashMap<>();

    public JSONObject setObject(HashMap<String, JSONValue> object) {
        this.object = object;
        return this;
    }

    public HashMap<String, JSONValue> getObject() {
        return object;
    }
}
