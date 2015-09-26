package net.gostun.ouch.moneypack;

import android.content.Context;
import android.util.Log;
import net.gostun.ouch.R;

import java.util.LinkedList;

public class ExampleMoneyPack extends AbstractMoneyPack {
	/**
	 * List the denominations (and respective resourceIDs) in DECREASING ORDER
	 * epsilon - margin of error for floating point calculations: slightly less than lowest denomination
	 */
	public static final float denominations[] = {100, 50, 10, 5, 1};
	public static final float epsilon = denominations[denominations.length - 1] - 0.001f;
	public static final int resourceIDs[] = {R.drawable.examplebill100dollar, R.drawable.examplebill50dollar, R.drawable.examplebill10dollar, R.drawable.examplebill5dollar, R.drawable.examplebill1dollar};

	@Override
	public String getName() {
		return "Monoply Dollar";
	}

	@Override
	public String getShortName() {
		return "MPD";
	}

	@Override
	public String getSymbol() {
		return "$";
	}


	/**
	 * Perform change-making (based on a greedy, naive algorithm in this case)
	 * Return the number of each denomination required.
	 *
	 * @param amount
	 * @return changeList
	 */
	private int[] makeChange(float amount) {
		int change[] = new int[denominations.length];
		int place = 0;
		while (amount - 0 > epsilon && place < denominations.length) {
			if (amount >= denominations[place])
				amount -= (change[place] = (int) (amount / denominations[place])) * denominations[place];
			place++;
		}
		return change;
	}

	/**
	 * Perform the change-making problem on the input amount
	 * and returns an array of cash view elements for display.
	 *
	 * @param amount
	 * @return cashList
	 */
	@Override
	public AbstractCash[] getCash(float amount, Context context) {
		LinkedList<AbstractCash> cashList = new LinkedList<>();

		// For each denomination.
		int[] change = makeChange(amount);
		for (int place = 0; place < denominations.length; place++) {
			// Count out the number of this denomination required.
			int c = change[place];
			for (int i = 0; i < c; i++) {
				// Add cash view to list
				cashList.add(new Note(context, context.getResources().getDrawable(resourceIDs[place])));
				Log.i("ouch", getSymbol() + denominations[place] + " cash view added.");
			}
		}

		// Return an array of cash views
		return cashList.toArray(new AbstractCash[cashList.size()]);
	}
}
