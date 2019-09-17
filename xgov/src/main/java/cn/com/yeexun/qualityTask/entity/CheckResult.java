package cn.com.yeexun.qualityTask.entity;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "check_result_detail")
public class CheckResult extends Unique {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long resultCountId;
	
	private String dataDetail;
	
	private String errorType;
	
	@Transient
	private String tableHead;
	
	

	public String getTableHead() {
		return tableHead;
	}

	public void setTableHead(String tableHead) {
		this.tableHead = tableHead;
	}

	public Long getResultCountId() {
		return resultCountId;
	}

	public void setResultCountId(Long resultCountId) {
		this.resultCountId = resultCountId;
	}

	public String getDataDetail() {
		return dataDetail;
	}

	public void setDataDetail(String dataDetail) {
		this.dataDetail = dataDetail;
	}
	
	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	

}
