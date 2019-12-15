package ECDuelist.Cards;

import ECDuelist.Settings.CardLibrary;

import java.util.HashMap;

public class CardFactory {

	private static HashMap<String, CardSettings> storedSettings;

	public static Card createCard(CardLibrary library, String cardId) {

		CardSettings settings = new CardSettings(library, cardId);
		settings.print();

		if (storedSettings == null) {
			storedSettings = new HashMap<String, CardSettings>();
		}
		storedSettings.put(cardId, settings);

		return instantiateCard(cardId);
	}

	private static Card instantiateCard(String cardId) {
		try {
			//Class cardClass = Class.forName(CardFactory.class.getPackage() + "." + cardId);
			Class cardClass = StrikeCard.class;
			return (Card) cardClass.newInstance();
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException(e);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static CardSettings loadSettings(String cardId) {
		return storedSettings.get(cardId);
	}

}
