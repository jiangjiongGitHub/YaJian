package test;

import love.image.model.t_user;
import love.image.service.RedisService;
import love.image.service.UserService;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.ShardedJedisPipeline;

import com.alibaba.fastjson.JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring_mvc.xml" })
public class UserServiceTest {

	private static final Logger LOGGER = Logger
			.getLogger(UserServiceTest.class);

	@Test
	public void testQueryById1() {
		LOGGER.info("Test");
	}
}