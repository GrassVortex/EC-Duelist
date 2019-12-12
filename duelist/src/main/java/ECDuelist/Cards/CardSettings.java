package ECDuelist.Cards;

import ECDuelist.InitializationException;
import ECDuelist.Settings.CardLibrary;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CardSettings {

	private RawCardSettings rawSettings;

	public String id;
	public String name;
	public int cost;
	public String description;
	public AbstractCard.CardType type;
	public AbstractCard.CardColor color;
	public AbstractCard.CardRarity rarity;
	public AbstractCard.CardTarget target;
	public AbstractCard.CardTags tags;
	public String image;

	public CardSettings(String cardId) {
		try {
			rawSettings = loadCardSettings(cardId);
			resolveSettings();
		} catch (NumberFormatException ex) {
			throw new InitializationException("Invalid number for card '" + cardId + "'.", ex);
		} catch (IllegalArgumentException ex) {
			throw new InitializationException("Check type, flag, and other spellings for card '" + cardId + "'.", ex);
		}
	}


	private static RawCardSettings loadCardSettings(String cardId) {
		InputStream in = CardLibrary.class.getResourceAsStream("/settings/cards/" + cardId + ".json");
		Gson reader = new Gson();
		RawCardSettings rawSettings = reader.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), RawCardSettings.class);
		RawCardSettings currentSettings;

		if (rawSettings.bases != null) {
			currentSettings = rawSettings.clone();
			// we start at the last base setting and work our way forward, that way we make sure that the later values
			// are not overridden by earlier "more base" values.
			for (int i = rawSettings.bases.length - 1; i >= 0; i--) {
				RawCardSettings baseSettings = loadCardSettings(rawSettings.bases[i]);
				currentSettings = mergeSettings(baseSettings, currentSettings);
			}
		} else {
			currentSettings = rawSettings;
		}

		return currentSettings;
	}

	private static RawCardSettings mergeSettings(RawCardSettings base, RawCardSettings current) {
		RawCardSettings finalSettings = new RawCardSettings();
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

	private static String coalesce(String a, String b) {
		return (a != null) ? a : b;
	}

	private static String[] coalesce(String[] a, String[] b) {
		return (a != null && a.length != 0) ? a : b;
	}

	private void resolveSettings() {
		id = rawSettings.id;
		name = rawSettings.name;
		cost = Integer.parseInt(rawSettings.cost);

		// TODO Translate description
		// CardCrawlGame.languagePack
		description = rawSettings.description;

		type = AbstractCard.CardType.valueOf(rawSettings.type);
		color = AbstractCard.CardColor.valueOf(rawSettings.color);
		rarity = AbstractCard.CardRarity.valueOf(rawSettings.rarity);
		target = AbstractCard.CardTarget.valueOf(rawSettings.target);
		tags = AbstractCard.CardTags.valueOf(rawSettings.tags);
	}

	private static class RawCardSettings {
		public String[] bases;

		public String id;
		public String name;
		public String cost;
		public String description;
		public String type;
		public String color;
		public String rarity;
		public String target;
		public String tags;

		public String[] actions;

		public String image;

		public RawCardSettings clone() {
			RawCardSettings c = new RawCardSettings();
			c.bases = bases.clone();
			c.id = id;
			c.name = name;
			c.cost = cost;
			c.description = description;
			c.type = type;
			c.color = color;
			c.rarity = rarity;
			c.target = target;
			c.tags = tags;
			c.actions = actions.clone();
			c.image = image;
			return c;
		}
	}

}
