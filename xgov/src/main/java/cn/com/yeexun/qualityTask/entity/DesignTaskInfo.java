package cn.com.yeexun.qualityTask.entity;

import java.io.Serializable;

import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.Unique;

/**
 * 
 * 质量任务调度表
 * 
 **/
@SuppressWarnings("serial")
@Table(name = "design_task_info")
public class DesignTaskInfo extends Unique implements Serializable {

	/****/
//	private Integer id;

	/****/
	private String taskName;

	/**任务类型：0-采集任务  1-质量任务**/
	private String taskType;

	/**执行方式：0-一次性   1-周期性**/
	private String cycleType;

	/****/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date startTime;

	/****/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date endTime;

	/****/
	private String jobCron;

	/****/
	private String triggerTime;

	/****/
	private String cronExpression;

	/****/
	private String taskDetail;

	/**告警id**/
	private Long alarmId;

	/**是否移动正确数据：0不移动1移动**/
//	private String moveRightdata;
//
//	/**是否移动错误数据：0不移动1移动**/
//	private String moveErrordata;
//
//	/**正确数据写入的数据**/
//	private Integer rightSourceId;
//
//	/**错误数据写入的数据源**/
//	private Integer errorSourceId;

	/****/
	private String description;

	/****/
	private String deleteFlag;

	/****/
	private java.util.Date createTime;

	/****/
	private Integer createUser;

	/****/
	private java.util.Date updateTime;

	/****/
	private Integer updateUser;
	
	@Transient
	private Long datasourceId;
	
	@Transient
	private String datasourceName;
	
	@Transient
	private String tableInfoIds;
	
	public String getTableInfoIds() {
		return tableInfoIds;
	}


	public void setTableInfoIds(String tableInfoIds) {
		this.tableInfoIds = tableInfoIds;
	}


	public String getDatasourceName() {
		return datasourceName;
	}


	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}


	public Long getDatasourceId() {
		return datasourceId;
	}


	public void setDatasourceId(Long datasourceId) {
		this.datasourceId = datasourceId;
	}


	public void setId(Integer id){
		this.id = id;
	}


	public void setTaskName(String taskName){
		this.taskName = taskName;
	}

	public String getTaskName(){
		return this.taskName;
	}

	public void setTaskType(String taskType){
		this.taskType = taskType;
	}

	public String getTaskType(){
		return this.taskType;
	}

	public void setCycleType(String cycleType){
		this.cycleType = cycleType;
	}

	public String getCycleType(){
		return this.cycleType;
	}

	public void setStartTime(java.util.Date startTime){
		this.startTime = startTime;
	}

	public java.util.Date getStartTime(){
		return this.startTime;
	}

	public void setEndTime(java.util.Date endTime){
		this.endTime = endTime;
	}

	public java.util.Date getEndTime(){
		return this.endTime;
	}

	public void setJobCron(String jobCron){
		this.jobCron = jobCron;
	}

	public String getJobCron(){
		return this.jobCron;
	}

	
	public String getTriggerTime() {
		return triggerTime;
	}


	public void setTriggerTime(String triggerTime) {
		this.triggerTime = triggerTime;
	}


	public void setCronExpression(String cronExpression){
		this.cronExpression = cronExpression;
	}

	public String getCronExpression(){
		return this.cronExpression;
	}

	public void setTaskDetail(String taskDetail){
		this.taskDetail = taskDetail;
	}

	public String getTaskDetail(){
		return this.taskDetail;
	}

	public void setAlarmId(Long alarmId){
		this.alarmId = alarmId;
	}

	public Long getAlarmId(){
		return this.alarmId;
	}

	/*public void setMoveRightdata(String moveRightdata){
		this.moveRightdata = moveRightdata;
	}

	public String getMoveRightdata(){
		return this.moveRightdata;
	}

	public void setMoveErrordata(String moveErrordata){
		this.moveErrordata = moveErrordata;
	}

	public String getMoveErrordata(){
		return this.moveErrordata;
	}

	public void setRightSourceId(Integer rightSourceId){
		this.rightSourceId = rightSourceId;
	}

	public Integer getRightSourceId(){
		return this.rightSourceId;
	}

	public void setErrorSourceId(Integer errorSourceId){
		this.errorSourceId = errorSourceId;
	}

	public Integer getErrorSourceId(){
		return this.errorSourceId;
	}*/

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return this.description;
	}

	public void setDeleteFlag(String deleteFlag){
		this.deleteFlag = deleteFlag;
	}

	public String getDeleteFlag(){
		return this.deleteFlag;
	}

	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}

	public java.util.Date getCreateTime(){
		return this.createTime;
	}

	public void setCreateUser(Integer createUser){
		this.createUser = createUser;
	}

	public Integer getCreateUser(){
		return this.createUser;
	}

	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}

	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}

	public void setUpdateUser(Integer updateUser){
		this.updateUser = updateUser;
	}

	public Integer getUpdateUser(){
		return this.updateUser;
	}


	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	

}
