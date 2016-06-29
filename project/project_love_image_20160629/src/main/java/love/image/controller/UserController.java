package love.image.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import love.image.model.t_user;
import love.image.service.RedisService;
import love.image.service.UserService;
import love.image.util.JJ_ImageUtil;
import love.image.util.MD5;
import love.image.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
			@RequestParam(required = false) MultipartFile file) {
		// http://192.168.020.122:15000/love_image/user/index.home
		// http://127.000.000.001:15000/love_image/user/index.home

		String wh = "w";
		int wh_size = 1000;
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

		try {
			if (file != null && file.getInputStream().available() > 0) {

				java.io.InputStream in = file.getInputStream();
				java.awt.image.BufferedImage srcImgBuffer = javax.imageio.ImageIO
						.read(in);

				int width_old = srcImgBuffer.getWidth();
				int height_old = srcImgBuffer.getHeight();

				System.out.println(width_old);
				System.out.println(height_old);

				if ("w".equals(wh)) {
					int width = wh_size;
					int height = height_old * wh_size / width_old;

					java.awt.image.BufferedImage buffImg = new java.awt.image.BufferedImage(
							width, height,
							java.awt.image.BufferedImage.TYPE_INT_RGB);

					int width_new = buffImg.getWidth();
					int height_new = buffImg.getHeight();

					System.out.println(width_new);
					System.out.println(height_new);

					buffImg.getGraphics().drawImage(
							srcImgBuffer.getScaledInstance(width, height,
									java.awt.Image.SCALE_SMOOTH), 0, 0, null);

					javax.imageio.ImageIO.write(buffImg, "JPEG",
							new java.io.File(path1 + path2 + "_" + name));

					java.awt.Image img = javax.imageio.ImageIO
							.read(new java.io.File(path1 + path2 + "_" + name));

					if (width_new >= height_new) {
						java.awt.image.BufferedImage buffImg_new = new java.awt.image.BufferedImage(
								width_new, width_new,
								java.awt.image.BufferedImage.TYPE_INT_RGB);

						java.awt.Graphics2D g = buffImg_new.createGraphics();
						g.setColor(java.awt.Color.white);
						g.fillRect(0, 0, width_new, width_new);
						g.drawImage(img, 0, (width_new - height_new) / 2,
								width_new, height_new, java.awt.Color.white,
								null);

						g.dispose();
						javax.imageio.ImageIO.write(buffImg_new, "JPEG",
								new java.io.File(path1 + path2 + name));
					} else {
						java.awt.image.BufferedImage buffImg_new = new java.awt.image.BufferedImage(
								height_new, height_new,
								java.awt.image.BufferedImage.TYPE_INT_RGB);
						java.awt.Graphics2D g = buffImg_new.createGraphics();
						g.setColor(java.awt.Color.white);
						g.fillRect(0, 0, height_new, height_new);
						g.drawImage(img, (height_new - width_new) / 2, 0,
								width_new, height_new, java.awt.Color.white,
								null);

						g.dispose();
						javax.imageio.ImageIO.write(buffImg_new, "JPEG",
								new java.io.File(path1 + path2 + name));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/user/index";
	}

}
