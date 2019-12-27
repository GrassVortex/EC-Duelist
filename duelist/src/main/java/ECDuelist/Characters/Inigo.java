package ECDuelist.Characters;

import ECDuelist.Utils.Path;
import ECDuelist.Utils.Text;
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
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CharacterStrings;
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
		public static AbstractPlayer.PlayerClass PlayerClass;
		@SpireEnum(name = "ECDuelist_Color") // These two HAVE to have the same absolutely identical name.
		public static AbstractCard.CardColor CardColor;
		@SpireEnum(name = "ECDuelist_Color")
		public static CardLibrary.LibraryType LibraryColor;
	}

	private Settings settings;
	private String modPrefix;

	private CharacterStrings localization;

	public Inigo(String modPrefix) {
		this(loadSettings(), modPrefix);
	}

	private Inigo(Settings settings, String modPrefix) {
		super("ECDuelist", Enums.PlayerClass, settings.orbs, settings.orbVfx,
				  new SpriterAnimation(settings.animation));
		this.settings = settings;
		this.modPrefix = modPrefix;

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

		// Text bubble location
		dialogX = (drawX + 0.0F * com.megacrit.cardcrawl.core.Settings.scale);
		dialogY = (drawY + 220.0F * com.megacrit.cardcrawl.core.Settings.scale);

		localization = CardCrawlGame.languagePack.getCharacterString(modPrefix + "Inigo");
	}

	private static Settings loadSettings() {
		Settings s;
		try (InputStream in = Inigo.class.getResourceAsStream(Path.SettingsPath + "character/Inigo.json")) {
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
		cards.add(modPrefix + "Strike");
		return cards;
	}

	@Override
	public ArrayList<String> getStartingRelics() {
		ArrayList<String> relics = new ArrayList<String>();
		relics.add("Kunai");
		return relics;
	}

	@Override
	public CharSelectInfo getLoadout() {
		Text.println("localization %s  %s", localization, localization.NAMES[0]);

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

	@Override
	public String getTitle(PlayerClass playerClass) {
		return null;
	}

	@Override
	public AbstractCard.CardColor getCardColor() {
		return null;
	}

	@Override
	public Color getCardRenderColor() {
		return null;
	}

	@Override
	public AbstractCard getStartCardForEvent() {
		return null;
	}

	@Override
	public Color getCardTrailColor() {
		return null;
	}

	@Override
	public int getAscensionMaxHPLoss() {
		return 0;
	}

	@Override
	public BitmapFont getEnergyNumFont() {
		return null;
	}

	@Override
	public void doCharSelectScreenSelectEffect() {

	}

	@Override
	public String getCustomModeCharacterButtonSoundKey() {
		return null;
	}

	@Override
	public String getLocalizedCharacterName() {
		return null;
	}

	@Override
	public AbstractPlayer newInstance() {
		return null;
	}

	@Override
	public String getSpireHeartText() {
		return null;
	}

	@Override
	public Color getSlashAttackColor() {
		return null;
	}

	@Override
	public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
		return new AbstractGameAction.AttackEffect[0];
	}

	@Override
	public String getVampireText() {
		return null;
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
	}
}
