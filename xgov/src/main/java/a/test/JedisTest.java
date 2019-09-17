package a.test;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class JedisTest {
	
	
	public static void main1(String[] args) {
		Jedis  jedis = new Jedis ("10.221.121.5", 6379); 
		//验证密码，如果没有设置密码这段代码省略
		jedis.auth("147852"); 
		jedis.connect();//连接
		jedis.select(11);
		/*Set<String> ids = jedis.smembers("id");
		System.out.println(ids);
		Set<String> names = jedis.smembers("name");
		System.out.println(names);
		Boolean isExist = jedis.sismember("id", "1");
		Boolean sismember = jedis.sismember("name", "1");
		System.out.println(isExist + "=====" + sismember);*/
		
		/*Long sadd = jedis.sadd("id", "1");
		System.out.println(sadd);
		Long sadd2 = jedis.sadd("id", "1");
		System.out.println(sadd2);*/
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			jedis.sadd("id", i + "");
//			jedis.set(i + "", i * 10 + "");
		}
		System.out.println("耗时：" + (System.currentTimeMillis() - start) / 1000 + "s.");
		
		

		jedis.disconnect();//断开连接
	}
	
	public static void main(String[] args) {
		Jedis  jedis = new Jedis ("10.221.121.5", 6379); 
		//验证密码，如果没有设置密码这段代码省略
		jedis.auth("147852"); 
		jedis.connect();//连接
		jedis.select(11);
		
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			jedis.sadd("id", i + "");
		}
		System.out.println("耗时：" + (System.currentTimeMillis() - start) / 1000 + "s.");
		
		
		start = System.currentTimeMillis();
		Pipeline pip = jedis.pipelined();
		for (int i = 0; i < 1000; i++) {
			pip.sadd("pipid", i + "");
		}
		pip.sync();
		System.out.println("耗时：" + (System.currentTimeMillis() - start) / 1000 + "s.");
		
		
		
	}

}
