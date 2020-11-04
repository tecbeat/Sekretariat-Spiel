package de.jspll.util.specific_json.map_components;

/**
 * Represents a Coordinate.
 *
 * @author Laura Schmidt
 */
public class Coordinate {
    private Integer xCoordinate;
    private Integer yCoordinate;

    public Coordinate(String x, String y) {
        this.xCoordinate = new Integer(x);
        this.yCoordinate = new Integer(y);
    }

    @Override
    public String toString(){
        return "[Coordinate: xCoordinate=" + xCoordinate + ", yCoordinate=" + yCoordinate + "]";
    }

    public Integer getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(Integer xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Integer getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(Integer yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
}
