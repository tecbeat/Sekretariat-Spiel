package de.jspll.data.objects;

import com.sun.istack.internal.Nullable;

import java.util.ArrayList;

/**
 * Created by reclinarka on 23-Oct-20.
 *
 * Data structure to store game objects in
 */
public class GameTrie {

    private GameObject value = null;

    private ArrayList<Character> chars = new ArrayList<>();

    private ArrayList<GameTrie> tries = new ArrayList<>();

    /**
     * @param key parameters to search trie by
     * @return found game object
     */
    public GameObject find(String key){
        if(key.contentEquals("")){
            return value;
        } else {
            for(int i = 0; i < chars.size(); i++){
                if (chars.get(i) == key.charAt(0))
                    return tries.get(i).find(key.substring(1));
            }
            return null;
        }
    }

    /**
     * @param key parameters to search trie by
     * @return found trie
     */
    public GameTrie findTrie(String key){
        if(key.contentEquals("")){
            return this;
        } else {
            for(int i = 0; i < chars.size(); i++){
                if (chars.get(i) == key.charAt(0))
                    return tries.get(i).findTrie(key.substring(1));
            }
            return null;
        }
    }

    /**
     * @param key key to insert by
     * @param value value to insert
     * @return new trie
     */
    public GameTrie insert(String key, GameObject value){
        if(key.contentEquals("")){
            this.value = value;
        } else {
            for(int i = 0; i < chars.size(); i++){
                if (chars.get(i) == key.charAt(0)) {
                    tries.get(i).insert(key.substring(1), value);
                    return this;
                }
            }
            chars.add(key.charAt(0));
            tries.add( new GameTrie().insert( key.substring(1),value ) );
        }
        return this;
    }

    /**
     * @return status
     */
    public boolean isEmpty(){
        return value == null && chars.size() == 0 && tries.size() == 0;
    }

    /**
     * @param key key to delete
     * @return new trie
     */
    public GameTrie delete(String key){
        if(key.contentEquals("")){
            this.value = null;
            return this;
        } else {
            for(int i = 0; i < chars.size(); i++){
                if (chars.get(i) == key.charAt(0)) {
                    tries.get(i).delete(key.substring(1));
                    if(tries.get(i).isEmpty()){
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
     * @param collector Array list to insert values into
     */
    private void _allValues(ArrayList<GameObject> collector){
        if(value != null){
            collector.add(value);
        }
        for (GameTrie trie: tries){
            trie._allValues(collector);
        }
    }

    /**
     * @return ArrayList with all game objects
     */
    public ArrayList<GameObject> allValues(){
        ArrayList<GameObject> values = new ArrayList<>();
        _allValues(values);
        return values;
    }


    /**
     * @param prefix to search by
     * @return all values following the prefix
     */
    public ArrayList<GameObject> allValuesAfter( String prefix){
        return findTrie(prefix).allValues();
    }

    private void _allKeys(ArrayList<String> collector, String prefix){
        if(value != null){
            collector.add(prefix);
        }
        for(int i = 0; i < chars.size(); i++){
            tries.get(i)._allKeys(collector,prefix + chars.get(i));
        }
    }

    public ArrayList<String> allKeys(@Nullable String prefix){
        ArrayList<String> keys = new ArrayList<>();
        _allKeys(keys,prefix);
        return keys;
    }

    private ArrayList<String> _findSuffix(@Nullable String s, String prefix){
        if(s == null || s.contentEquals("")){
            return allKeys(prefix);
        }
        ArrayList<String> suffix = new ArrayList<>();
        for (int i = 0; i < chars.size(); i++){
            if(chars.get(i) == s.charAt(0)) {
                suffix.addAll(tries.get(i)._findSuffix(s.substring(1),prefix));
            }
        }
        return suffix;
    }

    public ArrayList<String> findSuffix( String prefix){
        return _findSuffix(prefix,prefix);
    }



}
