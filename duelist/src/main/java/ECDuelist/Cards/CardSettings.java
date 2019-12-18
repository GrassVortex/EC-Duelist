package ECDuelist.Cards;

import ECDuelist.Cards.Actions.ActionLibrary;
import ECDuelist.Cards.Actions.ActionSettings;
import ECDuelist.Cards.Actions.Block;
import ECDuelist.InitializationException;
import ECDuelist.Settings.CardLibrary;
import ECDuelist.Utils.SettingsHelper;
import basemod.helpers.BaseModCardTags;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CardSettings {

	private static ActionLibrary actionLibrary;
	private RawCardSettings rawSettings;
	private CardStrings localizedStrings;

	public String id;
	public String rawId;
	public int cost;
	public AbstractCard.CardType type;
	public AbstractCard.CardColor color;
	public AbstractCard.CardRarity rarity;
	public AbstractCard.CardTarget target;
	public AbstractCard.CardTags[] stsTags;
	public AbstractCard.CardTags[] modTags;
	public String image;
	public CardTextures background;
	public CardTextures orb;
	public CardTextures banner;

	public String name;
	public String description;

	public ActionSettings[] actions;

	public CardSettings(String cardPrefix, String cardId) {
		try {
			rawSettings = loadRawSettings(cardId);
			parseSettings(cardPrefix);
			localizedStrings = CardCrawlGame.languagePack.getCardStrings(id);
			readLocalizedStrings();
		} catch (NumberFormatException ex) {
			throw new InitializationException("Invalid number for card '" + cardId + "'.", ex);
		} catch (IllegalArgumentException ex) {
			throw new InitializationException("Check type, flag, and other spellings for card '" + cardId + "'.", ex);
		}
	}

	public RawCardSettings getRawSettings() {
		return rawSettings;
	}

	public static void initializeStatics() {
		actionLibrary = new ActionLibrary();
		actionLibrary.registerActions();
	}

	private static RawCardSettings loadRawSettings(String cardId) {
		String settingsFileName = "/settings/cards/" + cardId + ".json";
		RawCardSettings rawSettings;
		try (InputStream in = CardLibrary.class.getResourceAsStream(settingsFileName)) {
			Gson reader = new Gson();
			rawSettings = reader.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), RawCardSettings.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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

		finalSettings.cost = SettingsHelper.coalesce(current.cost, base.cost);
		finalSettings.type = SettingsHelper.coalesce(current.type, base.type);
		finalSettings.color = SettingsHelper.coalesce(current.color, base.color);
		finalSettings.rarity = SettingsHelper.coalesce(current.rarity, base.rarity);
		finalSettings.target = SettingsHelper.coalesce(current.target, base.target);
		finalSettings.stsTags = SettingsHelper.coalesce(current.stsTags, base.stsTags);
		finalSettings.modTags = SettingsHelper.coalesce(current.modTags, base.modTags);
		finalSettings.actions = SettingsHelper.coalesce(current.actions, base.actions);
		finalSettings.image = SettingsHelper.coalesce(current.image, base.image);
		finalSettings.background = SettingsHelper.coalesce(current.background, base.background);
		finalSettings.orb = SettingsHelper.coalesce(current.orb, base.orb);
		finalSettings.banner = SettingsHelper.coalesce(current.banner, base.banner);

		return finalSettings;
	}

	private void parseSettings(String cardPrefix) {
		// Apply Prefix to the id in order to avoid conflicts with other mods
		rawId = rawSettings.id;

		id = String.join(":", cardPrefix, rawId);
		cost = Integer.parseInt(rawSettings.cost);

		type = AbstractCard.CardType.valueOf(rawSettings.type);
		color = AbstractCard.CardColor.valueOf(rawSettings.color);
		rarity = AbstractCard.CardRarity.valueOf(rawSettings.rarity);
		target = AbstractCard.CardTarget.valueOf(rawSettings.target);
		stsTags = new AbstractCard.CardTags[rawSettings.stsTags.length];
		for (int i = 0; i < stsTags.length; i++) {
			stsTags[i] = AbstractCard.CardTags.valueOf(rawSettings.stsTags[i]);
		}
		modTags = new AbstractCard.CardTags[rawSettings.modTags.length];
		for (int i = 0; i < modTags.length; i++) {
			String tag = rawSettings.modTags[i];
			switch (tag) {
				case "BasicStrike":
					modTags[i] = BaseModCardTags.BASIC_STRIKE;
					break;
				case "BasicDefend":
					modTags[i] = BaseModCardTags.BASIC_DEFEND;
					break;
				case "Form":
					modTags[i] = BaseModCardTags.FORM;
					break;
				default:
					throw new RuntimeException("Invalid tag name for card '" + rawId + "'");
			}
		}

		if (rawSettings.image != null && !rawSettings.image.isEmpty()) {
			image = rawSettings.image;
		} else {
			// Set default image path if none is specified
			image = "images/" + rawSettings.id + ".png";
		}

		background = rawSettings.background;
		orb = rawSettings.orb;
		banner = rawSettings.banner;

		if (rawSettings.actions != null) {
			actions = new ActionSettings[rawSettings.actions.length];
			for (int i = 0; i < rawSettings.actions.length; i++) {
				JsonObject json = rawSettings.actions[i].getAsJsonObject();

				actions[i] = actionLibrary.parseAction(json);
			}
		} else {
			actions = new ActionSettings[0];
		}
	}

	private void readLocalizedStrings() {
		name = localizedStrings.NAME;
		description = localizedStrings.DESCRIPTION;
	}

	public static class RawCardSettings {
		public String[] bases;

		public String id;
		public String cost;
		public String type;
		public String color;
		public String rarity;
		public String target;
		public String[] stsTags;
		public String[] modTags;

		public String image;
		public CardTextures background;
		public CardTextures orb;
		public CardTextures banner;

		public JsonElement[] actions;

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
			c.stsTags = stsTags.clone();
			c.modTags = modTags.clone();
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
