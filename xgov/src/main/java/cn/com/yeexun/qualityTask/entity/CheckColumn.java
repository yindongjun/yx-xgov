package cn.com.yeexun.qualityTask.entity;

import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 用于封装每列校验后的数据，包括该列的本身的数据，和违反的校验规则信息。
 * @author yx-hj
 *
 */
public class CheckColumn {
	
	/** 该字段本身的数据值 */
	private Object value;
	
	/** 违反的规则详细信息 */
	private List<String> errorMsgs;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public List<String> getErrorMsgs() {
		return errorMsgs;
	}

	public void setErrorMsgs(List<String> errorMsgs) {
		this.errorMsgs = errorMsgs;
	}
	
	public String toJsonString() {
		return JSON.toJSONString(this);
	}

	@Override
	public String toString() {
		return toJsonString();
	}
	
	

}
