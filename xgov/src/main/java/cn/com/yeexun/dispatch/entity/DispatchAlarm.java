package cn.com.yeexun.dispatch.entity;

import java.util.Date;

import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_dispatch_alarm")
public class DispatchAlarm extends Unique{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4636861828016162435L;
	
	public static final int REASON_SUCCESS = 1;
	public static final int REASON_FAIL = 0;
	public static final int REASON_ALL = 2;
	
	public static final int STATUS_OPEN = 1;
	public static final int STAUTS_CLOSE = 0;

	private String alarmName;
	
	
	/**
	 * 报警原因，1：成功，2：失败，3：成功+失败
	 */
	private Integer alarmReasion;
	
	/**
	 * 报警方式，1：邮件，2：短信，3：邮件+短信
	 */
	private Integer alarmMethod;
	
	/**
	 * 报警接收人，报警人的id用逗号隔开
	 */
	private String receivePeople;
	
	/**
	 * 报警状态:0-关  1-开
	 */
	private Integer status;
	
	/**
	 * 是否被引用:0-否   1-是
	 */
	private Integer isUsed;
	
	private Long createPeople;
	
	private Long updatePeople;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	public DispatchAlarm() {};
	

	public DispatchAlarm(String alarmName, Integer alarmReasion, Integer alarmMethod, String receivePeople,
			Integer status, Integer isUsed, Long createPeople, Long updatePeople, Date createTime, Date updateTime) {
		super();
		this.alarmName = alarmName;
		this.alarmReasion = alarmReasion;
		this.alarmMethod = alarmMethod;
		this.receivePeople = receivePeople;
		this.status = status;
		this.isUsed = isUsed;
		this.createPeople = createPeople;
		this.updatePeople = updatePeople;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}


	

	/**
	 * @param alarmName
	 * @param alarmReasion
	 * @param alarmMethod
	 * @param receivePeople
	 * @param status
	 * @param isUsed
	 * @param createPeople
	 * @param updatePeople
	 * @param createTime
	 * @param updateTime
	 */
	public DispatchAlarm(Long alarmId ,String alarmName, Integer alarmReasion, Integer alarmMethod, String receivePeople,
			Integer status, Integer isUsed, Long createPeople, Long updatePeople, Date createTime, Date updateTime) {
		super();
		this.id=alarmId;
		this.alarmName = alarmName;
		this.alarmReasion = alarmReasion;
		this.alarmMethod = alarmMethod;
		this.receivePeople = receivePeople;
		this.status = status;
		this.isUsed = isUsed;
		this.createPeople = createPeople;
		this.updatePeople = updatePeople;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}


	public String getAlarmName() {
		return alarmName;
	}


	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}




	public void setId(Long id) {
		this.id = id;
	}


	public Integer getAlarmReasion() {
		return alarmReasion;
	}


	public void setAlarmReasion(Integer alarmReasion) {
		this.alarmReasion = alarmReasion;
	}


	public Integer getAlarmMethod() {
		return alarmMethod;
	}


	public void setAlarmMethod(Integer alarmMethod) {
		this.alarmMethod = alarmMethod;
	}


	public String getReceivePeople() {
		return receivePeople;
	}


	public void setReceivePeople(String receivePeople) {
		this.receivePeople = receivePeople;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public Integer getIsUsed() {
		return isUsed;
	}


	public void setIsUsed(Integer isUsed) {
		this.isUsed = isUsed;
	}


	public Long getCreatePeople() {
		return createPeople;
	}


	public void setCreatePeople(Long createPeople) {
		this.createPeople = createPeople;
	}


	public Long getUpdatePeople() {
		return updatePeople;
	}


	public void setUpdatePeople(Long updatePeople) {
		this.updatePeople = updatePeople;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public Date getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
