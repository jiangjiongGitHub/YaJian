package love.image.util;

public class JJImageUtil {

	public static void main(String[] args) {

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

			return new java.io.FileInputStream(new java.io.File("0.jpg"));
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
}
