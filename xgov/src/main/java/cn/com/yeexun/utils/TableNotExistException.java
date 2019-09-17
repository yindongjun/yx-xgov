package cn.com.yeexun.utils;

/**
 * 保存任务时进行校验，若目的库中不存在对应的表，则抛出该异常；
 * @author yx-hj
 *
 */
public class TableNotExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/** 目的数据源id */
	private Long dataSourceId;
	
	/** 表名 */
	private String tableName;
	
	public TableNotExistException(String message,Throwable cause, Long dataSourceId, String tableName) {
		super(message,cause);
		this.dataSourceId = dataSourceId;
		this.tableName = tableName;
	}

	public TableNotExistException(String message,Throwable cause){
		super(message,cause);
	}
	
	public TableNotExistException(String message){
		super(message);
	}

	public Long getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(Long dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
