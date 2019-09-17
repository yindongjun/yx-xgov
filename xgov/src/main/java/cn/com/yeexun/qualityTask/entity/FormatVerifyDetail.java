package cn.com.yeexun.qualityTask.entity;

import com.alibaba.fastjson.JSON;

public class FormatVerifyDetail {

	private String columnName;
	private String formatType;
	private String formatLength;
	private String isStandard;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getFormatType() {
		return formatType;
	}

	public void setFormatType(String formatType) {
		this.formatType = formatType;
	}

	public String getFormatLength() {
		return formatLength;
	}

	public void setFormatLength(String formatLength) {
		this.formatLength = formatLength;
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
