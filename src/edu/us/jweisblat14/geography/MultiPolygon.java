package edu.us.jweisblat14.geography;

import java.io.Serializable;



public class MultiPolygon implements Polygon,Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3195190370708730442L;
	private final PolygonType type = PolygonType.MultiPolygon;
    private double[][][][] coordinates;

    public PolygonType getType() {
        return type;
    }

    public double[][][][] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[][][][] coordinates) {
        this.coordinates = coordinates;
    }
}