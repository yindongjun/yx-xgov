package cn.com.yeexun.utils;

/**
 * 当保存带有hbase的工作流时，如果hbase表中包含不同列族的相同列名时，抛出该异常；
 *
 */
public class ColumnDuplicateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ColumnDuplicateException(String message,Throwable cause){
		super(message,cause);
	}
	
	public ColumnDuplicateException(String message){
		super(message);
	}
		
}
