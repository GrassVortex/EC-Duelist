package ECDuelist.Cards.Actions;

import ECDuelist.Cards.Card;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class ActionBase {
	public abstract void initialize(Card card);

	public abstract AbstractGameAction[] createActions(AbstractPlayer player, AbstractMonster monster, Card card);
}
