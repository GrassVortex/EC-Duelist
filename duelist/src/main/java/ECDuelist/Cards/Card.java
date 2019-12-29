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
		// TODO handle settings controlled upgrades somehow
	}

	@Override
	public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
		ArrayList<AbstractGameAction> actionsToAdd = new ArrayList<AbstractGameAction>();
		for (ActionBase ca : cardActions) {
			Collections.addAll(actionsToAdd, ca.createActions(abstractPlayer, abstractMonster, this));
		}

		for (AbstractGameAction aga : actionsToAdd){
			AbstractDungeon.actionManager.addToBottom(aga);
		}
	}

}
