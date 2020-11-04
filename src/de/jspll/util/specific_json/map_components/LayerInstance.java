package de.jspll.util.specific_json.map_components;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a LayerInstance.
 *
 * @author Laura Schmidt
 */
public class LayerInstance {
    /*
        "__identifier": "Ausstattung2",
        "__type": "Tiles",
        "__cWid": 110,
        "__cHei": 98,
        "__gridSize": 32,
        "levelId": 0,
        "layerDefUid": 7,
        "pxOffsetX": 0,
        "pxOffsetY": 0,
        "intGrid": [],
        "autoTiles": [],
        "seed": 5947671,
        "gridTiles": [],
        "entityInstances": []
     */
    private String identifier;
    private String type;
    private Integer cWidth;
    private Integer cHeight;
    private Integer gridSize;
    private Integer levelId;
    private Integer layerDefUid;
    private Integer pxOffsetX;
    private Integer pxOffsetY;
    private Long seed;
    private List<GridTile> gridTiles = new ArrayList<>();

    public LayerInstance() {
        // create an empty LayerInstance object
    }

    public LayerInstance(String identifier, String type, String cWidth, String cHeight,
                         String gridSize, String levelId, String layerDefUid, String pxOffsetX,
                         String pxOffsetY, String seed, List<GridTile> gridTiles) {
        this.identifier = identifier;
        this.type = type;
        this.cWidth = cWidth.equals("null") ? null : new Integer(cWidth);
        this.cHeight = cHeight.equals("null") ? null : new Integer(cHeight);
        this.gridSize = gridSize.equals("null") ? null : new Integer(gridSize);
        this.levelId = levelId.equals("null") ? null : new Integer(levelId);
        this.layerDefUid = layerDefUid.equals("null") ? null : new Integer(layerDefUid);
        this.pxOffsetX = pxOffsetX.equals("null") ? null : new Integer(pxOffsetX);
        this.pxOffsetY = pxOffsetY.equals("null") ? null : new Integer(pxOffsetY);
        this.seed = seed.equals("null") ? null : new Long(seed);
        this.gridTiles = gridTiles;
    }

    @Override
    public String toString(){
        return "[LayerInstance: identifier=" + identifier + ", type=" + type + ", cWidth=" + cWidth
                + ", cHeight=" + cHeight + ", gridSize=" + gridSize + ", levelId=" + levelId
                + ", layerDefUid=" + layerDefUid + ", pxOffsetX=" + pxOffsetX + ", pxOffsetY="
                + pxOffsetY + ", seed=" + seed + ", gridTiles=" + gridTiles + "]";
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

    public Integer getcWidth() {
        return cWidth;
    }

    public void setcWidth(String cWidth) {
        this.cWidth = cWidth.equals("null") ? null : new Integer(cWidth);
    }

    public Integer getcHeight() {
        return cHeight;
    }

    public void setcHeight(String cHeight) {
        this.cHeight = cHeight.equals("null") ? null : new Integer(cHeight);
    }

    public Integer getGridSize() {
        return gridSize;
    }

    public void setGridSize(String gridSize) {
        this.gridSize = gridSize.equals("null") ? null : new Integer(gridSize);
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId.equals("null") ? null : new Integer(levelId);
    }

    public Integer getLayerDefUid() {
        return layerDefUid;
    }

    public void setLayerDefUid(String layerDefUid) {
        this.layerDefUid = layerDefUid.equals("null") ? null : new Integer(layerDefUid);
    }

    public Integer getPxOffsetX() {
        return pxOffsetX;
    }

    public void setPxOffsetX(String pxOffsetX) {
        this.pxOffsetX = pxOffsetX.equals("null") ? null : new Integer(pxOffsetX);
    }

    public Integer getPxOffsetY() {
        return pxOffsetY;
    }

    public void setPxOffsetY(String pxOffsetY) {
        this.pxOffsetY = pxOffsetY.equals("null") ? null : new Integer(pxOffsetY);
    }

    public Long getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed.equals("null") ? null : new Long(seed);
    }

    public List<GridTile> getGridTiles() {
        return gridTiles;
    }

    public void setGridTiles(List<GridTile> gridTiles) {
        this.gridTiles = gridTiles;
    }
}
