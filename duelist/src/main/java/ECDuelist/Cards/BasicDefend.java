package ECDuelist.Cards;

import ECDuelist.Utils.Constants;

public class BasicDefend extends Card {

	public static final String RawId = BasicDefend.class.getSimpleName();
	public static final String PrefixedId = Constants.ModPrefix + RawId;

	public BasicDefend() {
		super(RawId);
	}
}
