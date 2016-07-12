package love.image.test;

public class CharLearning {
	public static void main(String[] args) {
		String str = "_abcdefghijklmnopqrstuvwxyz_ABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789";
		for (int i = 0; i < 15; i++) {

			str = main_test(str, false);
			System.out.println(str);

		}
	}

	static public String main_test(String str, boolean flag) {
		// str="_abcdefghijklmnopqrstuvwxyz_ABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789";
		if (flag)
			System.out.println((int) ('m'));
		String rt = "";
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == 95) {
				if (flag)
					System.out.println();
			} else if (c >= 97 && c <= 109) {
				c = (char) (c + 1);
			} else if (c >= 110 && c <= 122) {
				c = (char) (c - 1);
			} else if (c >= 65 && c <= 77) {
				c = (char) (c + 1);
			} else if (c >= 78 && c <= 90) {
				c = (char) (c - 1);
			} else if (c >= 48 && c <= 52) {
				c = (char) (c + 1);
			} else if (c >= 53 && c <= 57) {
				c = (char) (c - 1);
			} else {
				if (flag)
					System.out.print("----");
			}
			if (flag)
				System.out.print(c);
			rt += c;
		}
		return rt;
	}
}
