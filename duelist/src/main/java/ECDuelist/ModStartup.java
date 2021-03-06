package ECDuelist;

import ECDuelist.Characters.Inigo;
import ECDuelist.Settings.CardLibrary;
import ECDuelist.Utils.Path;
import ECDuelist.Utils.Text;
import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


@SpireInitializer
public class ModStartup implements
		  EditCardsSubscriber,
		  EditStringsSubscriber,
		  EditCharactersSubscriber {

	private ModSettings settings;
	private ColorSettings colorSettings;
	private CardLibrary library;

	public ModStartup() {
		try (InputStream in = ModStartup.class.getResourceAsStream(Path.SettingsPath + "modBaseSettings.json")) {
			Gson reader = new Gson();
			settings = reader.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), ModSettings.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		library = new CardLibrary();

		BaseMod.subscribe(this);

		addCardColors();
	}

	private void addCardColors() {
		Text.println(new Object() {
		}.getClass().getEnclosingMethod().getName());

		ColorSettings cs;
		try (InputStream in = ModStartup.class.getResourceAsStream(Path.SettingsPath + "character/color.json")) {
			Gson reader = new Gson();
			cs = reader.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), ColorSettings.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Add the correct prefix to make the image paths work
		cs.attackBg = Path.ImagesPath + cs.attackBg;
		cs.skillBg = Path.ImagesPath + cs.skillBg;
		cs.powerBg = Path.ImagesPath + cs.powerBg;
		cs.energyOrb = Path.ImagesPath + cs.energyOrb;
		cs.attackBgPortrait = Path.ImagesPath + cs.attackBgPortrait;
		cs.skillBgPortrait = Path.ImagesPath + cs.skillBgPortrait;
		cs.powerBgPortrait = Path.ImagesPath + cs.powerBgPortrait;
		cs.energyOrbPortrait = Path.ImagesPath + cs.energyOrbPortrait;
		cs.cardEnergyOrb = Path.ImagesPath + cs.cardEnergyOrb;

		BaseMod.addColor(Inigo.Enums.CardColor, cs.bgColor, cs.backColor, cs.frameColor, cs.frameOutlineColor,
				  cs.descBoxColor, cs.trailVfxColor,
				  cs.glowColor, cs.attackBg, cs.skillBg, cs.powerBg, cs.energyOrb, cs.attackBgPortrait,
				  cs.skillBgPortrait, cs.powerBgPortrait, cs.energyOrbPortrait, cs.cardEnergyOrb);
		colorSettings = cs;
	}

	@SuppressWarnings("unused")
	// This class is indirectly used by ModTheSpire. But the tools can't verify it, therefore the warning
	public static void initialize() {
		Text.println(new Object() {
		}.getClass().getEnclosingMethod().getName());
		// Create an instance of out main mod class. Do all initialization in the constructor and related methods, not in this method.
		ModStartup start = new ModStartup();
	}


	@Override
	public void receiveEditCards() {
		Text.println(new Object() {
		}.getClass().getEnclosingMethod().getName());

		library.createCards();

	}

	@Override
	public void receiveEditStrings() {
		Text.println(new Object() {
		}.getClass().getEnclosingMethod().getName());

		String localizationBase = Path.ResourcesBasePathNotAbsolute + "localization/" + settings.language + "/ECDuelist-";

		BaseMod.loadCustomStringsFile(CardStrings.class, localizationBase + "cardStrings.json");
		BaseMod.loadCustomStringsFile(CharacterStrings.class, localizationBase + "characterStrings.json");
	}

	@Override
	public void receiveEditCharacters() {

		Inigo.initializeStatics();

		Inigo character = new Inigo(colorSettings);
		character.postConstructorSetup();
		BaseMod.addCharacter(character, character.getButtonArtPath(), character.getPortraitPath(), Inigo.Enums.ECDuelistPlayerClass);
	}

	private static class ModSettings {
		public String language;
	}

	public static class ColorSettings {
		public Color bgColor;
		public Color backColor;
		public Color frameColor;
		public Color frameOutlineColor;
		public Color descBoxColor;
		public Color trailVfxColor;
		public Color glowColor;
		public String attackBg;
		public String skillBg;
		public String powerBg;
		public String energyOrb;
		public String attackBgPortrait;
		public String skillBgPortrait;
		public String powerBgPortrait;
		public String energyOrbPortrait;
		public String cardEnergyOrb;

		public Color cardRenderColor;
		public Color cardTrailColor;
		public Color slashAttackColor;
	}

	;
}