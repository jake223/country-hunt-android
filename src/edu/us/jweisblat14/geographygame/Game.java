package edu.us.jweisblat14.geographygame;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import com.google.gson.GsonBuilder;

import edu.us.jweisblat14.geography.Country;
import edu.us.jweisblat14.geography.LevenshteinDistance;
import edu.us.jweisblat14.geography.Polygon;
import edu.us.jweisblat14.geography.PolygonDeserializer;

public class Game implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7758866443892773697L;
	public static final int FOUND = 10;
	public static final int REFERENCE = -1;
	public static final int CLOSER = 1;
	public static final int EQUAL = 2;
	public static final int FARTHER = 0;

	private Country hidden, last, prev;
	private ArrayList<Integer> results1;
	private ArrayList<String> results2;
	private long startTime;
	private boolean isRandom;
	private static Country[] countryData;

	public ArrayList<Integer> getResults1() {
		return results1;
	}

	public ArrayList<String> getResults2() {
		return results2;
	}

	public static void loadDataFromJson(final InputStream str/* ,Context context */) {
		try {
			ObjectInputStream os = new ObjectInputStream(str);
			countryData = (Country[]) os.readObject();
		} catch (IOException e) {

			GsonBuilder g = new GsonBuilder();
			g.registerTypeAdapter(Polygon.class, new PolygonDeserializer());
			countryData = g.create().fromJson(new InputStreamReader(str),
					CountryData.class).features;
		} catch (ClassNotFoundException e) {
			System.exit(1);
		}
	}

	public Game() {
		super();
		results1 = new ArrayList<Integer>();
		results2 = new ArrayList<String>();
		newGame(true);
	}

	/**
	 * Chooses a random country to play with
	 */
	public void newGame(boolean isRandom) {
		Random r = new Random();
		int rand = r.nextInt(countryData.length);
		while (countryData[rand] == hidden)
			rand = r.nextInt(countryData.length);
		hidden = countryData[rand];
		prev = null;
		last = null;
		results1.clear();
		results2.clear();
		startTime = System.currentTimeMillis();
		this.isRandom = isRandom;

	}

	/**
	 * Guesses a country
	 * 
	 * @param guess
	 *            the country we're guessing
	 * @return result of guess - FOUND,CLOSER,EQUAL,FARTHER
	 */
	public int makeGuess(Country guess) {
		int res = makeGuess2(guess);
		results1.add(res);
		results2.add(guess.getName());
		return res;
	}

	private int makeGuess2(Country guess) {
		if (guess == hidden)
			return Game.FOUND;
		prev = last;
		last = guess;
		if (prev == null)
			return Game.REFERENCE;
		return last.distanceTo(hidden) < prev.distanceTo(hidden) ? Game.CLOSER
				: last.distanceTo(hidden) == prev.distanceTo(hidden) ? Game.EQUAL
						: Game.FARTHER;
	}

	/**
	 * Gets the country we think the user means
	 * 
	 * @param input
	 *            a string provided by the user
	 * @returns the closest country to that string
	 */
	public static Country getGuess(String input) {
		double best = 5000;
		Country best1 = null;
		double a = 0;
		for (Country c : countryData) {
			if ((a = LevenshteinDistance
					.computeEditDistance(c.getName(), input)) < best) {
				best = a;
				best1 = c;
			}
			if ((a = LevenshteinDistance.computeEditDistance(c.getLongName(),
					input)) < best) {
				best = a;
				best1 = c;
			}
		}
		return best1;
	}

	public void newGame(String continent) {
		Random r = new Random();
		int rand = r.nextInt(countryData.length);
		while (countryData[rand] == hidden
				|| !countryData[rand].getContinent().equals(continent))
			rand = r.nextInt(countryData.length);
		hidden = countryData[rand];
		prev = null;
		last = null;
		results1.clear();
		results2.clear();
		this.isRandom = false;
	}

	public void newChoiceGame(Country guess) {
		newGame(false);
		hidden = guess;
	}

	public String[] getCountriesStrings() {
		String[] strings = new String[countryData.length];
		for (int i = 0; i < countryData.length; i++)
			strings[i] = countryData[i].getDisplayName();
		return strings;
	}

	public Country getHidden() {
		return hidden;
	}
	public boolean isRandom(){
		return this.isRandom;
	}
	public int[] winString() {
		int time=(int)((System.currentTimeMillis()-startTime)/1000);
		int guesses=results2.size();
		return new int[]{time,guesses};
	}
}

class CountryData {
	String type;
	Country[] features;
}