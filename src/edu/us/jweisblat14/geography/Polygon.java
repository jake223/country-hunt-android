package edu.us.jweisblat14.geography;


public interface Polygon {
	PolygonType getType();
	
	enum PolygonType{Polygon,MultiPolygon}
}