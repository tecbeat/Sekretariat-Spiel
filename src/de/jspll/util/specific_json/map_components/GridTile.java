package de.jspll.util.specific_json.map_components;

/**
 * Represents a GridTile.
 *
 * @author Laura Schmidt
 */
public class GridTile {
    private Integer coordId;
    private Integer tileId;
    private Coordinate coords;
    private Coordinate sourceCoords;

    public GridTile(String coordId, String tileId, Coordinate coords, Coordinate sourceCoords) {
        this.coordId = coordId.equals("null") ? null : new Integer(coordId);
        this.tileId = tileId.equals("null") ? null : new Integer(tileId);
        this.coords = coords;
        this.sourceCoords = sourceCoords;
    }

    @Override
    public String toString(){
        return "[GridTile: coordId=" + coordId + ", tileId=" + tileId + ", coords=" + coords.toString()
                + ", sourceCoords=" + sourceCoords.toString() + "]";
    }

    public Integer getCoordId() {
        return coordId;
    }

    public void setCoordId(Integer coordId) {
        this.coordId = coordId;
    }

    public Integer getTileId() {
        return tileId;
    }

    public void setTileId(Integer tileId) {
        this.tileId = tileId;
    }

    public Coordinate getCoords() {
        return coords;
    }

    public void setCoords(Coordinate coords) {
        this.coords = coords;
    }

    public Coordinate getSourceCoords() {
        return sourceCoords;
    }

    public void setSourceCoords(Coordinate sourceCoords) {
        this.sourceCoords = sourceCoords;
    }
}
