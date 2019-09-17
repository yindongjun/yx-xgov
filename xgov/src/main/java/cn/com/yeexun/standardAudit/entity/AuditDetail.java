package cn.com.yeexun.standardAudit.entity;

import java.util.Date;

import javax.persistence.Table;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_standard_audit_detail")
public class AuditDetail extends Unique {
	
	private String auditor;
	
	private Date auditTime;
	
	private String auditResult;
	
	private String auditDesc;

	private long standardAuditId;

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

	public String getAuditResult() {
		return auditResult;
	}

	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}

	public String getAuditDesc() {
		return auditDesc;
	}

	public void setAuditDesc(String auditDesc) {
		this.auditDesc = auditDesc;
	}

	public long getStandardAuditId() {
		return standardAuditId;
	}

	public void setStandardAuditId(long standardAuditId) {
		this.standardAuditId = standardAuditId;
	}
}
