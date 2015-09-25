package net.gostun.ouch.moneypak;

public abstract class AbstractMoneyPack {
	public abstract String getName();
	public abstract String getShortName();
	public abstract String getSymbol();

	protected AbstractNote[] notes;
	protected AbstractCoin[] coins;
}
