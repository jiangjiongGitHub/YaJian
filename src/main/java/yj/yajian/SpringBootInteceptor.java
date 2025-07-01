package yj.yajian;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class SpringBootInteceptor implements HandlerInterceptor {
    // log日志
    Logger log = LoggerFactory.getLogger(SpringBootInteceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestUri = request.getRequestURI();
        log.info("requestUri:" + requestUri);

        HttpSession httpSession = request.getSession();
        String contractorUser = (String) httpSession.getAttribute("user");
        if (contractorUser == null) {
            log.warn("未登录：{}", requestUri);

            // 未登录或者session过期在这里执行跳转登录页面
            if (false) {
                response.sendRedirect("/login");
                return false;
            }
        }

        Map<?, ?> map = request.getParameterMap();
        Set<?> keSet = map.entrySet();
        for (Iterator<?> itr = keSet.iterator(); itr.hasNext(); ) {
            @SuppressWarnings("rawtypes")
            Map.Entry me = (Map.Entry) itr.next();

            Object ok = me.getKey();
            Object ov = me.getValue();
            String[] value = new String[1];
            if (ov instanceof String[]) {
                value = (String[]) ov;
            } else {
                value[0] = ov.toString();
            }

            for (int k = 0; k < value.length; k++) {
                log.info("参数：" + ok + "=" + value[k]);
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
