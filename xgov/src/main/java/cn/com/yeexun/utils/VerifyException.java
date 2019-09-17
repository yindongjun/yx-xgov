package cn.com.yeexun.utils;


/**
 * @author yang.li
 * @version 1.0.0,2017年11月30日 下午3:44:46
 * @since yeexun
 */

public class VerifyException extends CommonException {

	public VerifyException(String message,Throwable cause){
		super(message,cause);
	}
	
	public VerifyException(String message){
		super(message);
	}
	
}
