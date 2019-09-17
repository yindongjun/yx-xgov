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
	private List<MatchDetail> matchDetails;

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

	public List<MatchDetail> getMatchDetails() {
		return matchDetails;
	}

	public void setMatchDetails(List<MatchDetail> matchDetails) {
		this.matchDetails = matchDetails;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	public static class MatchDetail {
		private String matchType;
		private String matchChar;
		private String targetValue;

		public String getMatchType() {
			return matchType;
		}

		public void setMatchType(String matchType) {
			this.matchType = matchType;
		}

		public String getMatchChar() {
			return matchChar;
		}

		public void setMatchChar(String matchChar) {
			this.matchChar = matchChar;
		}

		public String getTargetValue() {
			return targetValue;
		}

		public void setTargetValue(String targetValue) {
			this.targetValue = targetValue;
		}

		@Override
		public String toString() {
			return JSON.toJSONString(this);
		}

	}

}


