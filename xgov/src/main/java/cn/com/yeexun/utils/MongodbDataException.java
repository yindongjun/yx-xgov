package cn.com.yeexun.utils;

/**
 * @author yang.li
 * @version 1.0.0,2018年2月28日 上午9:44:46
 * @since yeexun
 */
public class MongodbDataException extends RuntimeException{
	public MongodbDataException(String message,Throwable cause){
		super(message,cause);
	}
	
	public MongodbDataException(String message){
		super(message);
	}

}
