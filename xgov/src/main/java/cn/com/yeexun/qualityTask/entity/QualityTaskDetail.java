package cn.com.yeexun.qualityTask.entity;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "quality_task_detail")
public class QualityTaskDetail extends Unique{
	
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_TYPE = "1";
	public static final String FORMAT_VERIFY = "2";
	public static final String DEFECT_VERIFY = "3";
	public static final String INTERVAL_VERIFY = "4";
	public static final String ENUM_VERIFY = "5";
	public static final String REGULAR_VERIFY = "6";
	public static final String UNIQUE_VERIFY = "31";
	public static final String RELATION_VERIFY = "32";
	public static final String MAPPING_VERIFY = "33";
	public static final String NOTEQUAL_VERIFY = "34";
	
	public static final int ENABLE = 1;
	public static final int DISABLE = 0;
	
	
//	private Long designTableId;
	
	private String verifyType;
	
	private String isStandard;
	
	private String verifyDetail;
	
	private String deleteFlag;
	
	private String name;
	
	private String columnName;
	
	private Long datasourceId;
	
	private Long elementId;
	
	//	@Transient
	private String tableName;
	
	@Transient
	private String taskName;
	
	private Long relationId;
	
	private Integer enable;
	
	public String getIsStandard() {
		return isStandard;
	}

	public void setIsStandard(String isStandard) {
		this.isStandard = isStandard;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public Long getRelationId() {
		return relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public Long getDesignTableId() {
//		return designTableId;
//	}

	public String getVerifyType() {
		return verifyType;
	}

	public String getVerifyDetail() {
		return verifyDetail;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

//	public void setDesignTableId(Long designTableId) {
//		this.designTableId = designTableId;
//	}

	public void setVerifyType(String verifyType) {
		this.verifyType = verifyType;
	}

	public void setVerifyDetail(String verifyDetail) {
		this.verifyDetail = verifyDetail;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	public Long getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(Long datasourceId) {
		this.datasourceId = datasourceId;
	}

	public Long getElementId() {
		return elementId;
	}

	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
	
	

}
