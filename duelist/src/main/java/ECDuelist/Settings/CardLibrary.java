package ECDuelist.Settings;

import ECDuelist.Cards.Card;
import ECDuelist.Cards.CardSettings;
import ECDuelist.Utils.Path;
import basemod.BaseMod;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class CardLibrary {

	private static HashMap<String, CardSettings> cardSettings;

	private Settings settings;
	private ArrayList<Card> cards;

	public CardLibrary() {
		if (cardSettings != null) {
			throw new IllegalStateException("Can't create more than one " + CardLibrary.class.getName());
		}

		cardSettings = new HashMap<String, CardSettings>();
		CardSettings.initializeStatics();

		try (InputStream in = CardSettings.class.getResourceAsStream(Path.SettingsPath + "cardLibrary.json")) {
			Gson reader = new Gson();
			settings = reader.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), Settings.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void createCards() {
		cards = new ArrayList<Card>();

		for (String cardId : settings.cards) {
			createCard(cardId);
		}

		registerCards();
		setLockStatus();
	}

	// Only intended to be used by subclasses of Card to get their settings
	public static CardSettings getSettings(String cardId) {
		return cardSettings.get(cardId);
	}

	private void createCard(String cardId) {
		// Create and save the card setting so that the card instance can load it by name later
		CardSettings s = new CardSettings(cardId);
		cardSettings.put(cardId, s);

		Card card = new Card(cardId);
		cards.add(card);
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

	private static class Settings {
		public String[] cards;
	}
}
