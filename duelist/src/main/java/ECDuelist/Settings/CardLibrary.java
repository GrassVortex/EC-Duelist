package ECDuelist.Settings;

import ECDuelist.Cards.Card;
import ECDuelist.Cards.CardSettings;
import ECDuelist.Cards.BasicDefend;
import ECDuelist.Cards.BasicStrike;
import basemod.BaseMod;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class CardLibrary {

	private static HashMap<String, CardSettings> cardSettings;

	private LibrarySettings settings;
	private ArrayList<Card> cards;

	public CardLibrary() {
		if (cardSettings != null) {
			throw new IllegalStateException("Can't create more than one " + CardLibrary.class.getName());
		}

		InputStream in = CardLibrary.class.getResourceAsStream("/settings/cardLibrary.json");
		Gson reader = new Gson();
		settings = reader.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), LibrarySettings.class);
		cardSettings = new HashMap<String, CardSettings>();
	}

	public void createCards() {
		cards = new ArrayList<Card>();

		createCard(BasicStrike.class);
		createCard(BasicDefend.class);

		registerCards();
		setLockStatus();
	}

	public static CardSettings getSettings(String cardId) {
		return cardSettings.get(cardId);
	}

	public String getModPrefix() {
		return settings.modPrefix;
	}

	private void createCard(Class cardClass) {
		String cardId = cardClass.getSimpleName();
		System.out.println("createCard cardId " + cardId);
		// Create and save the card setting so that the card instance can load it by name later
		cardSettings.put(cardId, new CardSettings(this, cardId));
		Card card = instantiateCard(cardClass);
		cards.add(card);
	}

	private Card instantiateCard(Class cardClass) {
		try {
			Card card = (Card) cardClass.newInstance();
			return card;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private void registerCards() {
		for (int i = 0; i < cards.size(); i++) {
			Card card = cards.get(i);
			BaseMod.addCard(card);
		}
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
