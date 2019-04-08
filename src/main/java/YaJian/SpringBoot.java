package YaJian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SpringBoot extends SpringBootServletInitializer {

	/** 屏蔽自带tomcat **/
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SpringBoot.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot.class, args);
	}

}
