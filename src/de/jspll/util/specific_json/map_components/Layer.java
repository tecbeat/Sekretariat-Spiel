package de.jspll.util.specific_json.map_components;

/**
 * Represents a Layer.
 *
 * @author Laura Schmidt
 */
public class Layer {
    private String identifier;
    private String type;
    private String uid;
    private Integer gridSize;
    private String displayOpacity;
    private Integer tilesetDefUid;
    private Coordinate tilePivot;

    public Layer(String identifier, String type, String uid, String gridSize, String displayOpacity,
                 String tilesetDefUid, Coordinate tilePivot) {
        this.identifier = identifier;
        this.type = type;
        this.uid = uid;
        this.gridSize = new Integer(gridSize);
        this.displayOpacity = displayOpacity;
        this.tilesetDefUid = tilesetDefUid.equals("null") ? null : new Integer(tilesetDefUid);
        this.tilePivot = tilePivot;
    }

    @Override
    public String toString(){
        return "[Layer: identifier=" + identifier + ", type=" + type + ", uid=" + uid + ", gridSize=" + gridSize
                + ", displayOpacity=" + displayOpacity + ", tilesetDefUid=" + tilesetDefUid
                + ", tilePivotCoordinates=" + tilePivot.toString() + "]";
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getGridSize() {
        return gridSize;
    }

    public void setGridSize(Integer gridSize) {
        this.gridSize = gridSize;
    }

    public String getDisplayOpacity() {
        return displayOpacity;
    }

    public void setDisplayOpacity(String displayOpacity) {
        this.displayOpacity = displayOpacity;
    }

    public Integer getTilesetDefUid() {
        return tilesetDefUid;
    }

    public void setTilesetDefUid(Integer tilesetDefUid) {
        this.tilesetDefUid = tilesetDefUid;
    }

    public Coordinate getTilePivot() {
        return tilePivot;
    }

    public void setTilePivot(Coordinate tilePivot) {
        this.tilePivot = tilePivot;
    }
}
