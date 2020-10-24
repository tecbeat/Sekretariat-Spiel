package de.jspll.logic;

import de.jspll.handlers.GameObjectHandler;

/**
 * Created by reclinarka on 12-Oct-20.
 */


public interface Interactable {
    /**
     *@return default return value 0x00
     * **/
    char call( Object[] input);

    /**
     * ID follows this form:
     * <objectID>:<id>
     * objectID: goup.subgroup.Object
     * id: 1
     * **/
    String getID();

    void setListener(GameObjectHandler listener);


}
