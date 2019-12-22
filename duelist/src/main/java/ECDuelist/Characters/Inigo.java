package ECDuelist.Characters;

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
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
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

	public Inigo() {
		this(loadSettings());
	}

	private Inigo(Settings settings) {

		super("ECDuelist", Enums.PlayerClass, settings.orbs, settings.orbVfx,
				  new SpriterAnimation(settings.animation));

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
	}

	private static Settings loadSettings() {
		try (InputStream in = CardLibrary.class.getResourceAsStream("settings/character/Inigo.json")) {
			Gson reader = new Gson();
			return reader.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), Settings.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ArrayList<String> getStartingDeck() {
		return null;
	}

	@Override
	public ArrayList<String> getStartingRelics() {
		return null;
	}

	@Override
	public CharSelectInfo getLoadout() {
		return null;
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
	}
}
