package ECDuelist.Settings;

import ECDuelist.Cards.Card;
import ECDuelist.Cards.CardSettings;
import ECDuelist.Cards.BasicDefend;
import ECDuelist.Cards.BasicStrike;
import basemod.BaseMod;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.ArrayList;
import java.util.HashMap;

public class CardLibrary {

	private static HashMap<String, CardSettings> cardSettings;

	private String cardPrefix;
	private ArrayList<Card> cards;

	public CardLibrary(String cardPrefix) {
		if (cardSettings != null) {
			throw new IllegalStateException("Can't create more than one " + CardLibrary.class.getName());
		}

		this.cardPrefix = cardPrefix;
		cardSettings = new HashMap<String, CardSettings>();
		CardSettings.initializeStatics();
	}

	public void createCards() {
		cards = new ArrayList<Card>();

		createCard(BasicStrike.class);
		createCard(BasicDefend.class);

		registerCards();
		setLockStatus();
	}

	// Only intended to be used by subclasses of Card to get their settings
	public static CardSettings getSettings(String cardId) {
		return cardSettings.get(cardId);
	}

	private void createCard(Class cardClass) {
		String cardId = cardClass.getSimpleName();
		System.out.println("createCard cardId " + cardId);
		// Create and save the card setting so that the card instance can load it by name later
		CardSettings s = new CardSettings(cardPrefix, cardId);
		s.print();
		cardSettings.put(cardId, s);
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


}
