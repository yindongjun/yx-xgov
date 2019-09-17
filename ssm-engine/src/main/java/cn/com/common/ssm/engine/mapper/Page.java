/**
 * @file Page.java
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
 * @version 1.0.0, 2016年8月4日 下午12:02:42
 */
public class Page<T> {

	private int count;	//记录总数

	private int pageCount;

	private int length = 10;	//页大小
	
	private int start = 0;	//下标启始位置
	
	private List<T> data;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
		this.pageCount = (count % length > 0) ? (count / length + 1) : (count / length);
	}


	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

}
