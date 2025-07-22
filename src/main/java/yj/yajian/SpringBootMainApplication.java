package yj.yajian;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class SpringBootMainApplication {

    public static void main(String[] args) {
        new File("./logs").mkdirs();
        log.info("SpringBootMainApplication starting");

        SpringApplication.run(SpringBootMainApplication.class, args);
        log.info("http://127.0.0.1:18888/main.html");
    }

}
