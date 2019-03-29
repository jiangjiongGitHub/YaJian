package YaJian;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootTests {

	@Test
	public void contextLoads() {
		for (int i = 0; i < 10; i++) {
			System.out.println("TEST JUNIT SUCCESS");
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			System.out.println("TEST MAIN SUCCESS");
		}
	}

}
