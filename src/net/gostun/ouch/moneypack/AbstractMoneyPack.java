package net.gostun.ouch.moneypack;

import android.content.Context;

public abstract class AbstractMoneyPack {
	public abstract String getName();
	public abstract String getShortName();
	public abstract String getSymbol();

	protected AbstractCash[] notes;
	protected Coin[] coins;

	public abstract AbstractCash[] getCash(float amount, Context context);
}
