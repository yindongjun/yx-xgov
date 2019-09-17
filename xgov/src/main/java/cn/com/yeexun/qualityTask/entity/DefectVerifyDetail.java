package cn.com.yeexun.qualityTask.entity;

import com.alibaba.fastjson.JSON;

/**
 * 空值校验
 *
 */
public class DefectVerifyDetail {

	private String columnName;
	private String isStandard;

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

	public String toJsonString() {
		return JSON.toJSONString(this);
	}

	@Override
	public String toString() {
		return toJsonString();
	}

}
