package de.jspll.data.objects;

import com.sun.istack.internal.Nullable;

import java.util.ArrayList;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Philipp Polland
 * @version 1.0
 */
public class GameTrie {

    private GameObject value = null;

    private ArrayList<Character> chars = new ArrayList<>();

    private ArrayList<GameTrie> tries = new ArrayList<>();

    /**
     * Search the {@code GameTrie} recursively for the {@code GameObject}
     *
     * @param key String
     * @return {@code GameObject} if key can be found recursively, else null
     */
    public synchronized GameObject find(String key) {
        if (key.contentEquals("")) {
            return value;
        } else {
            for (int i = 0; i < chars.size(); i++) {
                if (chars.get(i) == key.charAt(0))
                    return tries.get(i).find(key.substring(1));
            }
            return null;
        }
    }

    /**
     * Search the {@code GameTrie} recursively for the {@code GameTrie}
     *
     * @param key String
     * @return {@code GameTrie} if key can be found recursively, else null
     */
    public synchronized GameTrie findTrie(String key) {
        if (key.contentEquals("")) {
            return this;
        } else {
            for (int i = 0; i < chars.size(); i++) {
                if (chars.get(i) == key.charAt(0))
                    return tries.get(i).findTrie(key.substring(1));
            }
            return null;
        }
    }

    /**
     * Insert a new {@code GameObject} to the {@code tries}
     *
     * @param key   String
     * @param value GameObject
     * @return this
     **/
    public synchronized GameTrie insert(String key, GameObject value) {
        if (key.contentEquals("")) {
            this.value = value;
        } else {
            for (int i = 0; i < chars.size(); i++) {
                if (chars.get(i) == key.charAt(0)) {
                    tries.get(i).insert(key.substring(1), value);
                    return this;
                }
            }
            chars.add(key.charAt(0));
            tries.add(new GameTrie().insert(key.substring(1), value));
        }
        return this;
    }

    /**
     * @return true if {@code tries}, {@code chars} and {@code value} are null or 0, else false
     */
    public synchronized boolean isEmpty() {
        return value == null && chars.size() == 0 && tries.size() == 0;
    }

    public synchronized GameTrie delete(String key) {
        if (key.contentEquals("")) {
            this.value = null;
            return this;
        } else {
            for (int i = 0; i < chars.size(); i++) {
                if (chars.get(i) == key.charAt(0)) {
                    tries.get(i).delete(key.substring(1));
                    if (tries.get(i).isEmpty()) {
                        tries.remove(i);
                        chars.remove(i);
                    }
                    return this;
                }
            }
        }
        return this;
    }

    /**
     * All values from {@code tries} getting collected.
     *
     * @param collector ArrayList of all {@code GameObject}
     */

    private synchronized void _allValues(ArrayList<GameObject> collector) {
        if (value != null) {
            collector.add(value);
        }
        for (GameTrie trie : tries) {
            trie._allValues(collector);
        }
    }

    /**
     * @return ArrayList of all {@code GameObject} inside the {@code tries}.
     */
    public synchronized ArrayList<GameObject> allValues() {
        ArrayList<GameObject> values = new ArrayList<>();
        _allValues(values);
        return values;
    }

    /**
     * Reset the GameTrie to its default: <br>
     * {@code value} is null,<br>
     * {@code chars} is an empty ArrayList,<br>
     * {@code tries} is an empty ArrayList,<br>
     */
    public synchronized void dropAll() {
        value = null;
        chars = new ArrayList<>();
        tries = new ArrayList<>();
    }

    /**
     * Collect all {@code GameObject} which start with the {@code prefix}.
     *
     * @param prefix String where the Values start
     * @return ArrayList of {@code GameObject} inside the {@code tries}.
     */
    public synchronized ArrayList<GameObject> allValuesAfter(String prefix) {
        GameTrie tmp = findTrie(prefix);
        if (tmp != null) {
            return tmp.allValues();
        }
        return null;
    }

    /**
     * Collect all {@code keys} that start with the {@code prefix}.
     *
     * @param collector ArrayList in which the {@code keys} are stored.
     * @param prefix String where the Values start
     */
    private synchronized void _allKeys(ArrayList<String> collector, String prefix) {
        if (value != null) {
            collector.add(prefix);
        }
        for (int i = 0; i < chars.size(); i++) {
            tries.get(i)._allKeys(collector, prefix + chars.get(i));
        }
    }

    /**
     * Collect all {@code keys} that start with the {@code prefix}.
     *
     * @param prefix String where the Values start
     * @return Arraylist with keys starting from {@code prefix}
     */
    public synchronized ArrayList<String> allKeys(@Nullable String prefix) {
        ArrayList<String> keys = new ArrayList<>();
        _allKeys(keys, prefix);
        return keys;
    }

    private synchronized ArrayList<String> _findSuffix(@Nullable String s, String prefix) {
        if (s == null || s.contentEquals("")) {
            return allKeys(prefix);
        }
        ArrayList<String> suffix = new ArrayList<>();
        for (int i = 0; i < chars.size(); i++) {
            if (chars.get(i) == s.charAt(0)) {
                suffix.addAll(tries.get(i)._findSuffix(s.substring(1), prefix));
            }
        }
        return suffix;
    }

    public synchronized ArrayList<String> findSuffix(String prefix) {
        return _findSuffix(prefix, prefix);
    }
}
