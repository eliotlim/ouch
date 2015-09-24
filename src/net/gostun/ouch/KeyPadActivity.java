package net.gostun.ouch;

import android.app.Activity;
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
		setContentView(R.layout.main);
	}

	public void enter(View v){
		EditText editText_amount = (EditText) findViewById(R.id.edittext_amount);
		try {
			float amount = Float.parseFloat(editText_amount.getText().toString());
			Toast t = Toast.makeText(this.getApplicationContext(), getString(R.string.info_counting_amount)+amount, Toast.LENGTH_SHORT);
			t.show();
		} catch (NumberFormatException e) {
			Toast t = Toast.makeText(this.getApplicationContext(), getString(R.string.warning_invalid_amount), Toast.LENGTH_SHORT);
			t.show();
		}
	}
}
