package cn.com.yeexun.utils;

public class CommonException extends RuntimeException{
	

	public CommonException(String message,Throwable cause){
		super(message,cause);
	}
	
	public CommonException(String message){
		super(message);
	}

}
