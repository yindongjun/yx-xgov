package cn.com.yeexun.dispatch.entity;

import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.Unique;
@Table(name = "tb_dispatch_task")
public class DispatchTask extends Unique{

	private static final long serialVersionUID = 1L;
	
	public static final String STATUS_RUNNING = "1";
	public static final String STATUS_PAUSED = "2";
	public static final String STATUS_FINALIZED = "3";
	
	public static final String JOBCRON_MONTH = "0";
	public static final String JOBCRON_WEEK = "1";
	public static final String JOBCRON_DAY = "2";
	public static final String JOBCRON_HOUR = "3";
	public static final String JOBCRON_ONLYONCE = "4";
	
	public static final String COLLECT_TASK = "0";
	public static final String QUALITY_TASK = "1";
	
	private String taskName;
	
	private String taskType;
	
	private Long taskId;
	
	private Date createTime;
	
	private Date lastModifyTime;
	
	private String deleteFlag;
	
	private String cycleType;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	
	/**
	 * 调度频率(0-每月，1-每周，2-每天，3-每小时)
	 */
	private String jobCron;
	
	private String status;
	
	private Long alarmId;
	
	@Transient
	private String alarmName;
	
	private String triggerTime;
	
	private String cronExpression;
	
	private String cronDescription;
	
	
	
	public Long getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(Long alarmId) {
		this.alarmId = alarmId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Long getTaskId() {
		return taskId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getCycleType() {
		return cycleType;
	}

	public void setCycleType(String cycleType) {
		this.cycleType = cycleType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getJobCron() {
		return jobCron;
	}

	public void setJobCron(String jobCron) {
		this.jobCron = jobCron;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(String triggerTime) {
		this.triggerTime = triggerTime;
	}

	

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getCronDescription() {
		return cronDescription;
	}

	public void setCronDescription(String cronDescription) {
		this.cronDescription = cronDescription;
	}


	public String getAlarmName() {
		return alarmName;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}


	

	

	

}
