package YaJian;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringBootInteceptorConfig implements WebMvcConfigurer {

	@Autowired
	private SpringBootInteceptor springBootInteceptor;

	// 配置拦截的资源以及放行的资源
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 放行静态资源
		registry.addInterceptor(springBootInteceptor).addPathPatterns("/**").excludePathPatterns("/login")
				.excludePathPatterns("/img/**", "/css/**", "/fonts/**", "/js/**");
	}

	// 配置静态资源的位置
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

	}

}
