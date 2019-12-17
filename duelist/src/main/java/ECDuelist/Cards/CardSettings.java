package ECDuelist.Cards;

import ECDuelist.InitializationException;
import ECDuelist.Settings.CardLibrary;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CardSettings {

	private RawCardSettings rawSettings;
	private CardStrings localizedStrings;

	public String id;
	public String rawId;
	public int cost;
	public AbstractCard.CardType type;
	public AbstractCard.CardColor color;
	public AbstractCard.CardRarity rarity;
	public AbstractCard.CardTarget target;
	public AbstractCard.CardTags stsTags;
	public String image;
	public CardTextures background;
	public CardTextures orb;
	public CardTextures banner;

	public String name;
	public String description;

	public CardSettings(String cardPrefix, String cardId) {
		try {
			rawSettings = loadRawSettings(cardId);
			resolveSettings(cardPrefix);
			localizedStrings = CardCrawlGame.languagePack.getCardStrings(id);
			readLocalizedStrings();
		} catch (NumberFormatException ex) {
			throw new InitializationException("Invalid number for card '" + cardId + "'.", ex);
		} catch (IllegalArgumentException ex) {
			throw new InitializationException("Check type, flag, and other spellings for card '" + cardId + "'.", ex);
		}
	}

	private static RawCardSettings loadRawSettings(String cardId) {
		String settingsFileName = "/settings/cards/" + cardId + ".json";
		System.out.println("settingsFileName " + settingsFileName);
		InputStream in = CardLibrary.class.getResourceAsStream(settingsFileName);
		Gson reader = new Gson();
		RawCardSettings rawSettings = reader.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), RawCardSettings.class);
		RawCardSettings currentSettings;

		if (rawSettings.bases != null) {
			currentSettings = rawSettings.clone();
			// we start at the last (most base) base setting and work our way forward, that way we make sure that the later values
			// are not overridden by earlier "more base" values.
			for (int i = rawSettings.bases.length - 1; i >= 0; i--) {
				RawCardSettings baseSettings = loadRawSettings(rawSettings.bases[i]);
				currentSettings = mergeSettings(currentSettings, baseSettings);
			}
		} else {
			currentSettings = rawSettings;
		}

		return currentSettings;
	}

	private static RawCardSettings mergeSettings(RawCardSettings current, RawCardSettings base) {
		RawCardSettings finalSettings = new RawCardSettings();
		// The id and name should ALWAYS be from the current setting, not the base/parent
		finalSettings.id = current.id;

		finalSettings.cost = coalesce(current.cost, base.cost);
		finalSettings.type = coalesce(current.type, base.type);
		finalSettings.color = coalesce(current.color, base.color);
		finalSettings.rarity = coalesce(current.rarity, base.rarity);
		finalSettings.target = coalesce(current.target, base.target);
		finalSettings.stsTags = coalesce(current.stsTags, base.stsTags);
		finalSettings.actions = coalesce(current.actions, base.actions);
		finalSettings.image = coalesce(current.image, base.image);
		finalSettings.background = coalesce(current.background, base.background);
		finalSettings.orb = coalesce(current.orb, base.orb);
		finalSettings.banner = coalesce(current.banner, base.banner);

		return finalSettings;
	}

	private static String coalesce(String a, String b) {
		return (a != null && !a.isEmpty()) ? a : b;
	}

	private static String[] coalesce(String[] a, String[] b) {
		return (a != null && a.length != 0) ? a : b;
	}

	private static CardTextures coalesce(CardTextures a, CardTextures b) {
		return (a != null) ? a : b;
	}

	private void resolveSettings(String cardPrefix) {
		// Apply Prefix to the id in order to avoid conflicts with other mods
		rawId = rawSettings.id;

		id = String.join(":", cardPrefix, rawId);
		cost = Integer.parseInt(rawSettings.cost);

		type = AbstractCard.CardType.valueOf(rawSettings.type);
		color = AbstractCard.CardColor.valueOf(rawSettings.color);
		rarity = AbstractCard.CardRarity.valueOf(rawSettings.rarity);
		target = AbstractCard.CardTarget.valueOf(rawSettings.target);
		stsTags = AbstractCard.CardTags.valueOf(rawSettings.stsTags);

		if (rawSettings.image != null && !rawSettings.image.isEmpty()) {
			image = rawSettings.image;
		} else {
			// Set default image path if none is specified
			image = "images/" + rawSettings.id + ".png";
		}

		background = rawSettings.background;
		orb = rawSettings.orb;
		banner = rawSettings.banner;
	}

	private void readLocalizedStrings() {
		name = localizedStrings.NAME;
		description = localizedStrings.DESCRIPTION;
	}

	private static class RawCardSettings {
		public String[] bases;

		public String id;
		public String cost;
		public String type;
		public String color;
		public String rarity;
		public String target;
		// TODO make list
		public String stsTags;
		// TODO Make custom type
		public String[] actions;
		public String image;
		public CardTextures background;
		public CardTextures orb;
		public CardTextures banner;

		public RawCardSettings() {
			background = new CardTextures();
			orb = new CardTextures();
			banner = new CardTextures();
		}


		public RawCardSettings clone() {
			RawCardSettings c = new RawCardSettings();
			c.bases = bases.clone();
			c.id = id;
			c.cost = cost;
			c.type = type;
			c.color = color;
			c.rarity = rarity;
			c.target = target;
			c.stsTags = stsTags;
			c.actions = actions.clone();
			c.image = image;
			c.background = background.clone();
			c.orb = orb.clone();
			c.banner = banner.clone();
			return c;
		}
	}

	public static class CardTextures {
		public String small;
		public String large;

		public CardTextures clone() {
			CardTextures c = new CardTextures();
			c.small = small;
			c.large = large;
			return c;
		}
	}

}
