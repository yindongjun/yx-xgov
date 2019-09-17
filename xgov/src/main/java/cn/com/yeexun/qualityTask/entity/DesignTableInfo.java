package cn.com.yeexun.qualityTask.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;

@Table(name = "design_table_info")
public class DesignTableInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String CYCLE_TYPE_ONLYONCE = "0";
	
	public static final String CYCLE_TYPE_CYCLE = "1";
	
	public static final String STATUS_DRAFT = "0";
	public static final String STATUS_COMMITED = "2";
	public static final String STATUS_UNCOMMIT = "3";
	public static final String STATUS_CHANGING = "4";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Transient
	private Long designId;
	
//	private Long designSourceId;
	private Long datasourceId;
	
	private String taskName;
	
	private String tableName;
	
//	private Long ruleId;
	
	private String status;
	
	private Date createTime;
	
	private Date updateTime;
	
	private Long createUser;
	
	private Long updateUser;
	
	private String deleteFlag;
	
	/*private String cycleType;
	
	private Date startTime;
	
	private Date endTime;
	
	private String jobCron;
	
	private String moveRightdata;
	
	private String moveErrordata;
	
	private Long rightSourceId;
	
	private Long errorSourceId;*/
	
	private String description;
	
	private String changeNotice;
	
//	private String taskDetail;
	
//	private Long alarmId;
	
	private String isRuleChange;
	
	private Integer isDispatch;
	
//	private String triggerTime;
	
	@Transient
	private String sourceName;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDesignId() {
		return designId;
	}

	public void setDesignId(Long designId) {
		this.designId = designId;
	}

	/*public String getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(String triggerTime) {
		this.triggerTime = triggerTime;
	}*/

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public Integer getIsDispatch() {
		return isDispatch;
	}

	public void setIsDispatch(Integer isDispatch) {
		this.isDispatch = isDispatch;
	}

	public String getIsRuleChange() {
		return isRuleChange;
	}

	public void setIsRuleChange(String isRuleChange) {
		this.isRuleChange = isRuleChange;
	}

//	public Long getDesignSourceId() {
//		return designSourceId;
//	}

	public String getTaskName() {
		return taskName;
	}

	public String getTableName() {
		return tableName;
	}

//	public Long getRuleId() {
//		return ruleId;
//	}

	public String getStatus() {
		return status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public Long getCreateUser() {
		return createUser;
	}

	public Long getUpdateUser() {
		return updateUser;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	/*public String getCycleType() {
		return cycleType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public String getJobCron() {
		return jobCron;
	}

	public String getMoveRightdata() {
		return moveRightdata;
	}

	public String getMoveErrordata() {
		return moveErrordata;
	}

	public Long getRightSourceId() {
		return rightSourceId;
	}

	public Long getErrorSourceId() {
		return errorSourceId;
	}*/

	public String getDescription() {
		return description;
	}

	public String getChangeNotice() {
		return changeNotice;
	}

	/*public String getTaskDetail() {
		return taskDetail;
	}

	public Long getAlarmId() {
		return alarmId;
	}*/

//	public void setDesignSourceId(Long designSourceId) {
//		this.designSourceId = designSourceId;
//	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Long getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(Long datasourceId) {
		this.datasourceId = datasourceId;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

//	public void setRuleId(Long ruleId) {
//		this.ruleId = ruleId;
//	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	/*public void setCycleType(String cycleType) {
		this.cycleType = cycleType;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setJobCron(String jobCron) {
		this.jobCron = jobCron;
	}

	public void setMoveRightdata(String moveRightdata) {
		this.moveRightdata = moveRightdata;
	}

	public void setMoveErrordata(String moveErrordata) {
		this.moveErrordata = moveErrordata;
	}

	public void setRightSourceId(Long rightSourceId) {
		this.rightSourceId = rightSourceId;
	}

	public void setErrorSourceId(Long errorSourceId) {
		this.errorSourceId = errorSourceId;
	}*/

	public void setDescription(String description) {
		this.description = description;
	}

	public void setChangeNotice(String changeNotice) {
		this.changeNotice = changeNotice;
	}

	/*public void setTaskDetail(String taskDetail) {
		this.taskDetail = taskDetail;
	}

	public void setAlarmId(Long alarmId) {
		this.alarmId = alarmId;
	}*/

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	/**
	 * 通过数据源id+表名 来确定
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj != null){
			DesignTableInfo info = (DesignTableInfo) obj;
			if(info.getDesignId() != null && info.getDesignId() == this.designId)
				return true;
		}
		return super.equals(obj);
	}
	
	

}
