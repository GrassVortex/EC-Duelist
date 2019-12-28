package ECDuelist.Cards.Actions;

import ECDuelist.Cards.Card;
import ECDuelist.Cards.CardSettings;
import ECDuelist.Utils.SettingsHelper;
import ECDuelist.Utils.Text;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Damage extends
		  ActionBase {
	private Settings settings;

	private Damage(Settings settings) {
		this.settings = settings;
	}

	public static void registerTo(ActionLibrary actionLibrary) {
		actionLibrary.register(Damage.class.getSimpleName(), new Loader(), new Factory());
	}

	@Override
	public void initialize(Card card) {
		card.baseDamage = settings.damage;
		card.damageTypeForTurn = settings.damageType;
	}

	@Override
	public AbstractGameAction[] createActions(AbstractPlayer player, AbstractMonster monster, Card card) {
		AbstractGameAction[] actions = new AbstractGameAction[1];
		actions[0] = new DamageAction(monster, new DamageInfo(player, settings.damage, settings.damageType),
				  settings.attackEffect);

		return actions;
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
			settings.attackEffect = AbstractGameAction.AttackEffect.valueOf(rawSettings.attackEffect);

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
			result.attackEffect = SettingsHelper.coalesce(a.attackEffect, b.attackEffect);

			return result;
		}
	}

	public static class Settings extends
			  ActionSettings {

		public int damage;
		public DamageInfo.DamageType damageType;
		public AbstractGameAction.AttackEffect attackEffect;

		private RawSettings rawSettings;

		public Settings() {
			super(Damage.class.getSimpleName());
		}

		public static class RawSettings implements
				  ActionSettings.IRawSettings {

			public String[] bases;
			public String damage;
			public String damageType;
			public String attackEffect;

			@Override
			public IRawSettings clone() {
				RawSettings settings = new RawSettings();
				settings.bases = bases.clone();
				settings.damage = damage;
				settings.damageType = damageType;
				settings.attackEffect = attackEffect;
				return settings;
			}

			@Override
			public String[] getBases() {
				return bases;
			}
		}
	}
}
