package ECDuelist;

import ECDuelist.Settings.CardLibrary;
import basemod.interfaces.EditCardsSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;


@SpireInitializer
public class ModStartup implements
		  EditCardsSubscriber {

	private CardLibrary library;

	public ModStartup() {

		System.out.println("Hello StS World!");

		library = new CardLibrary();
	}

	@SuppressWarnings("unused")
	// This class is indirectly used by ModTheSpire. But the tools can't verify it, therefore the warning
	public static void initialize() {
		// Create an instance of out main mod class. Do all initialization in the constructor and related methods, not in this method.
		ModStartup start = new ModStartup();
	}


	@Override
	public void receiveEditCards() {
		library.registerCards();

	}
}