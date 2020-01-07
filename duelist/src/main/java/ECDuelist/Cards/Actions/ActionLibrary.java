package ECDuelist.Cards.Actions;

import ECDuelist.Cards.CardSettings;
import ECDuelist.Settings.CardLibrary;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;

public class ActionLibrary {

	private HashMap<String, ISettingsLoader> loaders;
	// Needs to be static so that the default constructor from Card sub-classes can access these factories.
	private static HashMap<String, IActionFactory> factories;

	public ActionLibrary() {
		if (factories != null) {
			throw new IllegalStateException("Can't create more than one " + ActionLibrary.class.getName());
		}

		loaders = new HashMap<String, ISettingsLoader>();
		factories = new HashMap<String, IActionFactory>();
	}

	public void registerActions() {
		Damage.registerTo(this);
		Block.registerTo(this);
		UpgradeCost.registerTo(this);
	}

	public static ActionBase createAction(ActionSettings settings) {
		// This method assumes that all types are already registered
		IActionFactory factory = factories.get(settings.typeName);
		return factory.createAction(settings);
	}

	public void register(String typeName, ISettingsLoader loader, IActionFactory factory) {
		loaders.put(typeName, loader);
		factories.put(typeName, factory);
	}

	public ActionSettings parseAction(JsonObject json) {
		// This method assumes that all types are already registered

		JsonPrimitive typeMember = json.getAsJsonPrimitive("type");
		String actionType = typeMember.getAsString();

		ISettingsLoader loader = loaders.get(actionType);
		return loader.parseAction(json);
	}

	public interface ISettingsLoader {
		ActionSettings parseAction(JsonObject json);
	}

	public interface IActionFactory {
		ActionBase createAction(ActionSettings settings);
	}
}
