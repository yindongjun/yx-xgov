package cn.com.yeexun.utils;

public class WorkFlowException  extends RuntimeException{

	public WorkFlowException(String message,Throwable cause){
		super(message,cause);
	}
	
	public WorkFlowException(String message){
		super(message);
	}
	
}
