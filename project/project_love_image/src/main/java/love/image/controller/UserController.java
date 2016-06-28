package love.image.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import love.image.model.t_user;
import love.image.service.RedisService;
import love.image.service.UserService;
import love.image.util.MD5;
import love.image.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private RedisService redisService;

	@RequestMapping("/showInfo/{userId}")
	public String showUserInfo(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap,
			@PathVariable int userId) {
		// http://127.0.0.1:8080/love_image/user/showInfo/1.

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
