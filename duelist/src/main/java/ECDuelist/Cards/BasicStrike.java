package ECDuelist.Cards;

import ECDuelist.Utils.Constants;

public class BasicStrike extends Card {

	public static final String RawId = BasicStrike.class.getSimpleName();
	public static final String PrefixedId = Constants.ModPrefix + RawId;

	public BasicStrike() {
		super(RawId);
	}
}
