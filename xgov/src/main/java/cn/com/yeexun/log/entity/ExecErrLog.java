package cn.com.yeexun.log.entity;

import javax.persistence.Table;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "datatools_execerr_log")
public class ExecErrLog extends Unique {

	private static final long serialVersionUID = -2184925494309115364L;
	
	private long etlflowId;
	
	private String msg;

	private String type;

	
	public ExecErrLog() {
		super();
	}
	
	public ExecErrLog(long etlflowId, String msg, String type) {
		super();
		this.etlflowId = etlflowId;
		this.msg = msg;
		this.type = type;
	}

	public long getEtlflowId() {
		return etlflowId;
	}

	public void setEtlflowId(long etlflowId) {
		this.etlflowId = etlflowId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ExecErrLog [etlflowId=" + etlflowId + ", msg=" + msg
				+ ", type=" + type + "]";
	}
	


}
