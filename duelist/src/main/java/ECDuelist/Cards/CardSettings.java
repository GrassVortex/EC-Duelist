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
	public String rawId;
	public String name;
	public int cost;
	public String description;
	public AbstractCard.CardType type;
	public AbstractCard.CardColor color;
	public AbstractCard.CardRarity rarity;
	public AbstractCard.CardTarget target;
	public AbstractCard.CardTags stsTags;
	public String image;
	public CardTextures background;
	public CardTextures orb;
	public CardTextures banner;

	public CardSettings(CardLibrary library, String cardId) {
		try {
			rawSettings = loadCardSettings(cardId);
			resolveSettings(library);
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
			// we start at the last (most base) base setting and work our way forward, that way we make sure that the later values
			// are not overridden by earlier "more base" values.
			for (int i = rawSettings.bases.length - 1; i >= 0; i--) {
				RawCardSettings baseSettings = loadCardSettings(rawSettings.bases[i]);
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
		finalSettings.name = current.name;

		finalSettings.cost = coalesce(current.cost, base.cost);
		finalSettings.description = coalesce(current.description, base.description);
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

	public void print() {
		System.out.printf("id %s%nname %s%ncost %d%ndescription %s%ntype %s%ncolor %s%nrarity %s%ntarget %s%nactions %s%nimage %s%n",
				  id, name, cost, description, type, color, rarity, target, format(rawSettings.actions), image);
	}

	public static String format(String[] list) {
		return format(list, ", ");
	}

	public static String format(String[] list, String separator) {
		String result = "";
		if (list == null || list.length == 0) {
			return result;
		}

		result = list[0];
		// Note that the loop starts at 1, not zero
		for (int i = 1; i < list.length; i++) {
			result += separator + list[i];
		}
		return result;
	}

	private void resolveSettings(CardLibrary library) {
		// Apply Prefix to the id in order to avoid conflicts with other mods
		rawId = rawSettings.id;
		id = library.getModPrefix() + rawId;
		name = rawSettings.name;
		cost = Integer.parseInt(rawSettings.cost);

		// TODO Translate description
		// CardCrawlGame.languagePack
		description = rawSettings.description;

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
			c.name = name;
			c.cost = cost;
			c.description = description;
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
