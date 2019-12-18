package ECDuelist.Cards;

import ECDuelist.Cards.Actions.ActionSettings;
import ECDuelist.Settings.CardLibrary;
import ECDuelist.Utils.Text;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class Card extends
		  CustomCard {
	private CardSettings settings;


	protected Card(String cardId) {
		this(CardLibrary.getSettings(cardId));
	}

	private Card(CardSettings settings) {
		super(settings.id, settings.name, settings.image, settings.cost, settings.description, settings.type, settings.color, settings.rarity, settings.target);
		this.settings = settings;

		for (int i = 0; i < settings.actions.length; i++) {
			ActionSettings actionSetting = settings.actions[i];

		}

//		this.tags.add(BaseModCardTags.BASIC_STRIKE);
//		this.tags.add(CardTags.STRIKE);
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
