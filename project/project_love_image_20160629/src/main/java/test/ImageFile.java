package test;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import love.image.model.Student;

/*
 * 获取一个目录下的所有文件夹和文件，包括子文件夹和子文件 。
 */
public class ImageFile {

	public static void main(String[] args) {
		// File dir = new File("E:\\360downloads\\wpcache\\");
		File dir = new File(".");

		getAllFiles(dir, 0);
	}

	// 获取层级的方法
	public static String getLevel(int level) {
		StringBuilder sb = new StringBuilder();
		for (int l = 0; l < level; l++) {
			sb.append("|--");
		}
		return sb.toString();
	}

	public static void getAllFiles(File dir, int level) {
		System.out.println(getLevel(level) + dir.getName());

		level++;
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				// 这里面用了递归的算法
				// getAllFiles(files[i], level);
			} else {
				System.out.println(getLevel(level) + files[i]);
				reSizeImage(files[i].getAbsolutePath());
			}
		}
	}

	public static void reSizeImage(String filePath) {

		if (filePath.toLowerCase().endsWith(".jpg")
				|| filePath.toLowerCase().endsWith(".png")) {
			// request.getSession().getServletContext().getRealPath("");
			String path1 = "D:\\" + File.separator;
			String path2 = new SimpleDateFormat("yyyyMM").format(new Date())
					+ File.separator;
			String name = new SimpleDateFormat("yyyyMMddHHmmssSSS")
					.format(new Date()) + ".png";
			System.out.println(path1 + path2 + name);

			File newFilePath = new File(path1 + path2);
			if (!newFilePath.exists()) {
				newFilePath.mkdirs();
			}

			int wh_size = 2000;
			try {
				BufferedImage srcImgBuffer = ImageIO.read(new File(filePath));

				int width_old = srcImgBuffer.getWidth();
				int height_old = srcImgBuffer.getHeight();

				System.out.println("width_old -- " + width_old);
				System.out.println("height_old -- " + height_old);

				if (width_old >= height_old) {
					System.out.println("width_old >= height_old");

					int width = wh_size;
					int height = height_old * wh_size / width_old;

					BufferedImage buffImg = new BufferedImage(width, height,
							BufferedImage.TYPE_INT_RGB);

					int width_new = buffImg.getWidth();
					int height_new = buffImg.getHeight();

					System.out.println("width_new -- " + width_new);
					System.out.println("height_new -- " + height_new);

					buffImg.getGraphics().drawImage(
							srcImgBuffer.getScaledInstance(width, height,
									Image.SCALE_SMOOTH), 0, 0, null);
					// write jpg
					ImageIO.write(buffImg, "png", new File(path1 + path2 + "_"
							+ name));
					// read jpg
					Image img = ImageIO.read(new File(path1 + path2 + "_"
							+ name));

					BufferedImage buffImg_new = new BufferedImage(width_new,
							width_new, BufferedImage.TYPE_INT_RGB);

					Graphics2D g = buffImg_new.createGraphics();

					// bgn 边框变透明
					buffImg_new = g.getDeviceConfiguration()
							.createCompatibleImage(width_new, width_new,
									Transparency.TRANSLUCENT);
					g.dispose();
					g = buffImg_new.createGraphics();
					g.setColor(new Color(255, 255, 255, 0));
					// end

					g.fillRect(0, 0, width_new, width_new);
					g.drawImage(img, 0, (width_new - height_new) / 2,
							width_new, height_new, Color.white, null);
					g.dispose();

					// write jpg
					ImageIO.write(buffImg_new, "png", new File(path1 + path2
							+ name));
				} else {
					System.out.println("width_old < height_old");

					int height = wh_size;
					int width = width_old * wh_size / height_old;

					BufferedImage buffImg = new BufferedImage(width, height,
							BufferedImage.TYPE_INT_RGB);

					int width_new = buffImg.getWidth();
					int height_new = buffImg.getHeight();

					System.out.println("width_new -- " + width_new);
					System.out.println("height_new -- " + height_new);

					buffImg.getGraphics().drawImage(
							srcImgBuffer.getScaledInstance(width, height,
									Image.SCALE_SMOOTH), 0, 0, null);
					// write jpg
					ImageIO.write(buffImg, "png", new File(path1 + path2 + "_"
							+ name));
					// read jpg
					Image img = ImageIO.read(new File(path1 + path2 + "_"
							+ name));

					BufferedImage buffImg_new = new BufferedImage(height_new,
							height_new, BufferedImage.TYPE_INT_RGB);

					Graphics2D g = buffImg_new.createGraphics();

					// bgn 边框变透明
					buffImg_new = g.getDeviceConfiguration()
							.createCompatibleImage(height_new, height_new,
									Transparency.TRANSLUCENT);
					g.dispose();
					g = buffImg_new.createGraphics();
					g.setColor(new Color(255, 255, 255, 0));
					// end

					g.fillRect(0, 0, height_new, height_new);
					g.drawImage(img, (height_new - width_new) / 2, 0,
							width_new, height_new, Color.white, null);
					g.dispose();

					// write jpg
					ImageIO.write(buffImg_new, "png", new File(path1 + path2
							+ name));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}