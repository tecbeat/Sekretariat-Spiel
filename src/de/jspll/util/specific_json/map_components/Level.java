package de.jspll.util.specific_json.map_components;

import java.util.List;

/**
 * @author Laura Schmidt
 */
public class Level {
    /*
        "identifier": "Level",
		"uid": 0,
		"pxWid": 3520,
		"pxHei": 3136,
		"layerInstances": [...]
     */
    private String identifier;
    private Integer uid;
    private Integer pxWidth;
    private Integer pxHeight;
    private List<LayerInstance> layerInstances;

    public Level(String identifier, String uid, String pxWidth, String pxHeight, List<LayerInstance> layerInstances) {
        this.identifier = identifier;
        this.uid = uid.equals("null") ? null : new Integer(uid);
        this.pxWidth = pxWidth.equals("null") ? null : new Integer(pxWidth);
        this.pxHeight = pxHeight.equals("null") ? null : new Integer(pxHeight);
        this.layerInstances = layerInstances;
    }

    @Override
    public String toString(){
        return "[Level: identifier=" + identifier + ", uid=" + uid + ", pxWidth=" + pxWidth + ", pxHeight="
                + pxHeight + ", layerInstances=" + layerInstances + "]";
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getPxWidth() {
        return pxWidth;
    }

    public void setPxWidth(Integer pxWidth) {
        this.pxWidth = pxWidth;
    }

    public Integer getPxHeight() {
        return pxHeight;
    }

    public void setPxHeight(Integer pxHeight) {
        this.pxHeight = pxHeight;
    }

    public List<LayerInstance> getLayerInstances() {
        return layerInstances;
    }

    public void setLayerInstances(List<LayerInstance> layerInstances) {
        this.layerInstances = layerInstances;
    }
}
