package ECDuelist.Cards;

import ECDuelist.Settings.CardLibrary;
import basemod.abstracts.CustomCard;
import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;



public class Card extends
		  CustomCard {

	private CardSettings settings;

	public Card(CardSettings settings) {
		super(settings.id, settings.name, settings.image, settings.cost, settings.description, settings.type, settings.color, settings.rarity, settings.target);
		this.settings = settings;

		baseDamage = 10;
//		this.tags.add(BaseModCardTags.BASIC_STRIKE);
//		this.tags.add(CardTags.STRIKE);
//		isCostModified = false;
//		isCostModifiedForTurn = false;
//		isDamageModified = false;
//		isBlockModified = false;
//		isMagicNumberModified = false;

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
