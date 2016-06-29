package love.image.intercepter;

import love.image.service.RedisService;
import love.image.service.UserService;
import love.image.util.StringUtil;

import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class AppInterceptor implements HandlerInterceptor {

	// 7天
	private static final int Day_7 = 7 * 24 * 60 * 60;

	private Log log = LogFactory.getLog(AppInterceptor.class);

	public AppInterceptor() {

	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());

		log.info("\n\n\n\n");
		log.info("preHandle");

		log.info("requestUri:" + requestUri);
		log.info("contextPath:" + contextPath);
		log.info("url:" + url);

		String authJson = request.getParameter("auth");
		String infoJson = request.getParameter("info");
		String sign = request.getParameter("sign");

		// 登录接口判断重复登录
		if (requestUri.equals("/YiYuanYunGou/user/login.yyyg")) {
			log.info("登录校验");

		}

		if (sign != null) {
			log.info("sign校验");
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("");
		log.info("postHandle");

		String authJson = request.getParameter("auth");
		String sign = request.getParameter("sign");
		if (sign != null) {
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.info("");
		log.info("afterCompletion");
	}

	private void responseData(Reslut data, HttpServletResponse response) {
		try {
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(JSON.toJSON(data));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class Reslut {
		private int errCode;

		private String msg;

		private Object data;

		public int getErrCode() {
			return errCode;
		}

		public void setErrCode(int errCode) {
			this.errCode = errCode;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public Object getData() {
			return data;
		}

		public void setData(Object data) {
			this.data = data;
		}

		public Reslut(int i, String msg, Object data) {
			this.errCode = i;
			this.msg = msg;
			this.data = data;
		}

	}
}