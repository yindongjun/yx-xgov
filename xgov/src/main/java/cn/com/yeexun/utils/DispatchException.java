package cn.com.yeexun.utils;

public class DispatchException extends RuntimeException {

		public DispatchException(String message,Throwable cause){
			super(message,cause);
		}
		
		public DispatchException(String message){
			super(message);
		}
	
}
