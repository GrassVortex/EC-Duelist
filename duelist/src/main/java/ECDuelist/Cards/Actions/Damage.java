package ECDuelist.Cards.Actions;

import ECDuelist.Cards.CardSettings;
import ECDuelist.Utils.Text;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.megacrit.cardcrawl.cards.DamageInfo;

public class Damage extends
		  ActionBase {

	public static void registerTo(ActionLibrary actionLibrary) {
		actionLibrary.register(Damage.class.getSimpleName(), new Loader());
	}


	private static class Loader implements
			  ActionLibrary.ISettingsLoader {
		@Override
		public ActionSettings parseAction(JsonObject json) {
			Merger merger = new Merger();
			Settings.RawSettings rawSettings = ActionSettings.loadRawSettings(json, merger, Settings.RawSettings.class);

			Settings settings = new Settings();
			settings.damage = Integer.parseInt(rawSettings.damage);

			return settings;
		}
	}

	private static class Merger implements
			  ActionSettings.IMerger<Settings.RawSettings> {
		@Override
		public Settings.RawSettings mergeSettings(Settings.RawSettings a, Settings.RawSettings b) {
			return null;
		}
	}

	public static class Settings extends
			  ActionSettings {

		public int damage;
		public DamageInfo.DamageType damageType;

		private RawSettings rawSettings;

		public static class RawSettings implements
				  ActionSettings.IRawSettings {

			public String[] bases;
			public String damage;
			public String damageType;

			@Override
			public IRawSettings clone() {
				RawSettings settings = new RawSettings();
				settings.bases = bases.clone();
				settings.damage = damage;
				settings.damageType = damageType;
				return settings;
			}

			@Override
			public String[] getBases() {
				return bases;
			}
		}
	}
}
