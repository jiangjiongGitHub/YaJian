package YaJian;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
public class SpringBootController {

	private static Logger log = LoggerFactory.getLogger(SpringBootController.class);

	private static void logInfo(Object obj) {
		// System.out.println("SpringBootController--" + obj);
		log.debug(obj + "");
	}

	/**
	 * 127.0.0.1:18888/main
	 * @return
	 */
	@RequestMapping("main")
	@ResponseBody
	public String main() {
		JSONObject json = new JSONObject();
		json.put("timestamp", System.currentTimeMillis());
		json.put("status", 404);
		json.put("error", "Not Found");
		json.put("message", "No message available");
		json.put("path", "/main.html");
		return json.toJSONString();
	}

	/**
	 * 127.0.0.1:18888/cron?cron=000
	 * 
	 * @return
	 */
	@RequestMapping("cron")
	@ResponseBody
	public String cron(String cron) {
		JSONObject json = new JSONObject();
		json.put("message", "Error");
		json.put("status", "-1");

		cron = cron.trim();
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

		Date curTime = new Date();
		CronExpression expression;
		try {
			// 第一个字段是秒，"/"后面最好被60整除分配均匀
			// 星期和日期不能都是问号，1是星期日
			// String c1 = "*/15 15,16 11-12 15 8 ? 2018";
			// String c2 = "5/15 15,16 11-12 15 8 ? 2018";
			// String c6 = "5/30 3/5 11-12 ? 8 7,1 2019";
			// String c7 = "0/30 30 10 ? 2 1,7 2019-2021";

			expression = new CronExpression(cron);
			int i = 0;
			Map<String, String> map = new LinkedHashMap<String, String>();
			while (curTime != null && i < 100) {
				curTime = expression.getNextValidTimeAfter(curTime);
				if (curTime != null) {
					i++;

					String t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(curTime);
					// String ii = new DecimalFormat("000").format(i);
					map.put("cron" + String.format("%03d", i), t);
					logInfo(t);
				}
			}
			json.put("map", map);
			json.put("mapCount", i);
		} catch (Exception e) {
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("cron" + String.format("%03d", 1), "请输入正确的CRON表达式");
			json.put("map", map);
			log.error("CRON表达式异常");
		}

		json.put("message", "Success");
		json.put("status", "0");
		return json.toJSONString();
	}

	/**
	 * 127.0.0.1:18888/set?str=000
	 * 
	 * @return
	 */
	@RequestMapping("set")
	@ResponseBody
	public String strSet(String str, String key) {
		JSONObject json = new JSONObject();
		json.put("message", "Error");
		json.put("status", "-1");

		if (key == null || "".equals(key)) {
			key = "0";
		}
		int keys = 0;
		try {
			keys = Integer.parseInt(key);
		} catch (Exception e) {
			keys = 0;
			log.error("Integer.parseInt(key)异常");
		}

		str = str.trim();
		json.put("str", str);
		json.put("key", key);
		json.put("strEncode", getEncoding(str));
		logInfo(str);

		if (str == null || "".equals(str)) {
			json.put("message", "请输入文本内容");
			json.put("status", "1");
			return json.toJSONString();
		}

		{
			byte[] byteStr = str.getBytes();
			int len = byteStr.length;

			StringBuffer sb_ = new StringBuffer(); // 数组扩容，导致内存溢出

			for (int i = 0; i < len; i++) {
				// 排查：1000次打印一次，精确到1000，然后逐个循环，找到内存溢出值。
				if (i % 3 == 0) {
					sb_.append(byteStr[i] + keys + ",");
				}
				if (i % 3 == 1) {
					sb_.append(byteStr[i] + keys + ",");
				}
				if (i % 3 == 2) {
					sb_.append(byteStr[i] + keys + ",");
				}
			}

			json.put("sb_", sb_.toString());
		}

		json.put("message", "Success");
		json.put("status", "0");
		return json.toJSONString();
	}

	/**
	 * 127.0.0.1:18888/get?str=000
	 * 
	 * @return
	 */
	@RequestMapping("get")
	@ResponseBody
	public String strGet(String str, String key) {
		JSONObject json = new JSONObject();
		json.put("message", "Error");
		json.put("status", "-1");

		if (key == null || "".equals(key)) {
			key = "0";
		}
		int keys = 0;
		try {
			keys = Integer.parseInt(key);
		} catch (Exception e) {
			keys = 0;
			log.error("Integer.parseInt(key)异常");
		}

		str = str.trim();
		json.put("str", str);
		json.put("key", key);
		logInfo(str);

		if (str == null || "".equals(str)) {
			json.put("message", "请输入文本内容");
			json.put("status", "1");
			return json.toJSONString();
		}

		try {
			StringTokenizer st = new StringTokenizer(str, ","); // split速度太慢
			byte[] byteStr = new byte[st.countTokens()];
			int i = 0;
			while (st.hasMoreElements()) {
				String stri = st.nextToken();
				if (i % 3 == 0) {
					byteStr[i] = (byte) (Integer.parseInt(stri) - keys);
				}
				if (i % 3 == 1) {
					byteStr[i] = (byte) (Integer.parseInt(stri) - keys);
				}
				if (i % 3 == 2) {
					byteStr[i] = (byte) (Integer.parseInt(stri) - keys);
				}
				i++;
			}

			json.put("sb_", new String(byteStr, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.error("new String(byteStr, \"UTF-8\")异常");
		}

		json.put("message", "Success");
		json.put("status", "0");
		return json.toJSONString();
	}

	/**
	 * 127.0.0.1:18888/html1
	 * 
	 * @return
	 */
	@RequestMapping("html1")
	public String html1(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		json.put("fast", "done");
		request.setAttribute("key", "hello world");
		return "index"; // thymeleaf to templates index.html
	}

	/**
	 * 127.0.0.1:18888/html2
	 * 
	 * @return
	 */
	@RequestMapping("html2")
	public String html2(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		json.put("fast", "done");
		request.setAttribute("key", "hello world");
		return "index.html"; // thymeleaf to static index.html
	}

	public static void main(String[] args) {
		// 获取系统默认编码
		System.out.println("系统默认编码：" + System.getProperty("file.encoding")); // 查询结果GBK
		// 系统默认字符编码
		System.out.println("系统默认字符编码：" + Charset.defaultCharset()); // 查询结果GBK
		// 操作系统用户使用的语言
		System.out.println("系统默认语言：" + System.getProperty("user.language")); // 查询结果zh

		System.out.println();
		String s1 = "hi, nice to meet you!";
		String s2 = "hi, 我来了！";

		System.out.println(getEncoding(s1));
		System.out.println(getEncoding(s2));

	}

	public static String getEncoding(String str) {
		try {
			String[] encodes = new String[] { "ASCII", "ISO-8859-1", "GBK", "UTF-8" };

			for (String encode : encodes) {
				logInfo(encode + "--{" + str + "}--{" + new String(str.getBytes(), encode) + "}");
				if (str.equals(new String(str.getBytes(), encode))) {
					return encode;
				}
			}
		} catch (UnsupportedEncodingException e) {
			log.error("new String(str.getBytes(), encode)异常");
		}
		return "未识别编码格式";
	}

}
