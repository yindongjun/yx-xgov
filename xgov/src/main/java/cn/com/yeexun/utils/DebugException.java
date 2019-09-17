package cn.com.yeexun.utils;

import com.alibaba.fastjson.JSON;

public class DebugException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tableName;
	
	private String columnName;
	
	private String qualityTaskDetail;
	
	private String qualityType;

	public DebugException(String message,Throwable cause){
		super(message,cause);
	}
	
	public DebugException(String message){
		super(message);
	}
	
	public DebugException(Throwable e, String tableName, String columnName
			, String qualityTaskDetail, String qualityType){
		super(e);
		this.tableName = tableName;
		this.columnName = columnName;
		this.qualityTaskDetail = qualityTaskDetail;
		this.qualityType = qualityType;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getQualityTaskDetail() {
		return qualityTaskDetail;
	}

	public void setQualityTaskDetail(String qualityTaskDetail) {
		this.qualityTaskDetail = qualityTaskDetail;
	}
	
	public String getQualityType() {
		return qualityType;
	}

	public void setQualityType(String qualityType) {
		this.qualityType = qualityType;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
}
