package yj.yajian;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class SpringBootMainApplication {

    public static void main(String[] args) {
        new File("./logs").mkdirs();
        log.info("SpringBootMainApplication starting");

        // 设置日志文件名的时间戳部分
        System.setProperty("bySecond", new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss").format(new Date()));

        // 启动应用并获取上下文
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootMainApplication.class, args);

        // 从环境变量中获取 server.port，如果没有设置则默认为 18888
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "80");

        // 输出实际的访问地址
        log.info(String.format("http://127.0.0.1:%s/main.html", port));
    }

}
