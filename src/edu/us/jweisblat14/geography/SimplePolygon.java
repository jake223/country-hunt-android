package edu.us.jweisblat14.geography;

import java.io.Serializable;


public class SimplePolygon implements Polygon,Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -633088613686912850L;
	private final PolygonType type = PolygonType.Polygon;
    private double[][][] coordinates;

    public PolygonType getType() {
        return type;
    }

    public double[][][] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[][][] coordinates) {
        this.coordinates = coordinates;
    }
}