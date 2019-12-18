package ECDuelist.Cards.Actions;

import ECDuelist.Cards.Card;
import ECDuelist.Cards.CardSettings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Block extends
		  ActionBase {

	private Settings settings;

	private Block(Settings settings) {
		this.settings = settings;
	}

	public static void registerTo(ActionLibrary actionLibrary) {
		actionLibrary.register(Block.class.getSimpleName(), new Loader(), new Factory());
	}

	@Override
	public void initialize(Card card) {

	}

	private static class Factory implements
			  ActionLibrary.IActionFactory {
		@Override
		public ActionBase createAction(ActionSettings settings) {
			Settings mySettings = (Settings) settings;
			return new Block(mySettings);
		}
	}

	private static class Loader implements
			  ActionLibrary.ISettingsLoader {

		@Override
		public ActionSettings parseAction(JsonObject json) {
			Gson reader = new Gson();
			Settings.RawSettings rawSettings = reader.fromJson(json, Settings.RawSettings.class);

			Settings settings = new Settings();
			settings.block = Integer.parseInt(rawSettings.block);

			return settings;
		}
	}

	public static class Settings extends ActionSettings {
		public int block;

		private RawSettings rawSettings;

		public Settings() {
			super(Block.class.getSimpleName());
		}

		public static class RawSettings {
			public String block;
		}
	}
}
