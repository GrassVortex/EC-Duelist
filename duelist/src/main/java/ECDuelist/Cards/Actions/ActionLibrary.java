package ECDuelist.Cards.Actions;

import ECDuelist.Cards.CardSettings;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;

public class ActionLibrary {

	private HashMap<String, ISettingsLoader> loaders;

	public ActionLibrary() {
		loaders = new HashMap<String, ISettingsLoader>();
	}

	public void registerActions() {
		Damage.registerTo(this);
		Block.registerTo(this);
	}

	public void register(String typeName, ISettingsLoader loader) {
		loaders.put(typeName, loader);
	}

	public CardSettings.ActionBase parseAction(JsonObject json) {
		JsonPrimitive typeMember = json.getAsJsonPrimitive("type");
		String actionType = typeMember.getAsString();

		ISettingsLoader loader = loaders.get(actionType);
		return loader.parseAction(json);
	}

	public interface ISettingsLoader {
		CardSettings.ActionBase parseAction(JsonObject json);
	}
}
