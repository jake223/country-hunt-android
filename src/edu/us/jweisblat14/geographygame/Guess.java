package edu.us.jweisblat14.geographygame;

import edu.us.jweisblat14.geography.Country;




public class Guess implements Comparable<Guess> {
	Country country;
	double value;
	public Guess(Country countryData)
	{
		country=countryData;
	}
	@Override
	public int compareTo(Guess other) {
		return Double.valueOf(value).compareTo(other.value);
	}
	public String toString()
	{
		return country.toString()+" ("+(int)(value)+")";
	}
}
