package net.gostun.ouch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class KeyPadActivity extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keypad);
	}

	/**
	 * Called when the amount of currency is entered and 'Ok' is pressed.
	 * @param v
	 */
	public void enter(View v){
		EditText editText_amount = (EditText) findViewById(R.id.edittext_amount);
		try {
			/**
			 * Parses the entered amount.
			 */
			float amount = Float.parseFloat(editText_amount.getText().toString());

			/**
			 * A valid amount was entered. Launch FlickActivity.
			 */
			Intent i = new Intent(this, FlickActivity.class);
			i.putExtra("amount", amount);
			startActivity(i);

		} catch (NumberFormatException e) {
			/**
			 * An invalid amount was entered. Notify Layer 8 accordingly.
			 */
			Toast t = Toast.makeText(this.getApplicationContext(), getString(R.string.warning_invalid_amount), Toast.LENGTH_SHORT);
			t.show();
		}
	}
}
