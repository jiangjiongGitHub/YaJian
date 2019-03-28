package YaJian;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;


@org.springframework.stereotype.Controller
public class SpringBootController {

	private Logger log = LoggerFactory.getLogger(SpringBootController.class);

	/**
	 * 127.0.0.1:8080/main
	 * 
	 * @return
	 */
	@org.springframework.web.bind.annotation.RequestMapping("main")
	@org.springframework.web.bind.annotation.ResponseBody
	public String main() {
		com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
		json.put("fast", "done");
		return json.toJSONString();
	}

	/**
	 * 127.0.0.1:8080/cron?cron=000
	 * 
	 * @return
	 */
	@RequestMapping("cron")
	@ResponseBody
	public String cron(String cron) {
		JSONObject json = new JSONObject();
		json.put("message", "Error");
		json.put("status", "-1");
		
		cron=cron.trim();
		json.put("cron", cron);
		logInfo(cron);

		if (cron == null || "".equals(cron)) {
			json.put("message", "请输入正确的表达式");
			json.put("status", "1");
			return json.toJSONString();
		}

		int count = 0;// 统计空格个数
		int len = cron.length();
		for (int i = 0; i < len; i++) {
			char a = cron.charAt(i);
			// 空格
			if (a == ' ') {
				count++;
			}
		}
		logInfo(count);

		json.put("cronCount", count);
		if (count < 5) {
			json.put("message", "请输入正确的表达式");
			json.put("status", "2");
			return json.toJSONString();
		}

		// TODO 解析
		Date curTime = new Date();
		CronExpression expression;
		try {
			// 第一个字段是秒，"/"后面最好被60整除分配均匀
			// 星期和日期不能都是问号，1是星期日
			String c1 = "*/15 15,16 11-12 15 8 ? 2018";
			String c2 = "5/15 15,16 11-12 15 8 ? 2018";
			String c6 = "5/30 3/5 11-12 ? 8 7,1 2019";
			String c7 = "0/30 30 10 ? 2 1,7 2019-2021";

			expression = new CronExpression(cron);
			int i = 0;
			Map<String,String> map=new LinkedHashMap<String, String>();
			while (curTime != null && i < 100) {
				curTime = expression.getNextValidTimeAfter(curTime);
				if (curTime != null) {
					i++;
					
					String t=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(curTime);
					map.put("cron" + i, t);
					logInfo(t);
				}
			}
			json.put("map", map);
			json.put("mapCount", i);
		} catch (Exception e) {
			e.printStackTrace();
		}

		json.put("message", "Success");
		json.put("status", "0");
		return json.toJSONString();
	}

	private void logInfo(Object cron) {
		System.out.println("SpringBootController--" + cron);
	}

	/**
	 * 127.0.0.1:8080/html1
	 * 
	 * @return
	 */
	@org.springframework.web.bind.annotation.RequestMapping("html1")
	public String html1(HttpServletRequest request) {
		com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
		json.put("fast", "done");
		request.setAttribute("key", "hello world");
		return "in"; // thymeleaf
	}

	/**
	 * 127.0.0.1:8080/html2
	 * 
	 * @return
	 */
	@org.springframework.web.bind.annotation.RequestMapping("html2")
	public String html2() {
		com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
		json.put("fast", "done");
		return "in.html";
	}

}
