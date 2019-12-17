package ECDuelist.Cards.Actions;

import ECDuelist.Cards.CardSettings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Damage {
	public static void registerTo(ActionLibrary actionLibrary) {
		actionLibrary.register(Damage.class.getSimpleName(), new Loader());
	}


	private static class Loader implements
			  ActionLibrary.ISettingsLoader {

		@Override
		public CardSettings.ActionBase parseAction(JsonObject json) {
			Gson reader = new Gson();
			Settings.RawSettings rawSettings = reader.fromJson(json, Settings.RawSettings.class);

			Settings settings = new Settings();
			settings.damage = Integer.parseInt(rawSettings.damage);

			return settings;
		}
	}

	public static class Settings extends CardSettings.ActionBase {
		public int damage;


		private RawSettings rawSettings;

		public static class RawSettings {
			public String damage;
		}
	}
}
