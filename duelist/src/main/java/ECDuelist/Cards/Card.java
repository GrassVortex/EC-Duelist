package ECDuelist.Cards;

import ECDuelist.Settings.CardLibrary;
import basemod.abstracts.CustomCard;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Card extends
		  CustomCard {

	private CardSettings settings;

	public Card(CardSettings settings) {
		super(settings.id, settings.name, settings.image, settings.cost, settings.description, settings.type, settings.color, settings.rarity, settings.target);
		this.settings = settings;
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
