package love.image.service;

import love.image.util.Function;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService {

	// 运行的Spring环境中如果存在就注入，没有就忽略
	@Autowired(required = false)
	private ShardedJedisPool shardedJedisPool;

	private <T> T execute(Function<T, ShardedJedis> fun) {
		ShardedJedis shardedJedis = null;
		try {
			// 从连接池中获取到jedis分片对象
			shardedJedis = shardedJedisPool.getResource();
			return fun.callback(shardedJedis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != shardedJedis) {
				// 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
				shardedJedis.close();
			}
		}
		return null;
	}

	/**
	 * 设置生存时间，单位为秒
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public Long expire(final String key, final Integer seconds) {
		return this.execute(new Function<Long, ShardedJedis>() {
			@Override
			public Long callback(ShardedJedis e) {
				return e.expire(key, seconds);
			}
		});
	}

	/**
	 * 执行set操作
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(final String key, final String value) {
		return this.execute(new Function<String, ShardedJedis>() {
			@Override
			public String callback(ShardedJedis e) {
				return e.set(key, value);
			}
		});
	}

	/**
	 * 执行set操作并且设置生存时间，单位为秒
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public String set(final String key, final String value,
			final Integer seconds) {
		return this.execute(new Function<String, ShardedJedis>() {
			@Override
			public String callback(ShardedJedis e) {
				String str = e.set(key, value);
				e.expire(key, seconds);
				return str;
			}
		});
	}

	/**
	 * 执行GET操作
	 * 
	 * @param key
	 * @return
	 */
	public String get(final String key) {
		return this.execute(new Function<String, ShardedJedis>() {
			@Override
			public String callback(ShardedJedis e) {

				return e.get(key);
			}
		});
	}

	/**
	 * 执行GET操作
	 * 
	 * @param key
	 * @return keys
	 */
	public Set<String> getKeys(final String key) {
		return this.execute(new Function<Set<String>, ShardedJedis>() {
			@Override
			public Set<String> callback(ShardedJedis e) {

				return e.getShard(key).keys(key);
			}
		});
	}

	/**
	 * 执行DEL操作
	 * 
	 * @param key
	 * @return
	 */
	public Long del(final String key) {
		return this.execute(new Function<Long, ShardedJedis>() {
			@Override
			public Long callback(ShardedJedis e) {
				return e.del(key);
			}
		});
	}

	// ****set****************************************************** //

	/**
	 * setAdd
	 * 
	 * @param setKey
	 * @param setValue
	 * @return
	 */
	public String setAdd(final String setKey, final String setValue) {
		return this.execute(new Function<String, ShardedJedis>() {
			@Override
			public String callback(ShardedJedis e) {
				return "" + e.sadd(setKey, setValue);
			}
		});
	}

	/**
	 * setRemove
	 * 
	 * @param setKey
	 * @param setValue
	 * @return
	 */
	public String setRemove(final String setKey, final String setValue) {
		return this.execute(new Function<String, ShardedJedis>() {
			@Override
			public String callback(ShardedJedis e) {
				return "" + e.srem(setKey, setValue);
			}
		});
	}

	/**
	 * setRandMember
	 * 
	 * @param setKey
	 * @return
	 */
	public String setRandMember(final String setKey) {
		return this.execute(new Function<String, ShardedJedis>() {
			@Override
			public String callback(ShardedJedis e) {
				return e.srandmember(setKey);
			}
		});
	}

	/**
	 * setCard
	 * 
	 * @param setKey
	 * @return
	 */
	public String setCard(final String setKey) {
		return this.execute(new Function<String, ShardedJedis>() {
			@Override
			public String callback(ShardedJedis e) {
				return "" + e.scard(setKey);
			}
		});
	}

	/**
	 * 执行事务操作
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public ShardedJedisPipeline pipelined() {
		return this.execute(new Function<ShardedJedisPipeline, ShardedJedis>() {
			@Override
			public ShardedJedisPipeline callback(ShardedJedis e) {
				ShardedJedisPipeline pipelined = e.pipelined();
				return pipelined;
			}
		});
	}

	/**
	 * f db
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String f() {
		return this.execute(new Function<String, ShardedJedis>() {
			@Override
			public String callback(ShardedJedis e) {
				return "";
			}
		});
	}
}
