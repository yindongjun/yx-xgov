package cn.com.yeexun.qualityreport.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;

import cn.com.common.ssm.engine.bean.Unique;

/**
 * 
 * 质量报告评分指标表
 *
 */
@Table(name = "tb_quality_measure")
public class QualityMeasureEntity extends Unique {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 评价指标名称 */
	private String measureIndex;
	
	/** 校验类型编码 */
	private String verifyCode;
	
	/** 权重 */
	private Integer weight;
	
	/** 父级指标id */
	private Long parentId;
	
	/** 模板id */
	private Long templateId;
	
	/** 描述 */
	private String description;
	private Integer deleteFlag;
	private Date createTime;
	private Long createBy;
	private Date updateTime;
	private Long updateBy;
	
	@Transient
	private List<QualityMeasureEntity> children;
	
	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
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
	
	public String getMeasureIndex() {
		return measureIndex;
	}

	public void setMeasureIndex(String measureIndex) {
		this.measureIndex = measureIndex;
	}

	public List<QualityMeasureEntity> getChildren() {
		return children;
	}

	public void setChildren(List<QualityMeasureEntity> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
