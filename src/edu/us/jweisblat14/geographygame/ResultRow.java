package edu.us.jweisblat14.geographygame;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;

public class ResultRow extends TableRow {
	private TextView right, left;

	public ResultRow(Context context) {
		super(context);

		right = new TextView(context);

		right.setGravity(Gravity.RIGHT);
		left = new TextView(context);
		left.setGravity(Gravity.LEFT);
		setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		right.setTextAppearance(context, android.R.style.TextAppearance_Large);
		left.setTextAppearance(context, android.R.style.TextAppearance_Large);
		addView(left);
		addView(right);
		// addView(rl);
	}

	public void setLeftText(String string) {
		left.setText(string);
	}

	public void setRightText(String string) {
		right.setText(string);
	}

}
