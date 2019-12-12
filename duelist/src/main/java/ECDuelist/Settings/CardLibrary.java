package ECDuelist.Settings;

import ECDuelist.Cards.Card;
import ECDuelist.Cards.CardFactory;
import basemod.BaseMod;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CardLibrary {

	private ArrayList<Card> cards;

	public CardLibrary() {
		InputStream in = CardLibrary.class.getResourceAsStream("/settings/cardLibrary.json");
		Gson reader = new Gson();
		LibrarySettings list = reader.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), LibrarySettings.class);

		cards = new ArrayList<Card>();
		for (int i = 0; i < list.cards.length; i++) {
			cards.add(loadCard(list.cards[i]));
		}
	}

	private Card loadCard(String cardId) {
		return CardFactory.createCard(cardId);
	}

	public void registerCards() {
		for (int i = 0; i < cards.size(); i++) {
			Card card = cards.get(i);
			BaseMod.addCard(card);

			if (card.isUnlocked()) {
				UnlockTracker.unlockCard(card.getPrefixedId());
			}
		}
	}

	public class LibrarySettings {
		public String[] cards;
		public String modPrefix;
	}
}
