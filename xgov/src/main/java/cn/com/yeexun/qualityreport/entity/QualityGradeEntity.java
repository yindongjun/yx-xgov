package cn.com.yeexun.qualityreport.entity;

import java.util.Date;

import javax.persistence.Table;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.Unique;

@Table(name = "tb_quality_grade")
public class QualityGradeEntity extends Unique {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 报告模板名称 */
	private Long templateId;
	
	/** 评价结果名称，优良中差 */
	private String gradeName;
	
	/** 评价等级排序标记 */
	private Integer gradeOrder;
	
	/** 评价等价分数范围下限 */
	private Integer lowerLimit;
	
	/** 上限 */
	private Integer upperLimit;
	
	/** 描述 */
	private String description;
	private Integer deleteFlag;
	private Date createTime;
	private Long createBy;
	private Date updateTime;
	private Long updateBy;

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public Integer getGradeOrder() {
		return gradeOrder;
	}

	public void setGradeOrder(Integer gradeOrder) {
		this.gradeOrder = gradeOrder;
	}

	public Integer getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(Integer lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public Integer getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(Integer upperLimit) {
		this.upperLimit = upperLimit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
