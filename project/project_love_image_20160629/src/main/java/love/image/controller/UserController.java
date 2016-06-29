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
	@Autowired
	private UserService userService;
	@Autowired
	private RedisService redisService;

	@RequestMapping("/index")
	public String index(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) MultipartFile file) {
		// http://192.168.20.122:15000/love_image/user/index.home

		String wh = "w";
		int wh_size = 1000;
		String path1 = request.getSession().getServletContext().getRealPath("")
				+ File.separator;
		String path2 = new SimpleDateFormat("yyyyMM").format(new Date())
				+ File.separator;
		String name = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
				+ ".jpg";
		System.out.println(path1 + path2 + name);

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
							new java.io.File("0.jpg"));

					java.awt.Image img = javax.imageio.ImageIO
							.read(new java.io.File("0.jpg"));

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
						g.drawImage(img, (height_new - width_new) / 2, 0,
								width_new, height_new, java.awt.Color.white,
								null);

						System.out.println("w<h");
						g.dispose();

						javax.imageio.ImageIO.write(buffImg_new, "JPEG",
								new java.io.File("1.jpg"));
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/user/index";
	}

	@RequestMapping("/showInfo/{userId}")
	public String showUserInfo(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap,
			@PathVariable int userId) {
		// http://192.168.20.122:15000/love_image/user/showInfo/1.home

		int max = userService.selectMaxUser();
		System.out.println(userId + "_" + max);
		t_user userInfo = userService.selectByPrimaryKey(max);
		modelMap.addAttribute("userInfo", userInfo);

		// 登陆统计Redis
		if (userInfo != null) {
			String loginCountKey = userInfo.getId() + "_LOGIN_COUNT";
			String loginCountValue = redisService.get(loginCountKey);
			if (StringUtil.isBlank(loginCountValue)) {
				redisService.set(loginCountKey, "0");
			} else {
				int count = Integer.parseInt(loginCountValue);
				redisService.set(loginCountKey, "" + (count + 1));
			}
		}

		// 启动插入数据和插入redis
		{
			int id = max + 1;
			for (int i = id; i < id + 10; i++) {
				String key = "key_" + i;
				String value = MD5.md5(key);
				redisService.set("key", key + " -- " + value, 24 * 60 * 60);

				t_user u = new t_user();
				u.setAdd_status(0);
				u.setAdd_time(new Date());
				u.setLogin_count(1);
				u.setLogin_ip(request.getRemoteAddr());
				u.setUser_image("url");
				u.setUser_level(1);
				u.setUser_money(0.00);
				u.setUser_name(key);
				u.setUser_name_nick("MD5");
				u.setUser_password(value);
				u.setUser_telephone("tel");
				userService.insert(u);
			}
		}
		modelMap.addAttribute("key", redisService.get("key"));

		return "/user/showInfo";
	}

	@RequestMapping("/showInfo_test/{userId}")
	public void showInfo_test(HttpServletRequest request,
			HttpServletResponse response, @PathVariable int userId) {
		// http://127.0.0.1:8080/love_image/user/showInfo_test/1.

		String ip = request.getRemoteAddr();
		response.setCharacterEncoding("utf-8");
		StringBuffer str = new StringBuffer();
		str.append("<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>");
		str.append("<html>");
		str.append("<head>");
		str.append("</head>");
		str.append("<body bgcolor='rgb(202,234,202)'>");
		str.append("<div style='background-color:rgb(202,234,202);'>");

		str.append("<div style='width:600px; height:300px;'>");
		str.append("<font color=\"red\">" + ip + "</font>");
		str.append("</div>");

		str.append("</div>");
		str.append("</body>");
		str.append("</html>");
		try {
			response.getWriter().write(str.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
