package cn.com.yeexun.dispatch.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class TaskHisQueryVo {
	
	private Long taskId;
	
	private String status;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTimeLower;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTimeUpper;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTimeLower;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTimeUpper;

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartTimeLower() {
		return startTimeLower;
	}

	public void setStartTimeLower(Date startTimeLower) {
		this.startTimeLower = startTimeLower;
	}

	public Date getEndTimeUpper() {
		return endTimeUpper;
	}

	public void setEndTimeUpper(Date endTimeUpper) {
		this.endTimeUpper = endTimeUpper;
	}
	
	

	public Date getStartTimeUpper() {
		return startTimeUpper;
	}

	public void setStartTimeUpper(Date startTimeUpper) {
		this.startTimeUpper = startTimeUpper;
	}

	public Date getEndTimeLower() {
		return endTimeLower;
	}

	public void setEndTimeLower(Date endTimeLower) {
		this.endTimeLower = endTimeLower;
	}

	@Override
	public String toString() {
		return "TaskHisQueryVo [taskId=" + taskId + ", status=" + status + ", startTimeLower=" + startTimeLower
				+ ", startTimeUpper=" + startTimeUpper + ", endTimeLower=" + endTimeLower + ", endTimeUpper="
				+ endTimeUpper + "]";
	}

	
	
	

}
