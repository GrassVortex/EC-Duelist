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

	private LibrarySettings settings;

	private ArrayList<Card> cards;

	public CardLibrary() {
		InputStream in = CardLibrary.class.getResourceAsStream("/settings/cardLibrary.json");
		Gson reader = new Gson();
		settings = reader.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), LibrarySettings.class);
	}

	public void loadAllCards() {
		cards = new ArrayList<Card>();
		for (int i = 0; i < settings.cards.length; i++) {
			cards.add(loadCard(settings.cards[i]));
		}
	}

	public String getModPrefix() {
		return settings.modPrefix;
	}

	private Card loadCard(String cardId) {
		Card card = CardFactory.createCard(this, cardId);
		return card;
	}

	public void registerCards() {
		for (int i = 0; i < cards.size(); i++) {
			Card card = cards.get(i);
			BaseMod.addCard(card);
		}

		setLockStatus();
	}


	private void setLockStatus() {
		for (int i = 0; i < cards.size(); i++) {
			Card card = cards.get(i);

			if (card.isUnlocked()) {
				UnlockTracker.unlockCard(card.getPrefixedId());
			}
		}
	}

	private class LibrarySettings {
		public String[] cards;
		public String modPrefix;
	}
}
