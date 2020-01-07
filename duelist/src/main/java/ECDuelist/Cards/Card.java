package ECDuelist.Cards;

import ECDuelist.Cards.Actions.ActionBase;
import ECDuelist.Cards.Actions.ActionLibrary;
import ECDuelist.Cards.Actions.ActionSettings;
import ECDuelist.Settings.CardLibrary;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Collections;


public class Card extends
		  CustomCard {
	private CardSettings settings;
	private ActionBase[] cardActions;

	public Card(String cardId) {
		this(CardLibrary.getSettings(cardId));
	}

	private Card(CardSettings settings) {
		super(settings.id, settings.name, settings.image, settings.cost, settings.description, settings.type, settings.color, settings.rarity, settings.target);
		this.settings = settings;

		cardActions = new ActionBase[settings.actions.length];
		for (int i = 0; i < cardActions.length; i++) {
			ActionSettings actionSetting = settings.actions[i];
			ActionBase action = ActionLibrary.createAction(actionSetting);
			action.initialize(this);
			cardActions[i] = action;
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
	public AbstractCard makeCopy() {
		return new Card(settings);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();

			for (ActionBase ca : cardActions) {
				ca.upgradeCard(this);
			}

			upgraded = true;
			initializeDescription();
		}
	}

	@Override
	public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
		ArrayList<AbstractGameAction> actionsToAdd = new ArrayList<AbstractGameAction>();
		for (ActionBase ca : cardActions) {
			Collections.addAll(actionsToAdd, ca.createActions(abstractPlayer, abstractMonster, this));
		}

		// Potential for some action filtering here, like make sure conditions are fulfilled

		for (AbstractGameAction aga : actionsToAdd){
			AbstractDungeon.actionManager.addToBottom(aga);
		}
	}

	public void action_upgradeDamage(int amount){
		upgradeDamage(amount);
	}

	public void action_upgradeBlock(int amount){
		upgradeBlock(amount);
	}

	public void action_upgradeMagicNumber(int amount){
		upgradeMagicNumber(amount);
	}

	public void action_upgradeBaseCost(int newBaseCost){
		upgradeBaseCost(newBaseCost);
	}

}
