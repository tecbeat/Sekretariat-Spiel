package de.jspll.logic;

/**
 * Created by reclinarka on 12-Oct-20.
 */


public interface Interactable {
    /**
     *@return default return value 0x00
     * **/
    char call(String targetID, Object[] input);

    /**
     * ID follows this form:
     * <prefix>.<id>
     * prefix: goup.subgroup.
     * id: classid.uuid
     * **/
    String getID();

    void setListener(LogicHandler listener);


}
