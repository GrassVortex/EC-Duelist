package ECDuelist.Cards.Actions;

import ECDuelist.Cards.CardSettings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.megacrit.cardcrawl.cards.DamageInfo;

public class Damage {
	public static void registerTo(ActionLibrary actionLibrary) {
		actionLibrary.register(Damage.class.getSimpleName(), new Loader());
	}


	private static class Loader implements
			  ActionLibrary.ISettingsLoader {

		@Override
		public ActionSettings parseAction(JsonObject json) {
			Gson reader = new Gson();
			Settings.RawSettings rawSettings = reader.fromJson(json, Settings.RawSettings.class);

			Settings settings = new Settings();
			settings.damage = Integer.parseInt(rawSettings.damage);

			return settings;
		}
	}

	public static class Settings extends ActionSettings {
		public int damage;
		public DamageInfo.DamageType damageType;

		private RawSettings rawSettings;

		public static class RawSettings {
			public String damage;
			public String damageType;
		}
	}
}
