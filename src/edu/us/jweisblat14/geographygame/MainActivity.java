package edu.us.jweisblat14.geographygame;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import edu.us.jweisblat14.geography.Country;

public class MainActivity extends Activity {
	Game game = null;
	private ProgressDialog progress;
	final static Bool b = new Bool();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AutoCompleteTextView editText = ((AutoCompleteTextView) findViewById(R.id.editText));
		editText.setImeActionLabel("Guess", KeyEvent.KEYCODE_ENTER);
		b.b = true;
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
					MainActivity.b.b = !MainActivity.b.b;
					if (MainActivity.b.b)
						return true;
					if(v.getText().toString().equals(""))
						return true;
					guess(v.getText().toString());
					v.setText("");
					return true;
				}
				return false;
			}
		});
		if (savedInstanceState != null) {
			game = (Game) savedInstanceState.getSerializable("game");
			for (int i = 0; i < game.getResults1().size(); i++) {
				ResultRow r = new ResultRow(this);
				r.setLeftText(game.getResults2().get(i));
				switch (game.getResults1().get(i)) {
				case Game.FOUND:
					r.setRightText("CORRECT!");
					break;
				case Game.FARTHER:
					r.setRightText("Farther");
					break;
				case Game.CLOSER:
					r.setRightText("Closer");
					break;
				case Game.EQUAL:
					r.setRightText("Equal");
					break;
				case Game.REFERENCE:
					r.setRightText("Reference");
				}
				((TableLayout) findViewById(R.id.table)).addView(r, 0);
			}

		}
		if (game == null) {
			progress = new ProgressDialog(this);
			progress.setTitle("Loading");
			progress.setMessage("Please wait while the border data loads...");
			progress.show();

			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					Game.loadDataFromJson(getResources().openRawResource(
							R.raw.country));
					game = new Game();
					runOnUiThread(new Runnable() {
						public void run() {
							MainActivity.this.dismissProgress();
							MainActivity.this.setupAutocomplete();
						}
					});
				}
			};
			Thread mythread = new Thread(runnable);
			mythread.start();
		} else {
			setupAutocomplete();
		}
		((TableLayout) findViewById(R.id.table)).setColumnStretchable(1, true);

	}

	protected void setupAutocomplete() {
		final AutoCompleteTextView editText = ((AutoCompleteTextView) findViewById(R.id.editText));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, game.getCountriesStrings());
		editText.setAdapter(adapter);
		editText.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				guess((String) arg0.getItemAtPosition(position));
				editText.setText("");
			}
		});
	}

	protected void dismissProgress() {
		progress.dismiss();
	}

	String last = "";

	public void guess(View sender) {
		AutoCompleteTextView editText = (AutoCompleteTextView) findViewById(R.id.editText);
		guess(editText.getText().toString());
		editText.setText("");
	}

	protected void guess(String string) {
		if (string.equals(last))
			return;
		Country guess = Game.getGuess(string);
		string = guess.getName();
		last = string;
		int res = game.makeGuess(guess);
		ResultRow r = new ResultRow(this);
		r.setLeftText(string);
		switch (res) {
		case Game.FOUND:
			r.setRightText("CORRECT!");
			dialog("You win!","The country was "+game.getHidden().getName()+"!");
			break;
		case Game.FARTHER:
			r.setRightText("Farther");
			break;
		case Game.CLOSER:
			r.setRightText("Closer");
			break;
		case Game.EQUAL:
			r.setRightText("Equal");
			break;
		case Game.REFERENCE:
			r.setRightText("Reference");
		default:
			Log.i("Geogame", "" + res);
		}
		((TableLayout) findViewById(R.id.table)).addView(r, 0);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_newgame:
			newGame();
			return true;
		case R.id.action_giveup:
			giveUp();
			return true;
		case R.id.action_hint:
			hint();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	private boolean hinted=false;
	private void hint() {
		if(hinted){
		double logpop = Math.log10(game.getHidden().getPopulation());
		String lowerBound = word((int) Math.floor(logpop));
		String upperBound = word((int) Math.ceil(logpop));
		dialog("Hint", lowerBound + " < Population < " + upperBound);
		}
		else
			dialog("Hint","The country is in "+game.getHidden().getContinent());
		hinted=true;
	}

	private String word(int ceil) {
		switch (ceil) {
		case 1:
			return "Ten";
		case 2:
			return "100";
		case 3:
			return "1000";
		case 4:
			return "10,000";
		case 5:
			return "100,000";
		case 6:
			return "1M";
		case 7:
			return "10M";
		case 8:
			return "100M";
		case 9:
			return "1B";
		case 10:
			return "10B";
		}
		throw new RuntimeException("Crazy Big Country??");
	}

	private void dialog(String title, String message) {
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// 2. Chain together various setter methods to set the dialog
		// characteristics
		builder.setMessage(message).setTitle(title);

		// Add the buttons
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// User clicked OK button
					}
				});

		builder.create().show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (game == null) {
			game = (Game) savedInstanceState.getSerializable("game");
			for (int i = 0; i < game.getResults1().size(); i++) {
				ResultRow r = new ResultRow(this);
				r.setLeftText(game.getResults2().get(i));
				switch (game.getResults1().get(i)) {
				case Game.FOUND:
					r.setRightText("CORRECT!");
					break;
				case Game.FARTHER:
					r.setRightText("Farther");
					break;
				case Game.CLOSER:
					r.setRightText("Closer");
					break;
				case Game.EQUAL:
					r.setRightText("Equal");
					break;
				case Game.REFERENCE:
					r.setRightText("Reference");
				}
				((TableLayout) findViewById(R.id.table)).addView(r, 0);
			}
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("game", game);
		super.onSaveInstanceState(outState);
	}

	void giveUp() {
		dialog(getResources().getString(R.string.giveup_title),
				"The country was: " + game.getHidden().getName());
		findViewById(R.id.editText).setEnabled(false);
	}

	void newGame() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.newGame).setItems(
				new String[] { "Random Country", "Choose Continent",
						"Enter Country" },
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							game.newGame(true);
							MainActivity.this.hinted=false;
							break;
						case 1:
							newContinentGame();
							break;
						case 2:
							newChoiceGame();
						}
						((TableLayout) findViewById(R.id.table))
								.removeAllViews();
						findViewById(R.id.editText).setEnabled(true);
					}

				});
		builder.create().show();
	}

	protected void newChoiceGame() {
		AlertDialog.Builder editalert = new AlertDialog.Builder(this);

		editalert.setTitle("Choose Hidden Country");
		editalert.setMessage("Enter a country for a friend to guess:");

		final EditText input = new EditText(this);
		input.setHeight(100);
		editalert.setView(input);
		input.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		editalert.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						game.newChoiceGame(Game.getGuess(input.getText()
								.toString()));

					}
				});

		editalert.show();
	}

	private void newContinentGame() {
		findViewById(R.id.editText).setEnabled(true);
		((TableLayout) findViewById(R.id.table)).removeAllViews();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] str = { "Asia", "Africa", "Europe", "N. America",
				"S. America", "Oceania" };
		builder.setTitle(R.string.newGame).setItems(str,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 3:
							game.newGame("North America");
							break;
						case 4:
							game.newGame("South America");
							break;
						default:
							game.newGame(str[which]);
						}
					}

				});
		builder.create().show();
	}
}

class Bool {
	boolean b;
}