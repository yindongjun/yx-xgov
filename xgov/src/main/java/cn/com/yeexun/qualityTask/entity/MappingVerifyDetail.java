package cn.com.yeexun.qualityTask.entity;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class MappingVerifyDetail {

	public static final String PRIFIX_MATCH = "1";
	public static final String SUFFIX_MATCH = "2";
	public static final String FULL_MATCH = "3";

	private String sourceColumn;
	private String isStandard;
	private String targetColumn;
	private List<MatchDetail> mappingRule;

	public String getSourceColumn() {
		return sourceColumn;
	}

	public void setSourceColumn(String sourceColumn) {
		this.sourceColumn = sourceColumn;
	}

	public String getIsStandard() {
		return isStandard;
	}

	public void setIsStandard(String isStandard) {
		this.isStandard = isStandard;
	}

	public String getTargetColumn() {
		return targetColumn;
	}

	public void setTargetColumn(String targetColumn) {
		this.targetColumn = targetColumn;
	}

	public List<MatchDetail> getMappingRule() {
		return mappingRule;
	}

	public void setMappingRule(List<MatchDetail> mappingRule) {
		this.mappingRule = mappingRule;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	

}


