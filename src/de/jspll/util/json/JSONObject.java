package de.jspll.util.json;

import java.util.HashMap;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Philipp Polland, Samuel Assmann
 *
 * @version 1.0
 */

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
