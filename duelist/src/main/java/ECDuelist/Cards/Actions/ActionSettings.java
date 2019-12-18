package ECDuelist.Cards.Actions;

import ECDuelist.Settings.CardLibrary;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class ActionSettings {
	public String type;


	protected static <T extends IRawSettings> T loadRawSettings(String actionId, IMerger<T> merger, Class<T> type) {
		String settingsFileName = "/settings/actions/" + actionId + ".json";

		T rawSettings;

		try (InputStream in = CardLibrary.class.getResourceAsStream(settingsFileName)) {
			Gson reader = new Gson();
			rawSettings = reader.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), type);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		IRawSettings currentSettings;

		String[] bases = rawSettings.getBases();
		if (bases != null) {
			currentSettings = rawSettings.clone();
			// we start at the last (most base) base setting and work our way forward, that way we make sure that the later values
			// are not overridden by earlier "more base" values.
			for (int i = bases.length - 1; i >= 0; i--) {
				IRawSettings baseSettings = loadRawSettings(bases[i], merger, type);
				currentSettings = merger.mergeSettings(currentSettings, baseSettings);
			}
		} else {
			currentSettings = rawSettings;
		}

		// This cast should not be able to fail, but the compiler can't verify that
		@SuppressWarnings("unchecked")
		T currentSettings1 = (T) currentSettings;
		return currentSettings1;
	}

	protected interface IRawSettings {
		IRawSettings clone();

		String[] getBases();
	}

	protected interface IMerger<T extends IRawSettings> {
		T mergeSettings(IRawSettings a, IRawSettings b);
	}
}
