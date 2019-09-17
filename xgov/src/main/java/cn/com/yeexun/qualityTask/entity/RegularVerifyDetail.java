package cn.com.yeexun.qualityTask.entity;

import com.alibaba.fastjson.JSON;

public class RegularVerifyDetail {

	private String columnName;
	private String isStandard;
	private String regularValue;
	private String regularName;

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

	public String getRegularValue() {
		return regularValue;
	}

	public void setRegularValue(String regularValue) {
		this.regularValue = regularValue;
	}
	
	public String getRegularName() {
		return regularName;
	}

	public void setRegularName(String regularName) {
		this.regularName = regularName;
	}

	public String toJsonString() {
		return JSON.toJSONString(this);
	}

	@Override
	public String toString() {
		return toJsonString();
	}
	
	

}
