package ECDuelist.Cards.Actions;

import ECDuelist.Cards.Card;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class UpgradeCost extends
		  ActionBase {
	private Settings settings;

	private UpgradeCost(Settings settings) {
		this.settings = settings;
	}

	public static void registerTo(ActionLibrary actionLibrary) {
		actionLibrary.register(UpgradeCost.class.getSimpleName(), new Loader(), new Factory());
	}

	@Override
	public void initialize(Card card) {

	}

	@Override
	public AbstractGameAction[] createActions(AbstractPlayer player, AbstractMonster monster, Card card) {
		AbstractGameAction[] actions = new AbstractGameAction[0];
		return actions;
	}

	@Override
	public void upgradeCard(Card card) {
		card.action_upgradeBaseCost(settings.newCost);
	}

	private static class Factory implements
			  ActionLibrary.IActionFactory {
		@Override
		public ActionBase createAction(ActionSettings settings) {
			Settings mySettings = (Settings) settings;
			return new UpgradeCost(mySettings);
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
		public int newCost;

		public Settings() {
			super(UpgradeCost.class.getSimpleName());
		}
	}
}
