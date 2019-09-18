package cn.com.yeexun.qualityTask.entity;

import com.alibaba.fastjson.JSON;

public class MatchDetail {
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
