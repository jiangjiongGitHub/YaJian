package love.image.util;

public class RanderNumberUtils {

	public static String getCheckCode(int size) {
		String checkCode = "";
		for (int i = 0; i < size; i++) {
			String code = (int) Math.floor(Math.random() * 10) + "";
			checkCode += code;
		}
		return checkCode;
	}
}
