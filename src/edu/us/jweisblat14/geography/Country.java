package edu.us.jweisblat14.geography;

import java.io.Serializable;
import java.util.Locale;

public class Country implements Comparable<Country>,Serializable {
		/**
	 * 
	 */
	private static final long serialVersionUID = -8330515292103351019L;
		String type;
		Properties properties;
		Polygon geometry;

	public String toString() {
		return properties.name;
	}

	public double distanceTo(Country other) {
		double min1 = 1E200;
		if (geometry instanceof SimplePolygon
				&& other.geometry instanceof SimplePolygon)
			for (double[] pair : ((SimplePolygon) geometry).getCoordinates()[0])
				for (double[] pair2 : ((SimplePolygon) other.geometry)
						.getCoordinates()[0]) {
					double result = dist(pair, pair2);
					if (result < min1)
						min1 = result;
				}
		else if (geometry instanceof MultiPolygon
				&& other.geometry instanceof SimplePolygon)
			for (double[][][] array : ((MultiPolygon) geometry)
					.getCoordinates())
				for (double[] pair : array[0])
					for (double[] pair2 : ((SimplePolygon) other.geometry)
							.getCoordinates()[0]) {
						double result = dist(pair, pair2);
						if (result < min1)
							min1 = result;
					}
		else if (geometry instanceof MultiPolygon
				&& other.geometry instanceof MultiPolygon)
			for (double[][][] array : ((MultiPolygon) geometry)
					.getCoordinates())
				for (double[][][] array2 : ((MultiPolygon) other.geometry)
						.getCoordinates())
					for (double[] pair : array[0])
						for (double[] pair2 : array2[0]) {
							double result = dist(pair, pair2);
							if (result < min1)
								min1 = result;
						}
		else
			return other.distanceTo(this);
		return min1;
	}

	private static double dist(double[] pair, double[] pair2) {
		double R = 6371; // km
		return Math.acos(Math.sin(pair[0] * Math.PI / 180)
				* Math.sin(pair2[0] * Math.PI / 180)
				+ Math.cos(pair[0] * Math.PI / 180)
				* Math.cos(pair2[0] * Math.PI / 180)
				* Math.cos((pair[1] - pair2[1]) * Math.PI / 180))
				* R;
	}

	@Override
	public int compareTo(Country arg0) {
		return properties.name_long.compareTo(arg0.properties.name_long);
	}

	public String getName() {
		return properties.name;
	}

	public String getLongName() {
		return properties.name_long;
	}

	public double getPopulation() {
		return properties.pop_est;
	}

	public String getContinent() {
		return properties.continent;
	}

	public String getDisplayName() {
		String a=properties.name,b=properties.name_long;
		if(a.contains("."))
			return b;
		else if(b.contains("."))
			return a;
		else if(a.equals(a.toUpperCase(Locale.US)))
			return b;
		else if(b.equals(b.toUpperCase(Locale.US)))
			return a;
		else return a;
		//return properties.name.length()<properties.name_long.length()?properties.name_long:properties.name;
	}
}
