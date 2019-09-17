package cn.com.yeexun.utils;

/**
 * 不支持的建表数据类型异常；
 *
 */
public class UnsupportColumnTypeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnsupportColumnTypeException(String message,Throwable cause){
		super(message,cause);
	}
	
	public UnsupportColumnTypeException(String message){
		super(message);
	}
			

}
