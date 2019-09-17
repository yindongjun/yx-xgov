package cn.com.yeexun.log.entity;

import java.util.Date;

import javax.persistence.OrderBy;
import javax.persistence.Table;

import cn.com.common.ssm.engine.bean.OperationTimeAware;
import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "sign_in_log")
public class SignInLog extends Unique implements OperationTimeAware {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8844663065073881465L;
	
	@OrderBy("desc")
	private Date createTime;

	private int status;
	
	private String personnel;
	
	private Date signInTime;

	private String signIp;

	private Date signOutTime;

	public Date getSignInTime() {
		return signInTime;
	}

	public void setSignInTime(Date signInTime) {
		this.signInTime = signInTime;
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

	public String getSignIp() {
		return signIp;
	}

	public void setSignIp(String signIp) {
		this.signIp = signIp;
	}

	public Date getSignOutTime() {
		return signOutTime;
	}

	public void setSignOutTime(Date signOutTime) {
		this.signOutTime = signOutTime;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "SignInLog [id=" + id + "createTime=" + createTime + 
				"personnel=" + personnel + ", signInTime=" + signInTime + 
				", signIp=" + signIp + ", signOutTime=" + signOutTime + 
				", status=" + status + "]";
	}

}


