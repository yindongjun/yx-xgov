package cn.com.yeexun.qualityTask.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Table;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.Unique;


/**
 * 
 * 质量任务对应的关联表
 * 
 **/
@SuppressWarnings("serial")
@Table(name = "design_task_table")
public class DesignTaskTable extends Unique implements Serializable {

	/****/
//	private Integer id;

	/****/
	private Long taskId;

	/****/
	private Long tableId;
	
	private Date createTime;
	
	private Date updateTime;
	
	private Integer isDel;
	
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getTableId() {
		return tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	



//	public void setId(Integer id){
//		this.id = id;
//	}

//	public Integer getId(){
//		return this.id;
//	}



}
