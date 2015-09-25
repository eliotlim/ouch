package net.gostun.ouch.moneypak;

public class ExampleMoneyPack extends AbstractMoneyPack {

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
}
