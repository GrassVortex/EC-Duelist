package ECDuelist.Cards.Actions;

import ECDuelist.Cards.Card;
import ECDuelist.Cards.CardSettings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

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
		card.baseBlock = settings.block;
	}

	@Override
	public AbstractGameAction[] createActions(AbstractPlayer player, AbstractMonster monster, Card card) {
		AbstractGameAction[] actions = new AbstractGameAction[1];
		actions[0] = new GainBlockAction(player, player, settings.block);

		return actions;
	}

	@Override
	public void upgradeCard(Card card) {
		if (settings.upgradedBlock != null) {
			card.action_upgradeBlock(settings.upgradedBlock - settings.block);
		}
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
			Settings settings = reader.fromJson(json, Settings.class);

			return settings;
		}
	}

	public static class Settings extends ActionSettings {
		public int block;
		public Integer upgradedBlock;

		public Settings() {
			super(Block.class.getSimpleName());
		}
	}
}
