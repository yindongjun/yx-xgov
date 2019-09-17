package cn.com.yeexun.qualityTask.entity;

import java.util.Date;

import javax.persistence.Table;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "quality_task_log")
public class QualityTaskLog extends Unique {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String INFO = "INFO";
	public static final String WARN = "WARN";
	public static final String ERROR = "ERROR";

	private Long dispatchHisId;
	
	private String tableName;
	
	private String level;
	
	private String logDetail;
	
	private Date createTime;
	
	private Long taskInfoId;
	
	public QualityTaskLog() {
		super();
	}

	public QualityTaskLog(Long dispatchHisId, String tableName, String level, String logDetail, Long taskInfoId) {
		super();
		this.dispatchHisId = dispatchHisId;
		this.tableName = tableName;
		this.level = level;
		this.logDetail = logDetail;
		this.taskInfoId = taskInfoId;
		this.createTime = new Date();
	}



	public Long getDispatchHisId() {
		return dispatchHisId;
	}

	public void setDispatchHisId(Long dispatchHisId) {
		this.dispatchHisId = dispatchHisId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLogDetail() {
		return logDetail;
	}

	public void setLogDetail(String logDetail) {
		this.logDetail = logDetail;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getTaskInfoId() {
		return taskInfoId;
	}

	public void setTaskInfoId(Long taskInfoId) {
		this.taskInfoId = taskInfoId;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	

}
