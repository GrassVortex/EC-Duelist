package ECDuelist.Cards.Actions;

import ECDuelist.Cards.CardSettings;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;

public class ActionLibrary {

	private HashMap<String, ISettingsLoader> loaders;
	private HashMap<String, IActionFactory> factories;

	public ActionLibrary() {
		loaders = new HashMap<String, ISettingsLoader>();
		factories = new HashMap<String, IActionFactory>();
	}

	public void registerActions() {
		Damage.registerTo(this);
		Block.registerTo(this);
	}

	public ActionBase createAction(ActionSettings settings) {
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
