package cn.com.yeexun.qualityTask.entity;

import com.alibaba.fastjson.JSON;

public class EnumVerifyDetail {

	private String columnName;
	private String isStandard;
	private Long codeSetId;
	/** 值域类型 */
	private String codeSetName;

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

	public Long getCodeSetId() {
		return codeSetId;
	}

	public void setCodeSetId(Long codeSetId) {
		this.codeSetId = codeSetId;
	}

	public String getCodeSetName() {
		return codeSetName;
	}

	public void setCodeSetName(String codeSetName) {
		this.codeSetName = codeSetName;
	}

	public String toJsonString() {
		return JSON.toJSONString(this);
	}

	@Override
	public String toString() {
		return toJsonString();
	}

}
