package cn.com.yeexun.utils;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;

@Service
public class JedisUtil {
	
	private Logger logger = Logger.getLogger(JedisUtil.class);
	
	@Autowired
	private JedisPool jedisPool;
	
	/** 
     * 设置一个key的过期时间（单位：秒） 
     * @param key key值 
     * @param seconds 多少秒后过期 
     * @return 1：设置了过期时间  0：没有设置过期时间/不能设置过期时间 
     */  
    public long expire(String key, int seconds) {  
        if (key==null || key.equals("")) {  
            return 0;  
        }  
  
        Jedis jedis = null;  
        try {  
        	jedis = jedisPool.getResource();
            return jedis.expire(key, seconds);  
        } catch (Exception ex) {  
            logger.error("EXPIRE error[key=" + key + " seconds=" + seconds + "]" + ex.getMessage(), ex);  
        } finally {  
        	jedis.close();  
        }  
        return 0;  
    }  
    
    public boolean set(String key, String value, int second) {  
    	Jedis jedis = null;  
        try {  
        	jedis = jedisPool.getResource();  
        	jedis.setex(key, second, value);  
            return true;  
        } catch (Exception ex) {  
            logger.error("set error.", ex);  
        } finally {  
        	jedis.close(); 
        }  
        return false;  
    }  
  
    public boolean set(String key, String value) {  
    	Jedis jedis = null;   
        try {  
        	jedis = jedisPool.getResource();   
        	jedis.set(key, value);  
            return true;  
        } catch (Exception ex) {  
            logger.error("set error.", ex);  
        } finally {  
        	jedis.close();   
        }  
        return false;  
    }  
  
    public String get(String key, String defaultValue) {  
    	Jedis jedis = null;    
        try {  
        	jedis = jedisPool.getResource(); 
            return jedis.get(key) == null?defaultValue:jedis.get(key);  
        } catch (Exception ex) {  
            logger.error("get error.", ex);  
        } finally {  
        	jedis.close();   
        }  
        return defaultValue;  
    }  
    
    public String get(String key) {  
    	Jedis jedis = null;    
        try {  
        	jedis = jedisPool.getResource(); 
            return jedis.get(key);  
        } catch (Exception ex) {  
            logger.error("get error.", ex);  
        } finally {  
        	jedis.close();   
        }
		return null;  
    } 
    
    public boolean del(String key) {  
    	Jedis jedis = null;      
        try {  
        	jedis = jedisPool.getResource(); 
        	jedis.del(key);  
            return true;  
        } catch (Exception ex) {  
            logger.error("del error.", ex);  
        } finally {  
        	jedis.close();     
        }  
        return false;  
    }  
  
    /** 
     * 判断key是否存在 
     * 
     * @param key   键值 
     * @return 
     */  
    /*public boolean exists(String key) {  
    	Jedis jedis = null;  
        boolean isExist = false;  
        try {  
        	jedis = jedisPool.getResource();   
            isExist = jedis.exists(key); 
        } catch (Exception ex) {  
            logger.error("existsHSet error.", ex);  
        } finally {  
        	jedis.close();   
        }  
        return isExist;  
    }  */
    public boolean exists(String key) throws Exception{
    	Jedis jedis = null;
    	try {
    		boolean isExist = false;  
	    	jedis = jedisPool.getResource();   
	        isExist = jedis.exists(key); 
	        return isExist;  
		} catch (Exception e) {
			logger.error("error:", e);
		} finally {
			jedis.close();
		}
    	return false;
    }  
    /*
     * redis 订阅
     * 
     */
    public void sub(JedisPubSub sub,String channels){
    	Jedis jedis = null; 
    	try {
    		jedis = jedisPool.getResource();   
			jedis.psubscribe(sub, channels);
		} catch (Exception e) {
			logger.error("sub error.", e); 
		} finally{
			jedis.close();  
		}
    }

    
    
    /**
     * 同步锁
	 * @param key
	 * @param second
	 * @return
	 */
	public boolean checkLock(String key,int second){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource(); 
			//之前不存在，上锁成功
			if(jedis.setnx(key, "lock") == 1){
				//设置锁期限
				jedis.expire(key,second);
				return true;
			}else{
				try {
					Thread.sleep(50L);
			    } catch (InterruptedException e) {
			    	logger.error(e);
			    }
				return false;
			}
		} catch (Exception e) {
			logger.error("sub error.", e); 
			return true;
		} finally{
			jedis.close();  
		}
		
	}
	
	public boolean setHash(String key, String field, String value) {
		Jedis jedis = null;    
        try {  
        	jedis = jedisPool.getResource(); 
            jedis.hset(key, field, value);
            return true;
        } catch (Exception ex) {  
            logger.error("set hash error. key:" + key + ", field:" + field + ".", ex);  
        } finally {  
        	jedis.close();   
        }
		return false; 
	}
	
	public String getHash(String key, String field) {
		Jedis jedis = null;    
        try {  
        	jedis = jedisPool.getResource(); 
            String result = jedis.hget(key, field);
            return result;
        } catch (Exception ex) {  
            logger.error("get hash field error. key:" + key + ", field:" + field + ".", ex);  
        } finally {  
        	jedis.close();   
        }
		return null;
	}
	
	public boolean delHashField(String key, String field) {
		Jedis jedis = null;    
        try {  
        	jedis = jedisPool.getResource(); 
            jedis.hdel(key, field);
            return true;
        } catch (Exception ex) {  
            logger.error("delete hash field error. key:" + key + ", field:" + field + ".", ex);  
        } finally {  
        	jedis.close();   
        }
		return false;
	}
	
	/**
	 * 向指定set中添加元素。
	 * @param key
	 * @param members
	 * @return
	 */
	public Long sadd(String key, String ... members) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long addCount = jedis.sadd(key, members);
			return addCount;
		} catch (Exception e) {
			logger.error("error:", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return 0L;
	}
	
	/**
	 * 判断给定的set中是否存在特定的一个元素。
	 * @param key set对应的key
	 * @param member 指定的元素
	 * @return
	 */
	public Boolean sismember(String key, String member) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Boolean isExist = jedis.sismember(key, member);
			return isExist;
		} catch (Exception e) {
			logger.error("error:", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return false;
		
	}
	
	/**
	 * 通过pipeline批量提交，向set中添加元素；
	 */
	public void saddPipeline(String key, List<String> members) {
		
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Pipeline pip = jedis.pipelined();
			for (String member : members) {
				pip.sadd(key, member);
			}
			pip.sync();
		} catch (Exception e) {
			logger.error("error:", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 通过pipeline批量提交，向set中添加元素，members参数的key是redis中set的元素，members的value是redis中set对应的key.
	 */
	public void saddPipeline(Map<String, String> members) {
		
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Pipeline pip = jedis.pipelined();
			for (Map.Entry<String, String> each : members.entrySet()) {
				pip.sadd(each.getValue(), each.getKey());
			}
			pip.sync();
		} catch (Exception e) {
			logger.error("error:", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
    
    
}
