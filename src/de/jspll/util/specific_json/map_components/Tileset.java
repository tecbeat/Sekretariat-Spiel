package de.jspll.util.specific_json.map_components;

/**
 * @author Laura Schmidt
 */
public class Tileset {
    /*
        "identifier": "Street",
        "uid": 10,
        "relPath": "Tileset/Street.png",
        "pxWid": 821,
        "pxHei": 489,
        "tileGridSize": 32,
        "spacing": 0,
        "padding": 0,
        "savedSelections": []
     */
    private String identifier;
    private Integer uid;
    private String relPath;
    private Integer pxWidth;
    private Integer pxHeight;
    private Integer tileGridSize;
    private Integer spacing;
    private Integer padding;

    public Tileset(String identifier, String uid, String relPath, String pxWidth, String pxHeight,
                   String tileGridSize, String spacing, String padding) {
        this.identifier = identifier;
        this.uid = new Integer(uid);
        this.relPath = relPath;
        this.pxWidth = new Integer(pxWidth);
        this.pxHeight = new Integer(pxHeight);
        this.tileGridSize = new Integer(tileGridSize);
        this.spacing = new Integer(spacing);
        this.padding = new Integer(padding);
    }

    @Override
    public String toString(){
        return "[Tileset: identifier=" + identifier + ", uid=" + uid + ", relPath=" + relPath
                + ", pxWidth=" + pxWidth + ", pxHeight=" + pxHeight + ", tileGridSize="
                + tileGridSize + ", spacing=" + spacing + ", padding=" + padding + "]";
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

    public String getRelPath() {
        return relPath;
    }

    public void setRelPath(String relPath) {
        this.relPath = relPath;
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

    public Integer getTileGridSize() {
        return tileGridSize;
    }

    public void setTileGridSize(Integer tileGridSize) {
        this.tileGridSize = tileGridSize;
    }

    public Integer getSpacing() {
        return spacing;
    }

    public void setSpacing(Integer spacing) {
        this.spacing = spacing;
    }

    public Integer getPadding() {
        return padding;
    }

    public void setPadding(Integer padding) {
        this.padding = padding;
    }
}
