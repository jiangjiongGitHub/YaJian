package love.image.util;

public class JJ_ImageUtil {

	public static void main1(String[] args) {

		try {

			resizeImage("C:\\Users\\Administrator\\Desktop\\0.jpg",
					"C:\\Users\\Administrator\\Desktop\\w-500.jpg", "w-500");
			resizeImage("C:\\Users\\Administrator\\Desktop\\0.jpg",
					"C:\\Users\\Administrator\\Desktop\\h-500.jpg", "h-500");
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * 功能 :调整图片大小
	 * 
	 * @param srcImgPath
	 *            原图片路径
	 * @param distImgPath
	 *            转换后图片路径
	 * @param widthORheight
	 *            宽度或高度设置为需要的大小
	 */

	public static void resizeImage(String srcImgPath, String distImgPath,
			String widthORheight) throws java.io.IOException {

		java.awt.image.BufferedImage srcImgBuffer = javax.imageio.ImageIO
				.read(new java.io.File(srcImgPath));

		int width_old = srcImgBuffer.getWidth();
		int height_old = srcImgBuffer.getHeight();
		System.out.println(width_old);
		System.out.println(height_old);

		String wh = widthORheight.split("-")[0];
		int wh_size = Integer.parseInt(widthORheight.split("-")[1]);
		if ("w".equals(wh)) {
			int width = wh_size;
			int height = height_old * wh_size / width_old;

			java.awt.image.BufferedImage buffImg = new java.awt.image.BufferedImage(
					width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);

			int width_new = buffImg.getWidth();
			int height_new = buffImg.getHeight();

			System.out.println(width_new);
			System.out.println(height_new);

			buffImg.getGraphics().drawImage(
					srcImgBuffer.getScaledInstance(width, height,
							java.awt.Image.SCALE_SMOOTH), 0, 0, null);

			javax.imageio.ImageIO.write(buffImg, "JPEG", new java.io.File(
					distImgPath));
		}

		if ("h".equals(wh)) {
			int height = wh_size;
			int width = width_old * wh_size / height_old;

			java.awt.image.BufferedImage buffImg = new java.awt.image.BufferedImage(
					width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);

			int width_new = buffImg.getWidth();
			int height_new = buffImg.getHeight();

			System.out.println(width_new);
			System.out.println(height_new);

			buffImg.getGraphics().drawImage(
					srcImgBuffer.getScaledInstance(width, height,
							java.awt.Image.SCALE_SMOOTH), 0, 0, null);

			javax.imageio.ImageIO.write(buffImg, "JPEG", new java.io.File(
					distImgPath));
		}
	}

	/**
	 * 图片流处理
	 * 
	 * @param file
	 * @param widthORheight
	 * @return
	 * @throws java.io.IOException
	 */
	public static java.io.InputStream resizeImage(
			org.springframework.web.multipart.MultipartFile file,
			String widthORheight) throws java.io.IOException {

		java.io.InputStream in = file.getInputStream();
		java.awt.image.BufferedImage srcImgBuffer = javax.imageio.ImageIO
				.read(in);

		int width_old = srcImgBuffer.getWidth();
		int height_old = srcImgBuffer.getHeight();
		System.out.println(width_old);
		System.out.println(height_old);

		String wh = widthORheight.split("-")[0];
		int wh_size = Integer.parseInt(widthORheight.split("-")[1]);

		if ("w".equals(wh)) {
			int width = wh_size;
			int height = height_old * wh_size / width_old;

			java.awt.image.BufferedImage buffImg = new java.awt.image.BufferedImage(
					width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);

			int width_new = buffImg.getWidth();
			int height_new = buffImg.getHeight();

			System.out.println(width_new);
			System.out.println(height_new);

			buffImg.getGraphics().drawImage(
					srcImgBuffer.getScaledInstance(width, height,
							java.awt.Image.SCALE_SMOOTH), 0, 0, null);

			javax.imageio.ImageIO.write(buffImg, "JPEG", new java.io.File(
					"0.jpg"));

			java.awt.Image img = javax.imageio.ImageIO.read(new java.io.File(
					"0.jpg"));
			if (width_new >= height_new) {
				java.awt.image.BufferedImage buffImg_new = new java.awt.image.BufferedImage(
						width_new, width_new,
						java.awt.image.BufferedImage.TYPE_INT_RGB);

				java.awt.Graphics2D g = buffImg_new.createGraphics();
				g.setColor(java.awt.Color.white);

				g.fillRect(0, 0, width_new, width_new);
				g.drawImage(img, 0, (width_new - height_new) / 2, width_new,
						height_new, java.awt.Color.white, null);

				System.out.println("w>h");
				g.dispose();

				javax.imageio.ImageIO.write(buffImg_new, "JPEG",
						new java.io.File("1.jpg"));
			} else {
				java.awt.image.BufferedImage buffImg_new = new java.awt.image.BufferedImage(
						height_new, height_new,
						java.awt.image.BufferedImage.TYPE_INT_RGB);
				java.awt.Graphics2D g = buffImg_new.createGraphics();
				g.setColor(java.awt.Color.white);

				g.fillRect(0, 0, height_new, height_new);
				g.drawImage(img, (height_new - width_new) / 2, 0, width_new,
						height_new, java.awt.Color.white, null);

				System.out.println("w<h");
				g.dispose();

				javax.imageio.ImageIO.write(buffImg_new, "JPEG",
						new java.io.File("1.jpg"));
			}

			return new java.io.FileInputStream(new java.io.File("1.jpg"));
		}

		if ("h".equals(wh)) {
			int height = wh_size;
			int width = width_old * wh_size / height_old;

			java.awt.image.BufferedImage buffImg = new java.awt.image.BufferedImage(
					width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);

			int width_new = buffImg.getWidth();
			int height_new = buffImg.getHeight();

			System.out.println(width_new);
			System.out.println(height_new);

			buffImg.getGraphics().drawImage(
					srcImgBuffer.getScaledInstance(width, height,
							java.awt.Image.SCALE_SMOOTH), 0, 0, null);

			javax.imageio.ImageIO.write(buffImg, "JPEG", new java.io.File(
					"0.jpg"));

			return new java.io.FileInputStream(new java.io.File("0.jpg"));
		}
		return in;
	}

	public static String getSmallUUID(String fileName) {
		String str = fileName.substring(fileName.lastIndexOf("."));
		String uuid = fileName.substring(0, fileName.lastIndexOf("."));
		return uuid + "_small" + str;
	}

	public static String getUUID(String fileName) {
		String str = fileName.substring(fileName.lastIndexOf("."));
		String uuid = fileName.substring(0, fileName.lastIndexOf("."));
		return uuid.replace("_small", "") + str;
	}

	public static void main(String[] args) {
		String a = "  null null  ";
		long t = System.currentTimeMillis();
		for (int i = 0; i < 10000 * 10000; i++) {
			isBlank(a, 1);
		}
		System.out.println(isBlank(a, 1));
		System.out.println(System.currentTimeMillis() - t);
	}

	/**
	 * String为空判断
	 * 
	 * @param str
	 *            字符串
	 * @param type
	 *            判断类型: 0--空,空串; 1--空,空串,"null",内部空字符
	 * @return
	 */
	public static boolean isBlank(String str, int type) {
		// System.out.println(str + "--" + type);

		if (str == null) {
			return true;
		} else {
			str = str.trim();
		}

		if (str.length() == 0) {
			return true;
		}

		if ("null".equals(str)) {
			return true;
		}

		if (type >= 1) {
			for (int i = 0; i < str.length(); i++) {
				if (!Character.isWhitespace(str.charAt(i))) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
}
