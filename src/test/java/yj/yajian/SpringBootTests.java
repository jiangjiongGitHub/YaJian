package yj.yajian;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootTests {

	private static Logger log = LoggerFactory.getLogger(SpringBootTests.class);
	@Test
	public void contextLoads() {
		for (int i = 0; i < 10; i++) {
			log.info("TEST JUNIT SUCCESS");
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			log.info("TEST MAIN SUCCESS");
		}
	}

}
