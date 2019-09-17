package cn.com.yeexun.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisConfig {
	
	@Value("${redis.pool.maxActive}")
	int maxTotal;
	
	@Value("${redis.pool.maxIdle}")
	int maxIdle;
	
	@Value("${redis.host}")
	String host;
	
	@Value("${redis.port}")
	int port;
	
	@Value("${redis.timeout}")
	int timeout;
	
	@Value("${redis.password}")
	String password;
	
	@Value("${redis.database}")
	int database;
	
	@Bean(destroyMethod="destroy")
	public JedisPool jedisPool() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setTestOnBorrow(true);
		jedisPoolConfig.setTestOnReturn(true);
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database);
		return jedisPool;
	}

}
