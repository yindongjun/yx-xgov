package cn.com.yeexun.standardAudit.entity;

import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_standard_audit")
public class StandardAudit extends Unique implements OperationTimeAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String TYPE_DATA_ELEMENT = "0";
	
	public static final String TYPE_DATASET = "1";
	
	public static final String TYPE_CODE = "2";
	
	public static final String STATUS_UNAUDITED = "0";
	
	public static final String STATUS_AUDITED = "1";
	
	public static final String STATUS_BACK = "2";

	private String taskName;
	
	private String type;
	
	private String submitter;
	
	private Date submitTime;
	
	//审核状态(0:待审核,1:已审核)
	private String status;
	
	private long flowId;
	
	private String comment;
	

	//审核结果(0:通过，1:退回)
	private String auditResult;
	
	private String auditor;
	
	private Date auditTime;
	
	@Transient
	private String changeInfo;
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAuditResult() {
		return auditResult;
	}

	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}

	public long getFlowId() {
		return flowId;
	}

	public void setFlowId(long flowId) {
		this.flowId = flowId;
	}
	
	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	
	
	
	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	@Override
	public void setCreateTime(Date createTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLastModifyTime(Date lastModifyTime) {
		// TODO Auto-generated method stub
		
	}

	public String getChangeInfo() {
		return changeInfo;
	}

	public void setChangeInfo(String changeInfo) {
		this.changeInfo = changeInfo;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	


	
}
