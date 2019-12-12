package ECDuelist.Settings;

import ECDuelist.Cards.Card;
import basemod.BaseMod;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CardLibrary {

	private ArrayList<Card> cards;

	public CardLibrary() {
		InputStream in = CardLibrary.class.getResourceAsStream("/settings/cardLibrary.json");
		Gson reader = new Gson();
		LibraryList list = reader.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), LibraryList.class);

		cards = new ArrayList<Card>();
		for (int i = 0; i < list.Cards.length; i++) {
			cards.add(loadCard(list.Cards[i]));
		}
	}

	private Card loadCard(String cardId) {
		return Card.createCard(cardId);
	}

	public void registerCards() {
		for (int i = 0; i < cards.size(); i++) {
			BaseMod.addCard(cards.get(i));
		}
	}

	private class LibraryList {
		public String[] Cards;
	}
}
