package ECDuelist.Utils;

import ECDuelist.Cards.CardSettings;
import com.google.gson.JsonElement;

public class SettingsHelper {
	public static String coalesce(String a, String b) {
		return (a != null && !a.isEmpty()) ? a : b;
	}

	public static <T> T coalesce(T a, T b) {
		return (a != null) ? a : b;
	}

	public static <T> T[] coalesce(T[] a, T[] b) {
		return (a != null && a.length != 0) ? a : b;
	}
}
