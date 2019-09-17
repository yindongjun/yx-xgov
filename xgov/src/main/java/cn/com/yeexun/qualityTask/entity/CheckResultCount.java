package cn.com.yeexun.qualityTask.entity;

import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "check_result_count")
public class CheckResultCount extends Unique {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 已处理 */
	public static final int STATUS_SOLVED = 1;
	/** 待处理 */
	public static final int STATUS_PENDING = 0;

	private Long designTableInfoId;
	
	private Long taskHisId;
	
	private String tableName;
	
//	private Long designSourceId;
	
	private Long datasourceId;
	
	private Integer errorDataNum;
	
	//问题责任人
	private String owner;
	
	private Integer status;
	
	private String dealPeople;
	
	private Date dealTime;
	
	//处理情况详情
	private String dealComment;
	
	@Transient
	private String dataSourceName;

	private String columnNames;
	
	private Date createTime;
	
	private Date updateTime;
	
	private Integer deleteFlag;
	
	private Integer totalRows;
	
	@Transient
	private String taskName;
	
	
	
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String columnNames) {
		this.columnNames = columnNames;
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Long getDesignTableInfoId() {
		return designTableInfoId;
	}

	public void setDesignTableInfoId(Long designTableInfoId) {
		this.designTableInfoId = designTableInfoId;
	}

	public Long getTaskHisId() {
		return taskHisId;
	}

	public void setTaskHisId(Long taskHisId) {
		this.taskHisId = taskHisId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	

//	public Long getDesignSourceId() {
//		return designSourceId;
//	}
//
//	public void setDesignSourceId(Long designSourceId) {
//		this.designSourceId = designSourceId;
//	}

	public Integer getErrorDataNum() {
		return errorDataNum;
	}

	public void setErrorDataNum(Integer errorDataNum) {
		this.errorDataNum = errorDataNum;
	}

	

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getDealPeople() {
		return dealPeople;
	}

	public void setDealPeople(String dealPeople) {
		this.dealPeople = dealPeople;
	}

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

	public String getDealComment() {
		return dealComment;
	}

	public void setDealComment(String dealComment) {
		this.dealComment = dealComment;
	}
	
	

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
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

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	public Long getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(Long datasourceId) {
		this.datasourceId = datasourceId;
	}
	
	public Integer getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	
	
	

}
