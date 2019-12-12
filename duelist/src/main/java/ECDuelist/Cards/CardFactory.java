package ECDuelist.Cards;

public class CardFactory {

	public static Card createCard(String cardId) {

		CardSettings settings = new CardSettings(cardId);
		Card card = new Card(settings);

		return card;
	}
}
