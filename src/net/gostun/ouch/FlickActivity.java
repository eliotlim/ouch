package net.gostun.ouch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
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
		Intent i = getIntent();
		float amount = i.getFloatExtra("amount", Float.NaN);
		if (amount == Float.NaN) {
			Toast.makeText(this, R.string.warning_invalid_amount, Toast.LENGTH_SHORT).show();
		}

		/**
		 * Load MoneyPack using reflection and delegate
		 * change-making algorithm to MoneyPack.
		 */
		try {
			AbstractMoneyPack moneyPack = (AbstractMoneyPack) Class.forName(i.getStringExtra("MoneyPack")).newInstance();
			AbstractCash[] cashList = moneyPack.getCash(amount, this);
			for (AbstractCash cash : cashList) {
				if (cash != null)
					flickView.addView(cash);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}
