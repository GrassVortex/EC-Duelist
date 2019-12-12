package ECDuelist.Cards;

import ECDuelist.Settings.CardLibrary;

public class CardFactory {

	public static Card createCard(CardLibrary library, String cardId) {

		CardSettings settings = new CardSettings(library, cardId);
		settings.print();
		Card card = new Card(settings);
		card.postInit();

		return card;
	}
}
