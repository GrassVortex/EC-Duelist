package ECDuelist;

import ECDuelist.Characters.Inigo;
import ECDuelist.Settings.CardLibrary;
import ECDuelist.Utils.Text;
import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


@SpireInitializer
public class ModStartup implements
		  EditCardsSubscriber,
		  EditStringsSubscriber,
		  EditCharactersSubscriber {

	private ModSettings settings;
	private CardLibrary library;

	public ModStartup() {


		InputStream in = CardLibrary.class.getResourceAsStream("/settings/modBaseSettings.json");
		Gson reader = new Gson();
		settings = reader.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), ModSettings.class);

		library = new CardLibrary(settings.modPrefix);

		BaseMod.subscribe(this);
	}

	@SuppressWarnings("unused")
	// This class is indirectly used by ModTheSpire. But the tools can't verify it, therefore the warning
	public static void initialize() {
		Text.println(new Object(){}.getClass().getEnclosingMethod().getName());
		// Create an instance of out main mod class. Do all initialization in the constructor and related methods, not in this method.
		ModStartup start = new ModStartup();
	}


	@Override
	public void receiveEditCards() {
		Text.println(new Object(){}.getClass().getEnclosingMethod().getName());
		library.createCards();

	}

	@Override
	public void receiveEditStrings() {
		Text.println(new Object(){}.getClass().getEnclosingMethod().getName());

		String localizationBase = String.join("/", "localization", settings.language, settings.modPrefix);

		// CardStrings
		BaseMod.loadCustomStringsFile(CardStrings.class,
				  localizationBase + "-CardStrings.json");

	}

	@Override
	public void receiveEditCharacters() {
	Inigo character = new Inigo();
		BaseMod.addCharacter(character, character.getButtonArtPath(), character.getPortraitPath(), Inigo.Enums.PlayerClass);
	}

	private class ModSettings {
		public String language;
		public String modPrefix;
	}
}