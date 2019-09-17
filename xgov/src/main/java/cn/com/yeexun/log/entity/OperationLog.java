package cn.com.yeexun.log.entity;

import java.util.Date;

import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "operation_log")
public class OperationLog extends Unique implements OperationTimeAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8566515722773984139L;
	
	@OrderBy("desc")
	private Date createTime;
	
	private String personnel;
	
	private String operationMode;
	
	private String operation;
	
	private String operationUrl;
	
	private Date operationTime;
	
	@Transient
	private String operationKey;
	@Transient
	private String operationValue;
	
	public Date getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}
	
	public String getOperationKey() {
		return operationKey;
	}
	
	public void setOperationKey(String operationKey) {
		this.operationKey = operationKey;
	}
	
	public String getOperationValue() {
		return operationValue;
	}
	
	public void setOperationValue(String operationValue) {
		this.operationValue = operationValue;
	}
	
	@Override
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Override
	public void setLastModifyTime(Date lastModifyTime) {
		this.createTime = lastModifyTime;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public String getPersonnel() {
		return personnel;
	}

	public void setPersonnel(String personnel) {
		this.personnel = personnel;
	}

	public String getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOperationUrl() {
		return operationUrl;
	}

	public void setOperationUrl(String url) {
		this.operationUrl = url;
	}

	@Override
	public String toString() {
		return "OperationLog [id=" + id + "createTime=" + createTime + 
				"personnel=" + personnel + ", operationMode=" + operationMode + 
				", operation=" + operation + ", operationUrl=" + operationUrl + 
				", operationTime=" + operationTime + "]";
	}
	
}
