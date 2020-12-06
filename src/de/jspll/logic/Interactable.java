package de.jspll.logic;

import de.jspll.handlers.GameObjectHandler;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Philipp Polland
 *
 * @version 1.0
 */
public interface Interactable {
    /**
     * @return default return value 0x00
     *
     **/
    char call( Object[] input);

    /**
     * ID follows this form:
     * <objectID>:<id>
     * objectID: goup.subgroup.Object
     * id: 1
     */
    String getID();

    void setListener(GameObjectHandler listener);
}
