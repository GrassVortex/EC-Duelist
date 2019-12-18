package ECDuelist.Cards.Actions;

import ECDuelist.Cards.CardSettings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Block extends
		  ActionBase {

	public static void registerTo(ActionLibrary actionLibrary) {
		actionLibrary.register(Block.class.getSimpleName(), new Loader());
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

		public static class RawSettings {
			public String block;
		}
	}
}
