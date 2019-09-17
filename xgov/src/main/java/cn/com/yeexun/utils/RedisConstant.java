package cn.com.yeexun.utils;

public class RedisConstant {
	
	//调度锁key
	public final static String 	DISPATCH_SYNC_KEY = "dispatchSyncKey";
	
	//调度Redis入库失败
	public final static String 	DISPATCH_REDIS_ADD_FAIL = "Redis调度入库失败";
	//调度新增失败
	public final static String 	DISPATCH_ADD_FAIL = "调度新增失败";
	//调度删除失败
	public final static String 	DISPATCH_REDIS_DEL_FAIL = "Redis删除标识失败";
	//调度删除失败
	public final static String 	DISPATCH_DEL_FAIL = "调度删除失败";
	//Redis  channel 
	public final static String 	REDIS_CHANNEL_EXPIRED = "__keyevent@0__:expired";
	public final static String 	REDIS_CHANNEL_DEL = "__keyevent@0__:del";
	public final static String 	REDIS_CHANNEL_SET = "__keyevent@0__:set";
	
	//调度设置过期时间 +半小时
	public final static int DISPATCH_INTERVAL = 20;
	
	//调度执行key
	public final static String 	DISPATCH_EXE_LOCK = "dispatchExeLock";
	//调度执行ip
	public final static String 	DISPATCH_EXE_IP = "dispatchExeIp";
	//调度更新失败
	public final static String 	DISPATCH_UPDATE_ERROR = "调度更新失败";
}
