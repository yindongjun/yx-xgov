/**
 * @file Condition.java
 * @date 2016年8月4日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.mapper;

import java.util.List;

/**
 * 
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年8月4日 下午12:02:25
 */
public class Condition<T> {

	public static final String EQUAL = "equalCondition";

	public static final String RANGE = "ranges";

	public static final String CUSTOM = "customSql";

	private T equalCondition;// entity

	private List<RangeCondition> ranges = null;

	private String customSql;

	public Condition() {
	}

	public Condition(T equalCondition) {
		this.equalCondition = equalCondition;
	}

	public Condition(T equalCondition, List<RangeCondition> ranges) {
		this.equalCondition = equalCondition;
		this.ranges = ranges;
	}

	public Condition(T equalCondition, List<RangeCondition> ranges, String customSql) {
		this.equalCondition = equalCondition;
		this.ranges = ranges;
		this.customSql = customSql;
	}

	public String getCustomSql() {
		return customSql;
	}

	public void setCustomSql(String customSql) {
		this.customSql = customSql;
	}

	public T getEqualCondition() {
		return equalCondition;
	}

	public void setEqualCondition(T equalCondition) {
		this.equalCondition = equalCondition;
	}

	public List<RangeCondition> getRanges() {
		return ranges;
	}

	public void setRanges(List<RangeCondition> ranges) {
		this.ranges = ranges;
	}

}
