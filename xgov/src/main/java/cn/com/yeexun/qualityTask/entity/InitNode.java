package cn.com.yeexun.qualityTask.entity;

import com.alibaba.fastjson.JSON;

public class InitNode {

	private String sql;
	private String extractType;
	private Integer fetchSize;
	private String pkColumn;
	
	public Integer getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(Integer fetchSize) {
		this.fetchSize = fetchSize;
	}

	public String getPkColumn() {
		return pkColumn;
	}

	public void setPkColumn(String pkColumn) {
		this.pkColumn = pkColumn;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getExtractType() {
		return extractType;
	}

	public void setExtractType(String extractType) {
		this.extractType = extractType;
	}

	public String toJsonString() {
		return JSON.toJSONString(this);
	}

	@Override
	public String toString() {
		return toJsonString();
	}

}
