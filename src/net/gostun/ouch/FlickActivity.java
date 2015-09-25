package net.gostun.ouch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FlickActivity extends Activity {

	/**
	 * Called when the activity is first created.
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flick);

		Intent i = getIntent();
		float amount = i.getFloatExtra("amount", 0);
		TextView textView = (TextView) findViewById(R.id.textView);
		textView.setText(getString(R.string.info_flicking_amount)+amount);
	}


}
