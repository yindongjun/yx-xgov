package cn.com.yeexun.qualityreport.entity;

import com.alibaba.fastjson.JSON;

public class RadarMapVo {

	private String measureIndex;
	private Integer fullMarks;
	private Integer mark;

	public String getMeasureIndex() {
		return measureIndex;
	}

	public void setMeasureIndex(String measureIndex) {
		this.measureIndex = measureIndex;
	}

	public Integer getFullMarks() {
		return fullMarks;
	}

	public void setFullMarks(Integer fullMarks) {
		this.fullMarks = fullMarks;
	}

	public Integer getMark() {
		return mark;
	}

	public void setMark(Integer mark) {
		this.mark = mark;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
