package ECDuelist.Cards.Actions;

import ECDuelist.Settings.CardLibrary;
import ECDuelist.Utils.Path;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class ActionSettings {
	public final String typeName;

	protected ActionSettings(String name) {
		this.typeName = name;
	}

	protected static <T extends IRawSettings> T loadRawSettings(JsonObject json, IMerger<T> merger, Class<T> type) {
		T rawSettings;
		Gson reader = new Gson();
		rawSettings = reader.fromJson(json, type);
		return loadRawSettings(rawSettings, merger, type);
	}

	protected static <T extends IRawSettings> T loadRawSettings(String actionId, IMerger<T> merger, Class<T> type) {
		String settingsFileName = Path.SettingsPath + "actions/" + actionId + ".json";
		T rawSettings;
		try (InputStream in = CardLibrary.class.getResourceAsStream(settingsFileName)) {
			Gson reader = new Gson();
			rawSettings = reader.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), type);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return loadRawSettings(rawSettings, merger, type);
	}

	protected static <T extends IRawSettings> T loadRawSettings(T rawSettings, IMerger<T> merger, Class<T> type) {
		T currentSettings;

		String[] bases = rawSettings.getBases();
		if (bases != null) {
			// This cast should not be able to fail, but the compiler can't verify that
			@SuppressWarnings("unchecked")
			T temp = (T) rawSettings.clone();
			currentSettings = temp;
			// we start at the last (most base) base setting and work our way forward, that way we make sure that the later values
			// are not overridden by earlier "more base" values.
			for (int i = bases.length - 1; i >= 0; i--) {
				T baseSettings = loadRawSettings(bases[i], merger, type);
				currentSettings = merger.mergeSettings(currentSettings, baseSettings);
			}
		} else {
			currentSettings = rawSettings;
		}
		return currentSettings;
	}

	protected interface IRawSettings {
		IRawSettings clone();

		String[] getBases();
	}

	protected interface IMerger<T extends IRawSettings> {
		T mergeSettings(T a, T b);
	}
}
