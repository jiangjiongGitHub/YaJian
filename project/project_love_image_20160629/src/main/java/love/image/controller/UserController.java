package love.image.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import love.image.model.Student;
import love.image.model.StudentDao;
import love.image.model.t_user;
import love.image.service.RedisService;
import love.image.service.UserService;
import love.image.util.JJ_ImageUtil;
import love.image.util.MD5;
import love.image.util.StringUtil;

import org.apache.catalina.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping("/index")
	public String index(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) MultipartFile file, Model model)
			throws Exception {
		// http://192.168.020.122:15000/love_image/user/index.home
		// http://127.000.000.001:15000/love_image/user/index.home

		String path1 = request.getSession().getServletContext().getRealPath("")
				+ File.separator;
		String path2 = new SimpleDateFormat("yyyyMM").format(new Date())
				+ File.separator;
		String name = new SimpleDateFormat("yyyyMMddHHmmssSSS")
				.format(new Date()) + ".jpg";
		System.out.println(path1 + path2 + name);

		File filePath = new File(path1 + path2);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}

		StudentDao stuDao = new StudentDao(path1 + "db/students.xml");

		try {
			int wh_size = 1500;

			if (file != null && file.getInputStream().available() > 0) {

				InputStream in = file.getInputStream();
				BufferedImage srcImgBuffer = ImageIO.read(in);

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
					ImageIO.write(buffImg, "JPEG", new File(path1 + path2 + "_"
							+ name));
					// read jpg
					Image img = ImageIO.read(new File(path1 + path2 + "_"
							+ name));

					BufferedImage buffImg_new = new BufferedImage(width_new,
							width_new, BufferedImage.TYPE_INT_RGB);

					Graphics2D g = buffImg_new.createGraphics();
					g.setColor(new Color(255, 255, 255, 0));
					g.fillRect(0, 0, width_new, width_new);
					g.drawImage(img, 0, (width_new - height_new) / 2,
							width_new, height_new, Color.white, null);
					g.dispose();

					// write jpg
					ImageIO.write(buffImg_new, "JPEG", new File(path1 + path2
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
					ImageIO.write(buffImg, "JPEG", new File(path1 + path2 + "_"
							+ name));
					// read jpg
					Image img = ImageIO.read(new File(path1 + path2 + "_"
							+ name));

					BufferedImage buffImg_new = new BufferedImage(height_new,
							height_new, BufferedImage.TYPE_INT_RGB);
					Graphics2D g = buffImg_new.createGraphics();
					g.setColor(new Color(255, 255, 255, 0));
					g.fillRect(0, 0, height_new, height_new);
					g.drawImage(img, (height_new - width_new) / 2, 0,
							width_new, height_new, Color.white, null);
					g.dispose();

					// write jpg
					ImageIO.write(buffImg_new, "JPEG", new File(path1 + path2
							+ name));
				}

				Student s = new Student();
				s.setId((stuDao.length() + 1) + "");
				s.setName(path2 + name);
				stuDao.create(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("image", stuDao.getall());

		// HttpSession session = request.getSession();
		// session.setAttribute("image", "true");

		return "/user/image";
	}

	public static void main(String[] args) throws Exception {
		StudentDao stuDao = new StudentDao("students.xml");
	}

}
