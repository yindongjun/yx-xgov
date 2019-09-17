package cn.com.yeexun.dispatch.entity;

import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.Unique;
@Table(name = "tb_dispatch_history")
public class DispatchHistory extends Unique{
	
	private static final long serialVersionUID = 1L;
	
	public final static String STATUS_ON = "0";
	public final static String STATUS_SUCCEED = "1";
	public final static String STATUS_FILED = "2";

	private Long taskId;
	
	private String taskType;
	
	private Date startTime;
	
	private Date endTime;
	
	private String status;
	
	private String spent;
	
	@Transient
	private String taskName;
	
	@Transient
	private String cycleType;
	
	@Transient
	private Integer execCount;
	
	/** 对应的tb_dispatch_task表的主键id */
	@Transient
	private Long dispatchTaskId;

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSpent() {
		return spent;
	}

	public void setSpent(String spent) {
		this.spent = spent;
	}
	
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getCycleType() {
		return cycleType;
	}

	public void setCycleType(String cycleType) {
		this.cycleType = cycleType;
	}

	public Integer getExecCount() {
		return execCount;
	}

	public void setExecCount(Integer execCount) {
		this.execCount = execCount;
	}
	
	public Long getDispatchTaskId() {
		return dispatchTaskId;
	}

	public void setDispatchTaskId(Long dispatchTaskId) {
		this.dispatchTaskId = dispatchTaskId;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
	
	
	
}
