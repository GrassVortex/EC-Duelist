package ECDuelist.Cards;

import ECDuelist.Cards.Actions.ActionBase;
import ECDuelist.Cards.Actions.ActionLibrary;
import ECDuelist.Cards.Actions.ActionSettings;
import ECDuelist.Settings.CardLibrary;
import ECDuelist.Utils.Text;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;
import java.util.Collections;


public class Card extends
		  CustomCard {
	private CardSettings settings;
	private ActionBase[] actions;

	protected Card(String cardId) {
		this(CardLibrary.getSettings(cardId));
	}

	private Card(CardSettings settings) {
		super(settings.id, settings.name, settings.image, settings.cost, settings.description, settings.type, settings.color, settings.rarity, settings.target);
		this.settings = settings;

		actions = new ActionBase[settings.actions.length];
		for (int i = 0; i < actions.length; i++) {
			ActionSettings actionSetting = settings.actions[i];
			ActionBase action = ActionLibrary.createAction(actionSetting);
			action.initialize(this);
			actions[i] = action;
		}

		Collections.addAll(tags, settings.stsTags);
		Collections.addAll(tags, settings.modTags);
	}

	public String getPrefixedId() {
		return settings.id;
	}

	public boolean isUnlocked() {
		return true;
	}


	@Override
	public void upgrade() {

	}

	@Override
	public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {

	}

}
