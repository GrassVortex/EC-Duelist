package ECDuelist.Cards.Actions;

import ECDuelist.Cards.CardSettings;
import ECDuelist.Utils.SettingsHelper;
import ECDuelist.Utils.Text;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.megacrit.cardcrawl.cards.DamageInfo;

public class Damage extends
		  ActionBase {
	private Settings settings;

	private Damage(Settings settings) {
		this.settings = settings;
	}

	public static void registerTo(ActionLibrary actionLibrary) {
		actionLibrary.register(Damage.class.getSimpleName(), new Loader(), new Factory());
	}


	private static class Factory implements
			  ActionLibrary.IActionFactory {
		@Override
		public ActionBase createAction(ActionSettings settings) {
			Settings mySettings = (Settings) settings;
			return new Damage(mySettings);
		}
	}

	private static class Loader implements
			  ActionLibrary.ISettingsLoader {
		@Override
		public ActionSettings parseAction(JsonObject json) {
			Merger merger = new Merger();
			Settings.RawSettings rawSettings = ActionSettings.loadRawSettings(json, merger, Settings.RawSettings.class);

			Settings settings = new Settings();
			settings.damage = Integer.parseInt(rawSettings.damage);
			settings.damageType = DamageInfo.DamageType.valueOf(rawSettings.damageType);

			return settings;
		}
	}

	private static class Merger implements
			  ActionSettings.IMerger<Settings.RawSettings> {
		@Override
		public Settings.RawSettings mergeSettings(Settings.RawSettings a, Settings.RawSettings b) {
			Settings.RawSettings result = new Settings.RawSettings();
			result.damage = SettingsHelper.coalesce(a.damage, b.damage);
			result.damageType = SettingsHelper.coalesce(a.damageType, b.damageType);

			return result;
		}
	}

	public static class Settings extends
			  ActionSettings {

		public int damage;
		public DamageInfo.DamageType damageType;

		private RawSettings rawSettings;

		public Settings() {
			super(Damage.class.getSimpleName());
		}

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
