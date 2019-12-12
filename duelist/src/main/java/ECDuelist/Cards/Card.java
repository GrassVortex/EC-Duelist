package ECDuelist.Cards;

import ECDuelist.Settings.CardLibrary;
import basemod.abstracts.CustomCard;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Card extends
		  CustomCard {

	private class CardSettings {
		public String[] bases;

		public String id;
		public String name;
		public String cost;
		public String description;
		public String type;
		public String color;
		public String rarity;
		public String target;

		public String[] actions;

		public String image;

		public CardSettings clone(){
			CardSettings c = new CardSettings();
			c.bases = bases.clone();
			c.id = id;
			c.name = name;
			c.cost = cost;
			c.description = description;
			c.type = type;
			c.color = color;
			c.rarity = rarity;
			c.target = target;
			c.actions = actions.clone();
			c.image = image;
			return c;
		}
	}


	public Card(String cardId) {
		CardSettings settings = loadCard(cardId);

		String description = settings.description;

				  super(settings.id, settings.name, settings.image, settings.cost, settings.description, );

	}

	private CardSettings loadCard(String cardId) {
		InputStream in = CardLibrary.class.getResourceAsStream("/settings/cards/" + cardId + ".json");
		Gson reader = new Gson();
		CardSettings rawSettings = reader.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), CardSettings.class);
		CardSettings currentSettings;

		if (rawSettings.bases != null) {
			currentSettings = rawSettings.clone();
			// we start at the last base setting and work our way forward, that way we make sure that the later values
			// are not overridden by earlier "more base" values.
			for (int i = rawSettings.bases.length - 1; i >= 0; i--) {
				CardSettings baseSettings = loadCard(rawSettings.bases[i]);
				currentSettings = mergeSettings(baseSettings, currentSettings);
			}
		} else {
			currentSettings = rawSettings;
		}

		return currentSettings;
	}

	private CardSettings mergeSettings(CardSettings base, CardSettings current) {
		CardSettings finalSettings = current.clone();
		// The id and name should ALWAYS be from the current setting, not the base/parent
		finalSettings.id = current.id;
		finalSettings.name = current.name;

		finalSettings.cost = coalesce(base.cost, current.cost);
		finalSettings.description = coalesce(base.description, current.description);
		finalSettings.type = coalesce(base.type, current.type);
		finalSettings.color = coalesce(base.color, current.color);
		finalSettings.rarity = coalesce(base.rarity, current.rarity);
		finalSettings.target = coalesce(base.target, current.target);
		finalSettings.actions = coalesce(base.actions, current.actions);
		finalSettings.image = coalesce(base.image, current.image);
		return finalSettings;
	}

	private String coalesce(String a, String b) {
		return (a != null) ? a : b;
	}

	private String[] coalesce(String[] a, String[] b) {
		return (a != null && a.length != 0) ? a : b;
	}

	@Override
	public void upgrade() {

	}

	@Override
	public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {

	}
}
