package cn.com.yeexun.qualityTask.entity;

import com.alibaba.fastjson.JSON;

public class RelationVerifyDetail {

	private String columnName;
	private String isStandard;
	private String targetTable;
	private String targetColumn;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getIsStandard() {
		return isStandard;
	}

	public void setIsStandard(String isStandard) {
		this.isStandard = isStandard;
	}

	public String getTargetTable() {
		return targetTable;
	}

	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}

	public String getTargetColumn() {
		return targetColumn;
	}

	public void setTargetColumn(String targetColumn) {
		this.targetColumn = targetColumn;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
