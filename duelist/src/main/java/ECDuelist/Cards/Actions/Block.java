package ECDuelist.Cards.Actions;

import ECDuelist.Cards.CardSettings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Block {
	public static void registerTo(ActionLibrary actionLibrary) {
		actionLibrary.register(Block.class.getSimpleName(), new Loader());
	}

	private static class Loader implements
			  ActionLibrary.ISettingsLoader {

		@Override
		public CardSettings.ActionSettingBase parseAction(JsonObject json) {
			Gson reader = new Gson();
			Settings.RawSettings rawSettings = reader.fromJson(json, Settings.RawSettings.class);

			Settings settings = new Settings();
			settings.block = Integer.parseInt(rawSettings.block);

			return settings;
		}
	}

	public static class Settings extends CardSettings.ActionSettingBase {
		public int block;

		private RawSettings rawSettings;

		public static class RawSettings {
			public String block;
		}
	}
}
