/**
 * @file RangeCondition.java
 * @date 2016年8月4日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年8月4日 下午12:20:09
 */
public class RangeCondition {

	public final static int TYPE_IN = 1;
	public final static int TYPE_RANGE = 2;

	private String columnName;

	private String rangeType;

	private List<Object> in = new ArrayList<Object>();

	private Object[] range = new Object[2];

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getRangeType() {
		return rangeType;
	}

	public void setRangeType(String rangeType) {
		this.rangeType = rangeType;
	}

	public List<Object> getIn() {
		return in;
	}

	public void setIn(List<Object> in) {
		this.in = in;
	}

	public Object[] getRange() {
		return range;
	}

	public void setRange(Object[] range) {
		this.range = range;
	}

}
