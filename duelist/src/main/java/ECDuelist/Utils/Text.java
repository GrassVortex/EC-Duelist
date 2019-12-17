package ECDuelist.Utils;

public class Text {

	public static void print(String format, Object ... args) {
		System.out.printf(format, args);
	}

	public static void println(String format, Object ... args) {
		System.out.printf(format, args);
		System.out.println();
	}

	public static String format(String[] list) {
		return format(list, ", ");
	}

	public static String format(String[] list, String separator) {
		String result = "";
		if (list == null || list.length == 0) {
			return result;
		}

		result = list[0];
		// Note that the loop starts at 1, not zero
		for (int i = 1; i < list.length; i++) {
			result += separator + list[i];
		}
		return result;
	}
}
