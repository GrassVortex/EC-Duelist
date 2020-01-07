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

	@Override
	public void upgradeCard(Card card) {
		if (settings.upgradedDamage != null) {
			card.action_upgradeDamage(settings.upgradedDamage - settings.damage);
		}
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

			Gson reader = new Gson();
			Settings settings = reader.fromJson(json, Settings.class);

			return settings;
		}
	}


	public static class Settings extends
			  ActionSettings {

		public int damage;
		public Integer upgradedDamage;
		public DamageInfo.DamageType damageType;
		public AbstractGameAction.AttackEffect attackEffect;

		public Settings() {
			super(Damage.class.getSimpleName());
		}


	}
}
