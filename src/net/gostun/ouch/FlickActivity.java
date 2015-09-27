package net.gostun.ouch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import net.gostun.ouch.moneypack.AbstractCash;
import net.gostun.ouch.moneypack.AbstractMoneyPack;

public class FlickActivity extends Activity {

	/**
	 * Called when the activity is first created.
	 *
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * Inflate a FrameLayout and sets it
		 * as the contentView for this Activity.
		 */
		setContentView(R.layout.flick);
		FrameLayout flickView = (FrameLayout) findViewById(R.id.flickView);

		/**
		 * Retrieve and validate entered amount
		 * from the intent.
		 */
		Intent intent = getIntent();
		float amount = intent.getFloatExtra("amount", Float.NaN);
		if (amount == Float.NaN) {
			Toast.makeText(this, R.string.warning_invalid_amount, Toast.LENGTH_SHORT).show();
		}

		/**
		 * Draw the cash ImageViews on the screen
		 */
		try {
			// Load MoneyPack using reflection and delegate change-making algorithm to MoneyPack.
			AbstractMoneyPack moneyPack = (AbstractMoneyPack) Class.forName(intent.getStringExtra("MoneyPack")).newInstance();
			AbstractCash[] cashList = moneyPack.getCash(amount, this);
			if (cashList.length <= 0) {
				// No cash to be displayed
				TextView tv = (TextView) findViewById(R.id.statusText);
				tv.setText(R.string.warning_invalid_amount);
			} else {
				// Populate FlickActivity with cash ImageViews
				// Calculate the angle separation between each ImageView
				int fanStartD = 60, fanEndD = 120, i = 0;
				float inc = (cashList.length == 1) ? 0 : (fanEndD - fanStartD) / (float) cashList.length - 1;
				for (AbstractCash cash : cashList) {
					// Add ImageView to FrameLayout with angle offset
					if (cash != null) {
						flickView.addView(cash);
						cash.setRotation(fanStartD + inc * (i++));
					}
				}
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// Handle any issues with loading the MoneyPack dynamically
			e.printStackTrace();
			//TODO: Notify Layer 8 of the problem
		}

	}
}
