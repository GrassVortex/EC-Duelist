package ECDuelist.Characters;

import ECDuelist.Cards.Card;
import ECDuelist.ModStartup;
import ECDuelist.Utils.Constants;
import ECDuelist.Utils.Path;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.Kunai;
import com.megacrit.cardcrawl.screens.CharSelectInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Inigo extends
		  CustomPlayer {

	public static class Enums {
		@SpireEnum
		public static AbstractPlayer.PlayerClass ECDuelistPlayerClass;
		@SpireEnum(name = "ECDuelist_Color") // These two HAVE to have the same absolutely identical name.
		public static AbstractCard.CardColor CardColor;
		@SpireEnum(name = "ECDuelist_Color")
		public static CardLibrary.LibraryType LibraryColor;
	}

	public static final String CharacterName = "Inigo";
	private static CharacterStrings localization;

	private Settings settings;
	private ModStartup.ColorSettings colorSettings;

	public Inigo(ModStartup.ColorSettings colorSettings) {
		this(loadSettings(), colorSettings);
	}

	private Inigo(Settings settings, ModStartup.ColorSettings colorSettings) {
		super("ECDuelist", Enums.ECDuelistPlayerClass, settings.orbs, settings.orbVfx,
				  new SpriterAnimation(settings.animation));
		this.settings = settings;
		this.colorSettings = colorSettings;

		// Text bubble location
		dialogX = (drawX + 0.0F * com.megacrit.cardcrawl.core.Settings.scale);
		dialogY = (drawY + 220.0F * com.megacrit.cardcrawl.core.Settings.scale);
	}

	public void postConstructorSetup() {
		initializeClass(null,
				  settings.campfire1,
				  settings.campfire2,
				  settings.corpse,
				  getLoadout(),
				  settings.hitBoxX,
				  settings.hitBoxY,
				  settings.hitBoxWidth,
				  settings.hitBoxHeight,
				  new EnergyManager(settings.energyPerTurn));

		loadAnimation(
				  settings.skeletonAtlas,
				  settings.skeletonJson,
				  settings.skeletonScale);
		AnimationState.TrackEntry e = state.setAnimation(0, "animation", true);
		e.setTime(e.getEndTime() * MathUtils.random());
	}

	public static void initializeStatics() {
		localization = CardCrawlGame.languagePack.getCharacterString(Constants.ModPrefix + CharacterName);
	}

	private static Settings loadSettings() {
		Settings s;
		try (InputStream in = Inigo.class.getResourceAsStream(Path.SettingsPath + "character/" + CharacterName + ".json")) {
			Gson reader = new Gson();
			s = reader.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), Settings.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		for (int i = 0; i < s.orbs.length; i++) {
			s.orbs[i] = Path.ImagesPath + s.orbs[i];
		}
		s.orbVfx = Path.ImagesPath + s.orbVfx;
		s.animation = Path.ImagesPath + s.animation;
		s.portrait = Path.ImagesPath + s.portrait;
		s.skeletonAtlas = Path.ImagesPath + s.skeletonAtlas;
		s.skeletonJson = Path.ImagesPath + s.skeletonJson;
		s.mainMenuButton = Path.ImagesPath + s.mainMenuButton;
		s.campfire1 = Path.ImagesPath + s.campfire1;
		s.campfire2 = Path.ImagesPath + s.campfire2;
		s.corpse = Path.ImagesPath + s.corpse;

		return s;
	}

	public String getButtonArtPath() {
		return settings.mainMenuButton;
	}

	public String getPortraitPath() {
		return settings.portrait;
	}

	@Override
	public ArrayList<String> getStartingDeck() {
		ArrayList<String> cards = new ArrayList<String>();
		for (String cardId : settings.startingHand) {
			cards.add(Constants.ModPrefix + cardId);
		}
		return cards;
	}

	@Override
	public ArrayList<String> getStartingRelics() {
		ArrayList<String> relics = new ArrayList<String>();
		relics.add(Kunai.ID);
		return relics;
	}

	@Override
	public CharSelectInfo getLoadout() {
		return new CharSelectInfo(
				  localization.NAMES[0],
				  localization.TEXT[0],
				  settings.startingHP,
				  settings.maxHP,
				  0,
				  settings.startingGold,
				  settings.cardDraw,
				  this,
				  getStartingRelics(),
				  getStartingDeck(),
				  false);
	}

	// The class name as it appears next to your player name in-game
	@Override
	public String getTitle(PlayerClass playerClass) {
		return localization.NAMES[1];
	}

	@Override
	public AbstractCard.CardColor getCardColor() {
		return Enums.CardColor;
	}

	@Override
	public Color getCardRenderColor() {
		return colorSettings.cardRenderColor;
	}

	//Which card should be obtainable from the Match and Keep event?
	@Override
	public AbstractCard getStartCardForEvent() {
		// TODO How should this be handled?
		return new Card(settings.startingHand[0]);
	}

	@Override
	public Color getCardTrailColor() {
		return colorSettings.cardTrailColor;
	}

	// Should return how much HP your maximum HP reduces by when starting a run at
	// Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
	@Override
	public int getAscensionMaxHPLoss() {
		return 0;
	}

	// Should return a BitmapFont object that you can use to customize how your
	// energy is displayed from within the energy orb.
	@Override
	public BitmapFont getEnergyNumFont() {
		return FontHelper.energyNumFontRed;
	}

	// Effects to do during character select
	@Override
	public void doCharSelectScreenSelectEffect() {
		CardCrawlGame.sound.playA("ATTACK_DAGGER_1", 1.25f); // Sound Effect
		CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false); // Screen Effect
	}

	// character Select on-button-press sound effect
	@Override
	public String getCustomModeCharacterButtonSoundKey() {
		return "ATTACK_DAGGER_1";
	}

	// Should return class name as it appears in run history screen.
	@Override
	public String getLocalizedCharacterName() {
		return localization.NAMES[0];
	}

	@Override
	public AbstractPlayer newInstance() {
		Inigo i = new Inigo(colorSettings);
		i.postConstructorSetup();
		return i;
	}

	// Should return a string containing what text is shown when your character is
	// about to attack the heart. For example, the defect is "NL You charge your
	// core to its maximum..."
	@Override
	public String getSpireHeartText() {
		return localization.TEXT[1];
	}

	@Override
	public Color getSlashAttackColor() {
		return colorSettings.slashAttackColor;
	}

	@Override
	public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
		// TODO Add at least one effect or the game crashes when approaching the heart.
		return new AbstractGameAction.AttackEffect[0];
	}

	// The vampire events refer to the base game characters as "brother", "sister",
	// and "broken one" respectively.This method should return a String containing
	// the full text that will be displayed as the first screen of the vampires event.
	@Override
	public String getVampireText() {
		return localization.TEXT[2];
	}

	private class Settings {
		public String[] orbs;
		public String orbVfx;
		public String animation;
		public String portrait;
		public String skeletonAtlas;
		public String skeletonJson;
		public float skeletonScale;
		public String mainMenuButton;
		public String campfire1;
		public String campfire2;
		public String corpse;
		public float hitBoxX;
		public float hitBoxY;
		public float hitBoxWidth;
		public float hitBoxHeight;
		public int energyPerTurn;
		public int startingHP;
		public int maxHP;
		public int startingGold;
		public int cardDraw;
		public String[] startingHand;
	}
}
