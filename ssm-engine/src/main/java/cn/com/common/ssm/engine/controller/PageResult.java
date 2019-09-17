/**
 * @file PageResult.java
 * @date 2016年5月21日
 * @version 1.0.0
 *
 * Copyright (c) 2016 Yeexun Information Tech, Inc. All Rights Reserved.
 */
package cn.com.common.ssm.engine.controller;

import java.io.Serializable;
import java.util.List;

/**
 * 
 *
 * @author yong.zhou
 * @version 1.0.0, 2016年5月21日 下午5:09:26
 * @since yeexun
 */
public class PageResult implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1417458395497494594L;
	private int count;
	private int total;
	private int currentPage;
	private int size;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	private List<Object> result;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public List<Object> getResult() {
		return result;
	}

	public void setResult(List<Object> result) {
		this.result = result;
	}

}
