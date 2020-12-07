package de.jspll.util.json;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Philipp Polland, Samuel Assmann
 *
 * @version 1.0
 */

public class JSONValue<T> {

    protected T value;

    public JSONValue(){

    }
    public JSONValue(T value){
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
